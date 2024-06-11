package tech.calaverita.sfast_xpress.utils;

import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import tech.calaverita.sfast_xpress.dto.LiquidacionDTO;
import tech.calaverita.sfast_xpress.models.mariaDB.*;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoModel;
import tech.calaverita.sfast_xpress.pojos.ModelValidation;
import tech.calaverita.sfast_xpress.pojos.PagoConLiquidacion;
import tech.calaverita.sfast_xpress.retrofit.RetrofitOdoo;
import tech.calaverita.sfast_xpress.retrofit.pojos.LiquidacionBody;
import tech.calaverita.sfast_xpress.retrofit.pojos.PagoBody;
import tech.calaverita.sfast_xpress.retrofit.pojos.PagoList;
import tech.calaverita.sfast_xpress.retrofit.pojos.ResponseBodyXms;
import tech.calaverita.sfast_xpress.services.*;
import tech.calaverita.sfast_xpress.services.views.PrestamoService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public final class PagoUtil {
    private static AgenciaService agenciaService;
    private static GerenciaService gerenciaService;
    private static LiquidacionService liquidacionService;
    private static PagoService pagoService;
    private static CalendarioService calendarioService;
    private static PrestamoService prestamoService;

    private PagoUtil(AgenciaService agenciaService, GerenciaService gerenciaService,
                     LiquidacionService liquidacionService, PagoService pagoService,
                     CalendarioService calendarioService, PrestamoService prestamoService) {
        PagoUtil.agenciaService = agenciaService;
        PagoUtil.gerenciaService = gerenciaService;
        PagoUtil.liquidacionService = liquidacionService;
        PagoUtil.pagoService = pagoService;
        PagoUtil.calendarioService = calendarioService;
        PagoUtil.prestamoService = prestamoService;
    }

    public static ModelValidation modValPagoModelValidation(PagoConLiquidacion pagoConLiquidacion,
                                                            PrestamoModel prestamoModel) {
        String strResponse_O = "";
        HttpStatus httpStatus_O = HttpStatus.CREATED;
        boolean boolIsOnline_O = true;

        if (pagoConLiquidacion.getPagoId() == null) {
            strResponse_O += "Debe Ingresar El 'pagoId'";
            httpStatus_O = HttpStatus.BAD_REQUEST;
            boolIsOnline_O = false;
        }

        if (((pagoConLiquidacion.getPrestamoId() == null) || pagoConLiquidacion.getPrestamoId()
                .equalsIgnoreCase(""))) {
            strResponse_O += "\nDebe Ingresar El 'prestamoId'";
            httpStatus_O = HttpStatus.BAD_REQUEST;
            boolIsOnline_O = false;
        }

        if (PagoUtil.pagoService.existsById(pagoConLiquidacion.getPagoId())) {
            strResponse_O += "\nEl Pago Ya Existe";
            httpStatus_O = HttpStatus.CONFLICT;
            boolIsOnline_O = false;
        }

        if (PagoUtil.boolIsPagoMigracion(pagoConLiquidacion.getPrestamoId(), pagoConLiquidacion.getAnio(),
                pagoConLiquidacion.getSemana())) {
            strResponse_O += "\nEl Pago cuenta con migración";
            httpStatus_O = HttpStatus.CONFLICT;
            boolIsOnline_O = false;
        }

        if (prestamoModel == null) {
            strResponse_O += "\nNo Se Encontró Ningún Prestamo Con Ese 'prestamoId'";
            httpStatus_O = HttpStatus.NOT_FOUND;
            boolIsOnline_O = false;
        }

        return new ModelValidation(strResponse_O, httpStatus_O, boolIsOnline_O);
    }

    public static void subProcessPayment(ModelValidation modelValidation, PagoConLiquidacion pagoConLiquidacion,
                                         PrestamoModel prestamoModel) {
        String strSession = "session_id=76d814874514726176f0615260848da2aab725ea";
        PagoModel pagMod = PagoUtil.pagoModelFromPagoConLiquidacion(pagoConLiquidacion);
        LiquidacionModel liqMod = pagoConLiquidacion.getInfoLiquidacion();

        modelValidation.setStrResponse("Pago Insertado con Éxito");
        modelValidation.setHttpStatus(HttpStatus.CREATED);

        //                                                  //A continuación se hace una validación para comprobar si el
        //                                                  //      pago recibido trae liquidación
        if (liqMod != null) {
            liqMod.setPagoId(pagMod.getPagoId());
            pagMod.setAbreCon(prestamoModel.getTotalAPagar() - PagoUtil.pagoService
                    .findCobradoByPrestamoId(pagoConLiquidacion.getPrestamoId()));
            pagMod.setCierraCon(0.0);
            pagMod.setEsPrimerPago(false);

            retrofit2.Call<ResponseBodyXms> callrespBodyXms = RetrofitOdoo.getInstance().getApi()
                    .liquidacionCreateOne(strSession, new LiquidacionBody(liqMod));
            RetrofitOdooUtil.sendCall(callrespBodyXms);
        } else {
            pagMod.setAbreCon(prestamoModel.getTotalAPagar() - PagoUtil.pagoService.findCobradoByPrestamoId(
                    pagoConLiquidacion.getPrestamoId()));
            pagMod.setCierraCon(pagMod.getAbreCon() - pagoConLiquidacion.getMonto());
            pagMod.setEsPrimerPago(false);
        }

        try {
            PagoUtil.pagoService.save(pagMod);

            AgenciaModel agencMod = PagoUtil.agenciaService.findById(pagoConLiquidacion.getAgente());
            GerenciaModel gerMod = PagoUtil.gerenciaService.findById(agencMod.getGerenciaId());
            PagoUtil.subSendPayMessage(prestamoModel, pagMod, gerMod);

            retrofit2.Call<ResponseBodyXms> callrespBodyXms = RetrofitOdoo.getInstance().getApi().pagoCreateOne(strSession,
                    new PagoBody(new PagoList(pagMod)));
            RetrofitOdooUtil.sendCall(callrespBodyXms);

            if (liqMod != null) {
                PagoUtil.liquidacionService.save(liqMod);
            }
        } catch (HttpClientErrorException e) {
            modelValidation.setStrResponse(e.toString());
            modelValidation.setHttpStatus(HttpStatus.CONFLICT);
        }
    }

    public static PagoModel pagoModelFromPagoConLiquidacion(PagoConLiquidacion pagoConLiquidacion) {
        PagoModel pagoModel = new PagoModel();

        pagoModel.setPagoId(pagoConLiquidacion.getPagoId());
        pagoModel.setPrestamoId(pagoConLiquidacion.getPrestamoId());
        pagoModel.setPrestamo(pagoConLiquidacion.getPrestamo());
        pagoModel.setMonto(pagoConLiquidacion.getMonto());
        pagoModel.setSemana(pagoConLiquidacion.getSemana());
        pagoModel.setAnio(pagoConLiquidacion.getAnio());
        pagoModel.setEsPrimerPago(pagoConLiquidacion.getEsPrimerPago());
        pagoModel.setAbreCon(pagoConLiquidacion.getAbreCon());
        pagoModel.setCierraCon(pagoConLiquidacion.getCierraCon());
        pagoModel.setTarifa(pagoConLiquidacion.getTarifa());
        pagoModel.setCliente(pagoConLiquidacion.getCliente());
        pagoModel.setAgente(pagoConLiquidacion.getAgente());
        pagoModel.setTipo(pagoConLiquidacion.getTipo());
        pagoModel.setCreadoDesde(pagoConLiquidacion.getCreadoDesde());
        pagoModel.setIdentificador(pagoConLiquidacion.getIdentificador());
        pagoModel.setFechaPago(pagoConLiquidacion.getFechaPago());
        pagoModel.setLat(pagoConLiquidacion.getLat());
        pagoModel.setLng(pagoConLiquidacion.getLng());
        pagoModel.setComentario(pagoConLiquidacion.getComentario());
        pagoModel.setDatosMigracion(pagoConLiquidacion.getDatosMigracion());
        pagoModel.setCreatedAt(pagoConLiquidacion.getCreatedAt());
        pagoModel.setUpdatedAt(pagoConLiquidacion.getUpdatedAt());
        pagoModel.setLog(pagoConLiquidacion.getLog());
        pagoModel.setQuienPago(pagoConLiquidacion.getQuienPago());

        return pagoModel;
    }

    public static JSONObject jsonObjectOfPago(PagoModel pagoModel) {
        JSONObject pagoJsonObject = new JSONObject();

        try {
            pagoJsonObject.put("pagoId", pagoModel.getPagoId());
            pagoJsonObject.put("prestamoId", pagoModel.getPrestamoId());
            pagoJsonObject.put("prestamo", pagoModel.getPrestamo());
            pagoJsonObject.put("monto", pagoModel.getMonto());
            pagoJsonObject.put("semana", pagoModel.getSemana());
            pagoJsonObject.put("anio", pagoModel.getAnio());
            pagoJsonObject.put("esPrimerPago", pagoModel.getEsPrimerPago());
            pagoJsonObject.put("abreCon", pagoModel.getAbreCon());
            pagoJsonObject.put("cierraCon", pagoModel.getCierraCon());
            pagoJsonObject.put("tarifa", pagoModel.getTarifa());
            pagoJsonObject.put("cliente", pagoModel.getCliente());
            pagoJsonObject.put("agente", pagoModel.getAgente());
            pagoJsonObject.put("tipo", pagoModel.getTipo());
            pagoJsonObject.put("creadoDesde", pagoModel.getCreadoDesde());
            pagoJsonObject.put("identificador", pagoModel.getIdentificador());
            pagoJsonObject.put("fechaPago", pagoModel.getFechaPago());
            pagoJsonObject.put("lat", pagoModel.getLat());
            pagoJsonObject.put("lng", pagoModel.getLng());
            pagoJsonObject.put("comentario", pagoModel.getComentario());
            pagoJsonObject.put("datosMigracion", pagoModel.getDatosMigracion());
            pagoJsonObject.put("createdAt", pagoModel.getCreatedAt());
            pagoJsonObject.put("updatedAt", pagoModel.getUpdatedAt());
            pagoJsonObject.put("log", pagoModel.getLog());
            pagoJsonObject.put("quienPago", pagoModel.getQuienPago());
        } catch (
                JSONException e
        ) {
            e.printStackTrace();
        }

        return pagoJsonObject;
    }

    public static JSONObject jsonObjectOfGerencia(GerenciaModel gerenciaModel) {
        JSONObject GerenciaJsonObject = new JSONObject();

        try {
            GerenciaJsonObject.put("gerenciaId", gerenciaModel.getGerenciaId());
            GerenciaJsonObject.put("status", gerenciaModel.getStatus());
            GerenciaJsonObject.put("chatIdPagos", gerenciaModel.getChatIdPagos());
            GerenciaJsonObject.put("chatIdNumeros", gerenciaModel.getChatIdNumeros());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return GerenciaJsonObject;
    }

    public static RequestBody requestBodyPayMessage(PrestamoModel prestamoModel, PagoModel pagoModel,
                                                    GerenciaModel gerenciaModel) {
        JSONObject infoPagoJsonObject = new JSONObject();
        JSONObject pagoJsonObject = jsonObjectOfPago(pagoModel);
        JSONObject gerenciaJsonObject = jsonObjectOfGerencia(gerenciaModel);

        try {
            infoPagoJsonObject.put("nombresCliente", prestamoModel.getNombres());
            infoPagoJsonObject.put("nombresAval", prestamoModel.getNombresAval());
            infoPagoJsonObject.put("montoPago", pagoModel.getMonto());
            infoPagoJsonObject.put("telefonoCliente", prestamoModel.getTelefonoCliente());
            infoPagoJsonObject.put("telefonoAval", prestamoModel.getTelefonoAval());
            infoPagoJsonObject.put("tipoPago", pagoModel.getTipo());
            infoPagoJsonObject.put("pago", pagoJsonObject);
            infoPagoJsonObject.put("gerencia", gerenciaJsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return RequestBody.create(MediaType.parse("application/json"), infoPagoJsonObject.toString());
    }

    public static void subSendPayMessage(PrestamoModel prestamoModel, PagoModel pagoModel,
                                         GerenciaModel gerenciaModel) {
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://fast-n8n.terio.xyz/webhook/91557792-c1f3-442c-88ed-bdee5407d4ce")
                .post(requestBodyPayMessage(prestamoModel, pagoModel, gerenciaModel))
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            // Do something with the response.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean boolIsPagoMigracion(String prestamoId, int anio, int semana) {
        String creadoDesde = "Migracion";
        PagoModel pagoModel = PagoUtil.pagoService.findByPrestamoIdAnioSemanaAndCreadoDesde(prestamoId, anio, semana,
                creadoDesde);

        return pagoModel != null;
    }

    public static PagoModel getPagoEntity(LiquidacionDTO liquidacionDTO) {
        PagoModel pagoModel = PagoUtil.pagoService.getPagoEntity(liquidacionDTO);
        {
            String fechaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            CalendarioModel calendarioModel = PagoUtil.calendarioService.findByFechaActual(fechaActual);
            PrestamoModel prestamoModel = PagoUtil.prestamoService.findById(liquidacionDTO.getPrestamoId());

            pagoModel.setPagoId(UUID.randomUUID().toString());
            pagoModel.setSemana(calendarioModel.getSemana());
            pagoModel.setAnio(calendarioModel.getAnio());
            pagoModel.setEsPrimerPago(false);

            if (prestamoModel != null) {
                pagoModel.setTarifa(prestamoModel.getTarifa());
                pagoModel.setAgente(prestamoModel.getAgente());
            }

            pagoModel.setTipo("Liquidacion");
            pagoModel.setCreadoDesde("PGS");
            pagoModel.setCierraCon(0.0);
            pagoModel.setFechaPago(fechaActual);
        }
        return pagoModel;
    }
}
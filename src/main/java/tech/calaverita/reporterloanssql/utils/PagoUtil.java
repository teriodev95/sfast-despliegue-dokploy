package tech.calaverita.reporterloanssql.utils;

import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import tech.calaverita.reporterloanssql.dto.LiquidacionDTO;
import tech.calaverita.reporterloanssql.models.mariaDB.*;
import tech.calaverita.reporterloanssql.models.view.PrestamoModel;
import tech.calaverita.reporterloanssql.pojos.ModelValidation;
import tech.calaverita.reporterloanssql.pojos.PagoConLiquidacion;
import tech.calaverita.reporterloanssql.retrofit.RetrofitOdoo;
import tech.calaverita.reporterloanssql.retrofit.pojos.LiquidacionBody;
import tech.calaverita.reporterloanssql.retrofit.pojos.PagoBody;
import tech.calaverita.reporterloanssql.retrofit.pojos.PagoList;
import tech.calaverita.reporterloanssql.retrofit.pojos.ResponseBodyXms;
import tech.calaverita.reporterloanssql.services.*;
import tech.calaverita.reporterloanssql.services.view.PrestamoService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Component
public final class PagoUtil {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private static AgenciaService agencServ;
    private static GerenciaService gerServ;
    private static LiquidacionService liqServ;
    private static PagoService pagServ;
    private static CalendarioService calendarioService;
    private static PrestamoService prestamoService;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    private PagoUtil(
            AgenciaService agencServ_S,
            GerenciaService gerServ_S,
            LiquidacionService liqServ_S,
            PagoService pagServ_S,
            CalendarioService calendarioService,
            PrestamoService prestamoService
    ) {
        PagoUtil.agencServ = agencServ_S;
        PagoUtil.gerServ = gerServ_S;
        PagoUtil.liqServ = liqServ_S;
        PagoUtil.pagServ = pagServ_S;
        PagoUtil.calendarioService = calendarioService;
        PagoUtil.prestamoService = prestamoService;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    public static ModelValidation modValPagoModelValidation(
            PagoConLiquidacion pagConLiq_I,
            PrestamoModel prestMod_M
    ) {
        String strResponse_O = "";
        HttpStatus httpStatus_O = HttpStatus.CREATED;
        boolean boolIsOnline_O = true;

        if (
                pagConLiq_I.getPagoId() == null
        ) {
            strResponse_O += "Debe Ingresar El 'pagoId'";
            httpStatus_O = HttpStatus.BAD_REQUEST;
            boolIsOnline_O = false;
        }

        Optional<PagoModel> optpagMod = PagoUtil.pagServ.optpagModFindById(pagConLiq_I.getPagoId());

        if (
                ((pagConLiq_I.getPrestamoId() == null) || pagConLiq_I.getPrestamoId()
                        .equalsIgnoreCase(""))
        ) {
            strResponse_O += "\nDebe Ingresar El 'prestamoId'";
            httpStatus_O = HttpStatus.BAD_REQUEST;
            boolIsOnline_O = false;
        }

        if (
                !optpagMod.isEmpty()
        ) {
            strResponse_O += "\nEl Pago Ya Existe";
            httpStatus_O = HttpStatus.CONFLICT;
            boolIsOnline_O = false;
        }

        if (
                PagoUtil.boolIsPagoMigracion(pagConLiq_I.getPrestamoId(), pagConLiq_I.getAnio(),
                        pagConLiq_I.getSemana())
        ) {
            strResponse_O += "\nEl Pago cuenta con migración";
            httpStatus_O = HttpStatus.CONFLICT;
            boolIsOnline_O = false;
        }

        if (
                prestMod_M == null
        ) {
            strResponse_O += "\nNo Se Encontró Ningún Prestamo Con Ese 'prestamoId'";
            httpStatus_O = HttpStatus.NOT_FOUND;
            boolIsOnline_O = false;
        }

        return new ModelValidation(strResponse_O, httpStatus_O, boolIsOnline_O);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public static void subProcessPayment(
            ModelValidation modVal_M,
            PagoConLiquidacion pagConLiq_I,
            PrestamoModel prestMod_I
    ) {
        String strSession = "session_id=76d814874514726176f0615260848da2aab725ea";
        PagoModel pagMod = PagoUtil.pagoModelFromPagoConLiquidacion(pagConLiq_I);
        LiquidacionModel liqMod = pagConLiq_I.getInfoLiquidacion();

        modVal_M.setStrResponse("Pago Insertado con Éxito");
        modVal_M.setHttpStatus(HttpStatus.CREATED);

        //                                                  //A continuación se hace una validación para comprobar si el
        //                                                  //      pago recibido trae liquidación
        if (
                liqMod != null
        ) {
            liqMod.setPagoId(pagMod.getPagoId());
            pagMod.setAbreCon(prestMod_I.getTotalAPagar() - PagoUtil.pagServ
                    .numGetSaldoFromPrestamoByPrestamoId(pagConLiq_I.getPrestamoId()));
            pagMod.setCierraCon(0.0);
            pagMod.setEsPrimerPago(false);

            retrofit2.Call<ResponseBodyXms> callrespBodyXms = RetrofitOdoo.getInstance().getApi()
                    .liquidacionCreateOne(strSession, new LiquidacionBody(liqMod));
            RetrofitOdooUtil.sendCall(callrespBodyXms);
        } //
        else {
            pagMod.setAbreCon(prestMod_I.getTotalAPagar() - PagoUtil.pagServ.numGetSaldoFromPrestamoByPrestamoId(
                    pagConLiq_I.getPrestamoId()));
            pagMod.setCierraCon(pagMod.getAbreCon() - pagConLiq_I.getMonto());
            pagMod.setEsPrimerPago(false);
        }

        try {
            PagoUtil.pagServ.save(pagMod);

            AgenciaModel agencMod = PagoUtil.agencServ.agencModFindByAgenciaId(pagConLiq_I.getAgente());
            GerenciaModel gerMod = PagoUtil.gerServ.gerModFindByGerenciaId(agencMod.getGerenciaId());
            PagoUtil.subSendPayMessage(prestMod_I, pagMod, gerMod);

            retrofit2.Call<ResponseBodyXms> callrespBodyXms = RetrofitOdoo.getInstance().getApi().pagoCreateOne(strSession,
                    new PagoBody(new PagoList(pagMod)));
            RetrofitOdooUtil.sendCall(callrespBodyXms);

            if (
                    liqMod != null
            ) {
                PagoUtil.liqServ.save(liqMod);
            }
        } //
        catch (HttpClientErrorException e) {
            modVal_M.setStrResponse(e.toString());
            modVal_M.setHttpStatus(HttpStatus.CONFLICT);
        }
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public static PagoModel pagoModelFromPagoConLiquidacion(
            PagoConLiquidacion pagoConLiquidacion_I
    ) {
        PagoModel pagoModel_O = new PagoModel();

        pagoModel_O.setPagoId(pagoConLiquidacion_I.getPagoId());
        pagoModel_O.setPrestamoId(pagoConLiquidacion_I.getPrestamoId());
        pagoModel_O.setPrestamo(pagoConLiquidacion_I.getPrestamo());
        pagoModel_O.setMonto(pagoConLiquidacion_I.getMonto());
        pagoModel_O.setSemana(pagoConLiquidacion_I.getSemana());
        pagoModel_O.setAnio(pagoConLiquidacion_I.getAnio());
        pagoModel_O.setEsPrimerPago(pagoConLiquidacion_I.getEsPrimerPago());
        pagoModel_O.setAbreCon(pagoConLiquidacion_I.getAbreCon());
        pagoModel_O.setCierraCon(pagoConLiquidacion_I.getCierraCon());
        pagoModel_O.setTarifa(pagoConLiquidacion_I.getTarifa());
        pagoModel_O.setCliente(pagoConLiquidacion_I.getCliente());
        pagoModel_O.setAgente(pagoConLiquidacion_I.getAgente());
        pagoModel_O.setTipo(pagoConLiquidacion_I.getTipo());
        pagoModel_O.setCreadoDesde(pagoConLiquidacion_I.getCreadoDesde());
        pagoModel_O.setIdentificador(pagoConLiquidacion_I.getIdentificador());
        pagoModel_O.setFechaPago(pagoConLiquidacion_I.getFechaPago());
        pagoModel_O.setLat(pagoConLiquidacion_I.getLat());
        pagoModel_O.setLng(pagoConLiquidacion_I.getLng());
        pagoModel_O.setComentario(pagoConLiquidacion_I.getComentario());
        pagoModel_O.setDatosMigracion(pagoConLiquidacion_I.getDatosMigracion());
        pagoModel_O.setCreatedAt(pagoConLiquidacion_I.getCreatedAt());
        pagoModel_O.setUpdatedAt(pagoConLiquidacion_I.getUpdatedAt());
        pagoModel_O.setLog(pagoConLiquidacion_I.getLog());
        pagoModel_O.setQuienPago(pagoConLiquidacion_I.getQuienPago());

        return pagoModel_O;
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public static JSONObject jsonObjectOfPago(
            PagoModel pagoModel_I
    ) {
        JSONObject jsonPago_O = new JSONObject();

        try {
            jsonPago_O.put("pagoId", pagoModel_I.getPagoId());
            jsonPago_O.put("prestamoId", pagoModel_I.getPrestamoId());
            jsonPago_O.put("prestamo", pagoModel_I.getPrestamo());
            jsonPago_O.put("monto", pagoModel_I.getMonto());
            jsonPago_O.put("semana", pagoModel_I.getSemana());
            jsonPago_O.put("anio", pagoModel_I.getAnio());
            jsonPago_O.put("esPrimerPago", pagoModel_I.getEsPrimerPago());
            jsonPago_O.put("abreCon", pagoModel_I.getAbreCon());
            jsonPago_O.put("cierraCon", pagoModel_I.getCierraCon());
            jsonPago_O.put("tarifa", pagoModel_I.getTarifa());
            jsonPago_O.put("cliente", pagoModel_I.getCliente());
            jsonPago_O.put("agente", pagoModel_I.getAgente());
            jsonPago_O.put("tipo", pagoModel_I.getTipo());
            jsonPago_O.put("creadoDesde", pagoModel_I.getCreadoDesde());
            jsonPago_O.put("identificador", pagoModel_I.getIdentificador());
            jsonPago_O.put("fechaPago", pagoModel_I.getFechaPago());
            jsonPago_O.put("lat", pagoModel_I.getLat());
            jsonPago_O.put("lng", pagoModel_I.getLng());
            jsonPago_O.put("comentario", pagoModel_I.getComentario());
            jsonPago_O.put("datosMigracion", pagoModel_I.getDatosMigracion());
            jsonPago_O.put("createdAt", pagoModel_I.getCreatedAt());
            jsonPago_O.put("updatedAt", pagoModel_I.getUpdatedAt());
            jsonPago_O.put("log", pagoModel_I.getLog());
            jsonPago_O.put("quienPago", pagoModel_I.getQuienPago());
        } catch (
                JSONException e
        ) {
            e.printStackTrace();
        }

        return jsonPago_O;
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public static JSONObject jsonObjectOfGerencia(
            GerenciaModel gerenciaModel_I
    ) {
        JSONObject jsonGerencia_O = new JSONObject();

        try {
            jsonGerencia_O.put("gerenciaId", gerenciaModel_I.getGerenciaId());
            jsonGerencia_O.put("status", gerenciaModel_I.getStatus());
            jsonGerencia_O.put("chatIdPagos", gerenciaModel_I.getChatIdPagos());
            jsonGerencia_O.put("chatIdNumeros", gerenciaModel_I.getChatIdNumeros());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonGerencia_O;
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public static RequestBody requestBodyPayMessage(
            PrestamoModel prestamoModel_I,
            PagoModel pagoModel_I,
            GerenciaModel gerenciaModel_I
    ) {
        JSONObject infoPago = new JSONObject();
        JSONObject jsonPago = jsonObjectOfPago(pagoModel_I);
        JSONObject jsonGerencia = jsonObjectOfGerencia(gerenciaModel_I);

        try {
            infoPago.put("nombresCliente", prestamoModel_I.getNombres());
            infoPago.put("nombresAval", prestamoModel_I.getNombresAval());
            infoPago.put("montoPago", pagoModel_I.getMonto());
            infoPago.put("telefonoCliente", prestamoModel_I.getTelefonoCliente());
            infoPago.put("telefonoAval", prestamoModel_I.getTelefonoAval());
            infoPago.put("tipoPago", pagoModel_I.getTipo());
            infoPago.put("pago", jsonPago);
            infoPago.put("gerencia", jsonGerencia);
        } catch (
                JSONException e
        ) {
            e.printStackTrace();
        }

        return RequestBody.create(MediaType.parse("application/json"), infoPago.toString());
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public static void subSendPayMessage(
            PrestamoModel prestamoModel_I,
            PagoModel pagoModel_I,
            GerenciaModel gerenciaModel_I
    ) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://fast-n8n.terio.xyz/webhook/91557792-c1f3-442c-88ed-bdee5407d4ce")
//                .url("https://yfspvm3deal7jebp1s2andy5.hooks.n8n.cloud/webhook-test/93866ec9-652f-4c92-8314-e3f05a4ce6f7")
                .post(requestBodyPayMessage(prestamoModel_I, pagoModel_I, gerenciaModel_I))
                .build();

        try {
            Response response = client.newCall(request).execute();
            // Do something with the response.
        } catch (
                IOException e
        ) {
            e.printStackTrace();
        }
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public static boolean boolIsPagoMigracion(
            String prestamoId,
            int anio,
            int semana
    ) {
        PagoModel pagoModel = PagoUtil.pagServ.pagModFindByAgenteAnioAndSemana(prestamoId, anio, semana);

        return (
                pagoModel != null
        );
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public static PagoModel getPagoEntity(
            LiquidacionDTO liquidacionDTO
    ) {
        PagoModel entity = PagoUtil.pagServ.getPagoEntity(liquidacionDTO);
        {
            String fechaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            CalendarioModel calendarioModel = PagoUtil.calendarioService.findByFechaActual(fechaActual);
            Optional<PrestamoModel> prestamoEntity = PagoUtil.prestamoService.findById(liquidacionDTO.getPrestamoId());

            entity.setPagoId(UUID.randomUUID().toString());
            entity.setSemana(calendarioModel.getSemana());
            entity.setAnio(calendarioModel.getAnio());
            entity.setEsPrimerPago(false);

            if (
                    prestamoEntity.isPresent()
            ) {
                entity.setTarifa(prestamoEntity.get().getTarifa());
                entity.setAgente(prestamoEntity.get().getAgente());
            }

            entity.setTipo("Liquidacion");
            entity.setCreadoDesde("PGS");
            entity.setCierraCon(0.0);
            entity.setFechaPago(fechaActual);
        }
        return entity;
    }
}
package tech.calaverita.reporterloanssql.utils;

import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import tech.calaverita.reporterloanssql.persistence.dto.LiquidacionDTO;
import tech.calaverita.reporterloanssql.persistence.entities.*;
import tech.calaverita.reporterloanssql.persistence.entities.view.PrestamoEntity;
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
            PrestamoEntity prestMod_M
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

        Optional<PagoEntity> optpagMod = PagoUtil.pagServ.optpagModFindById(pagConLiq_I.getPagoId());

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
            PrestamoEntity prestMod_I
    ) {
        String strSession = "session_id=76d814874514726176f0615260848da2aab725ea";
        PagoEntity pagMod = PagoUtil.pagoModelFromPagoConLiquidacion(pagConLiq_I);
        LiquidacionEntity liqMod = pagConLiq_I.getInfoLiquidacion();

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

            AgenciaEntity agencMod = PagoUtil.agencServ.agencModFindByAgenciaId(pagConLiq_I.getAgente());
            GerenciaEntity gerMod = PagoUtil.gerServ.gerModFindByGerenciaId(agencMod.getGerenciaId());
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
    public static PagoEntity pagoModelFromPagoConLiquidacion(
            PagoConLiquidacion pagoConLiquidacion_I
    ) {
        PagoEntity pagoEntity_O = new PagoEntity();

        pagoEntity_O.setPagoId(pagoConLiquidacion_I.getPagoId());
        pagoEntity_O.setPrestamoId(pagoConLiquidacion_I.getPrestamoId());
        pagoEntity_O.setPrestamo(pagoConLiquidacion_I.getPrestamo());
        pagoEntity_O.setMonto(pagoConLiquidacion_I.getMonto());
        pagoEntity_O.setSemana(pagoConLiquidacion_I.getSemana());
        pagoEntity_O.setAnio(pagoConLiquidacion_I.getAnio());
        pagoEntity_O.setEsPrimerPago(pagoConLiquidacion_I.getEsPrimerPago());
        pagoEntity_O.setAbreCon(pagoConLiquidacion_I.getAbreCon());
        pagoEntity_O.setCierraCon(pagoConLiquidacion_I.getCierraCon());
        pagoEntity_O.setTarifa(pagoConLiquidacion_I.getTarifa());
        pagoEntity_O.setCliente(pagoConLiquidacion_I.getCliente());
        pagoEntity_O.setAgente(pagoConLiquidacion_I.getAgente());
        pagoEntity_O.setTipo(pagoConLiquidacion_I.getTipo());
        pagoEntity_O.setCreadoDesde(pagoConLiquidacion_I.getCreadoDesde());
        pagoEntity_O.setIdentificador(pagoConLiquidacion_I.getIdentificador());
        pagoEntity_O.setFechaPago(pagoConLiquidacion_I.getFechaPago());
        pagoEntity_O.setLat(pagoConLiquidacion_I.getLat());
        pagoEntity_O.setLng(pagoConLiquidacion_I.getLng());
        pagoEntity_O.setComentario(pagoConLiquidacion_I.getComentario());
        pagoEntity_O.setDatosMigracion(pagoConLiquidacion_I.getDatosMigracion());
        pagoEntity_O.setCreatedAt(pagoConLiquidacion_I.getCreatedAt());
        pagoEntity_O.setUpdatedAt(pagoConLiquidacion_I.getUpdatedAt());
        pagoEntity_O.setLog(pagoConLiquidacion_I.getLog());
        pagoEntity_O.setQuienPago(pagoConLiquidacion_I.getQuienPago());

        return pagoEntity_O;
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public static JSONObject jsonObjectOfPago(
            PagoEntity pagoEntity_I
    ) {
        JSONObject jsonPago_O = new JSONObject();

        try {
            jsonPago_O.put("pagoId", pagoEntity_I.getPagoId());
            jsonPago_O.put("prestamoId", pagoEntity_I.getPrestamoId());
            jsonPago_O.put("prestamo", pagoEntity_I.getPrestamo());
            jsonPago_O.put("monto", pagoEntity_I.getMonto());
            jsonPago_O.put("semana", pagoEntity_I.getSemana());
            jsonPago_O.put("anio", pagoEntity_I.getAnio());
            jsonPago_O.put("esPrimerPago", pagoEntity_I.getEsPrimerPago());
            jsonPago_O.put("abreCon", pagoEntity_I.getAbreCon());
            jsonPago_O.put("cierraCon", pagoEntity_I.getCierraCon());
            jsonPago_O.put("tarifa", pagoEntity_I.getTarifa());
            jsonPago_O.put("cliente", pagoEntity_I.getCliente());
            jsonPago_O.put("agente", pagoEntity_I.getAgente());
            jsonPago_O.put("tipo", pagoEntity_I.getTipo());
            jsonPago_O.put("creadoDesde", pagoEntity_I.getCreadoDesde());
            jsonPago_O.put("identificador", pagoEntity_I.getIdentificador());
            jsonPago_O.put("fechaPago", pagoEntity_I.getFechaPago());
            jsonPago_O.put("lat", pagoEntity_I.getLat());
            jsonPago_O.put("lng", pagoEntity_I.getLng());
            jsonPago_O.put("comentario", pagoEntity_I.getComentario());
            jsonPago_O.put("datosMigracion", pagoEntity_I.getDatosMigracion());
            jsonPago_O.put("createdAt", pagoEntity_I.getCreatedAt());
            jsonPago_O.put("updatedAt", pagoEntity_I.getUpdatedAt());
            jsonPago_O.put("log", pagoEntity_I.getLog());
            jsonPago_O.put("quienPago", pagoEntity_I.getQuienPago());
        } catch (
                JSONException e
        ) {
            e.printStackTrace();
        }

        return jsonPago_O;
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public static JSONObject jsonObjectOfGerencia(
            GerenciaEntity gerenciaEntity_I
    ) {
        JSONObject jsonGerencia_O = new JSONObject();

        try {
            jsonGerencia_O.put("gerenciaId", gerenciaEntity_I.getGerenciaId());
            jsonGerencia_O.put("status", gerenciaEntity_I.getStatus());
            jsonGerencia_O.put("chatIdPagos", gerenciaEntity_I.getChatIdPagos());
            jsonGerencia_O.put("chatIdNumeros", gerenciaEntity_I.getChatIdNumeros());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonGerencia_O;
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public static RequestBody requestBodyPayMessage(
            PrestamoEntity prestamoEntity_I,
            PagoEntity pagoEntity_I,
            GerenciaEntity gerenciaEntity_I
    ) {
        JSONObject infoPago = new JSONObject();
        JSONObject jsonPago = jsonObjectOfPago(pagoEntity_I);
        JSONObject jsonGerencia = jsonObjectOfGerencia(gerenciaEntity_I);

        try {
            infoPago.put("nombresCliente", prestamoEntity_I.getNombres());
            infoPago.put("nombresAval", prestamoEntity_I.getNombresAval());
            infoPago.put("montoPago", pagoEntity_I.getMonto());
            infoPago.put("telefonoCliente", prestamoEntity_I.getTelefonoCliente());
            infoPago.put("telefonoAval", prestamoEntity_I.getTelefonoAval());
            infoPago.put("tipoPago", pagoEntity_I.getTipo());
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
            PrestamoEntity prestamoEntity_I,
            PagoEntity pagoEntity_I,
            GerenciaEntity gerenciaEntity_I
    ) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://fast-n8n.terio.xyz/webhook/91557792-c1f3-442c-88ed-bdee5407d4ce")
//                .url("https://yfspvm3deal7jebp1s2andy5.hooks.n8n.cloud/webhook-test/93866ec9-652f-4c92-8314-e3f05a4ce6f7")
                .post(requestBodyPayMessage(prestamoEntity_I, pagoEntity_I, gerenciaEntity_I))
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
        PagoEntity pagoEntity = PagoUtil.pagServ.pagModFindByAgenteAnioAndSemana(prestamoId, anio, semana);

        return (
                pagoEntity != null
        );
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public static PagoEntity getPagoEntity(
            LiquidacionDTO liquidacionDTO
    ) {
        PagoEntity entity = PagoUtil.pagServ.getPagoEntity(liquidacionDTO);
        {
            String fechaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            CalendarioEntity calendarioEntity = PagoUtil.calendarioService.findByFechaActual(fechaActual);
            Optional<PrestamoEntity> prestamoEntity = PagoUtil.prestamoService.findById(liquidacionDTO.getPrestamoId());

            entity.setPagoId(UUID.randomUUID().toString());
            entity.setSemana(calendarioEntity.getSemana());
            entity.setAnio(calendarioEntity.getAnio());
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
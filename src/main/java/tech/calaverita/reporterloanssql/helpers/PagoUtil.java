package tech.calaverita.reporterloanssql.helpers;

import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.calaverita.reporterloanssql.models.GerenciaModel;
import tech.calaverita.reporterloanssql.models.PagoModel;
import tech.calaverita.reporterloanssql.models.PrestamoModel;
import tech.calaverita.reporterloanssql.pojos.PagoConLiquidacion;
import tech.calaverita.reporterloanssql.services.PagoService;

import java.io.IOException;

@Component
public class PagoUtil {
    private static PagoService pagoService;

    @Autowired
    private PagoUtil(PagoService pagoService) {
        PagoUtil.pagoService = pagoService;
    }

    public static PagoModel getPagoModelFromPagoConLiquidacion(PagoConLiquidacion pagoConLiquidacion) {
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

    public static JSONObject getJsonObjectOfPago(PagoModel pago) {
        JSONObject jsonPago = new JSONObject();

        try {
            jsonPago.put("pagoId", pago.getPagoId());
            jsonPago.put("prestamoId", pago.getPrestamoId());
            jsonPago.put("prestamo", pago.getPrestamo());
            jsonPago.put("monto", pago.getMonto());
            jsonPago.put("semana", pago.getSemana());
            jsonPago.put("anio", pago.getAnio());
            jsonPago.put("esPrimerPago", pago.getEsPrimerPago());
            jsonPago.put("abreCon", pago.getAbreCon());
            jsonPago.put("cierraCon", pago.getCierraCon());
            jsonPago.put("tarifa", pago.getTarifa());
            jsonPago.put("cliente", pago.getCliente());
            jsonPago.put("agente", pago.getAgente());
            jsonPago.put("tipo", pago.getTipo());
            jsonPago.put("creadoDesde", pago.getCreadoDesde());
            jsonPago.put("identificador", pago.getIdentificador());
            jsonPago.put("fechaPago", pago.getFechaPago());
            jsonPago.put("lat", pago.getLat());
            jsonPago.put("lng", pago.getLng());
            jsonPago.put("comentario", pago.getComentario());
            jsonPago.put("datosMigracion", pago.getDatosMigracion());
            jsonPago.put("createdAt", pago.getCreatedAt());
            jsonPago.put("updatedAt", pago.getUpdatedAt());
            jsonPago.put("log", pago.getLog());
            jsonPago.put("quienPago", pago.getQuienPago());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonPago;
    }

    public static JSONObject getJsonObjectOfGerencia(GerenciaModel gerenciaModel) {
        JSONObject jsonGerencia = new JSONObject();

        try {
            jsonGerencia.put("gerenciaId", gerenciaModel.getGerenciaId());
            jsonGerencia.put("status", gerenciaModel.getStatus());
            jsonGerencia.put("chatIdPagos", gerenciaModel.getChatIdPagos());
            jsonGerencia.put("chatIdNumeros", gerenciaModel.getChatIdNumeros());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonGerencia;
    }

    public static RequestBody getPayMessage(PrestamoModel prestamo, PagoModel pago, GerenciaModel gerencia) {
        JSONObject infoPago = new JSONObject();
        JSONObject jsonPago = getJsonObjectOfPago(pago);
        JSONObject jsonGerencia = getJsonObjectOfGerencia(gerencia);

        try {
            infoPago.put("nombresCliente", prestamo.getNombres());
            infoPago.put("nombresAval", prestamo.getNombresAval());
            infoPago.put("montoPago", pago.getMonto());
            infoPago.put("telefonoCliente", prestamo.getTelefonoCliente());
            infoPago.put("telefonoAval", prestamo.getTelefonoAval());
            infoPago.put("tipoPago", pago.getTipo());
            infoPago.put("pago", jsonPago);
            infoPago.put("gerencia", jsonGerencia);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), infoPago.toString());

        return requestBody;
    }

    public static void sendPayMessage(PrestamoModel prestamo, PagoModel pago, GerenciaModel gerencia) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://fast-n8n.terio.xyz/webhook/91557792-c1f3-442c-88ed-bdee5407d4ce")
//                .url("https://yfspvm3deal7jebp1s2andy5.hooks.n8n.cloud/webhook-test/93866ec9-652f-4c92-8314-e3f05a4ce6f7")
                .post(getPayMessage(prestamo, pago, gerencia))
                .build();

        try {
            Response response = client.newCall(request).execute();
            // Do something with the response.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isPagoMigracion(String prestamoId, int anio, int semana) {
        PagoModel pagoModel = PagoService.getPagoMigracionByAgenteAnioAndSemana(prestamoId, anio, semana);

        return pagoModel != null;
    }
}
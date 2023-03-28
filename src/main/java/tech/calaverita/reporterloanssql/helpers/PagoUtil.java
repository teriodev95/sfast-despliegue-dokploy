package tech.calaverita.reporterloanssql.helpers;

import okhttp3.*;
import retrofit2.Call;
import org.json.JSONException;
import tech.calaverita.reporterloanssql.models.PagoModel;
import tech.calaverita.reporterloanssql.models.PrestamoModel;
import tech.calaverita.reporterloanssql.pojos.odoo.OdooRequest;
import tech.calaverita.reporterloanssql.pojos.odoo.OdooResponse;
import tech.calaverita.reporterloanssql.pojos.PagoConLiquidacion;

import java.io.IOException;
import org.json.JSONObject;
import tech.calaverita.reporterloanssql.retrofit.RetrofitOdoo;

public class PagoUtil {
    public static PagoModel getPagoModelFromPagoConLiquidacion(PagoConLiquidacion pagoConLiquidacion){
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

        return pagoModel;
    }

    public static JSONObject getJsonObjectOfPago(PagoModel pago){
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
        }catch (JSONException e){
            e.printStackTrace();
        }

        return jsonPago;
    }

    public static RequestBody getPayMessage(PrestamoModel prestamo, PagoModel pago){
        JSONObject infoPago = new JSONObject();
        JSONObject jsonPago = getJsonObjectOfPago(pago);

        try {
            infoPago.put("nombresCliente", prestamo.getNombres());
            infoPago.put("nombresAval", prestamo.getNombresAval());
            infoPago.put("montoPago", pago.getMonto());
            infoPago.put("telefonoCliente", prestamo.getTelefonoCliente());
            infoPago.put("telefonoAval", prestamo.getTelefonoAval());
            infoPago.put("tipoPago", pago.getTipo());
            infoPago.put("pago", jsonPago);
        }catch (JSONException e){
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), infoPago.toString());

        return requestBody;
    }

    public static void sendPayMessage(PrestamoModel prestamo, PagoModel pago){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://n8n.terio.xyz/webhook/91557792-c1f3-442c-88ed-bdee5407d4ce")
//                .url("https://heavy-cow-23.hooks.n8n.cloud/webhook-test/93866ec9-652f-4c92-8314-e3f05a4ce6f7")
                .post(getPayMessage(prestamo, pago))
                .build();

        try {
            Response response = client.newCall(request).execute();
            // Do something with the response.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
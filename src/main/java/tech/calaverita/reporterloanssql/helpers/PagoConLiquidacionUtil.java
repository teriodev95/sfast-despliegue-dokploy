package tech.calaverita.reporterloanssql.helpers;

import com.google.gson.Gson;
import okhttp3.*;
import tech.calaverita.reporterloanssql.models.PagoModel;
import tech.calaverita.reporterloanssql.models.PrestamoModel;
import tech.calaverita.reporterloanssql.pojos.PagoConLiquidacion;

import java.io.IOException;
import java.util.HashMap;

public class PagoConLiquidacionUtil {
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

    public static String getPayMessage(PrestamoModel prestamo, PagoModel pago){
        HashMap<String, Object> infoPago = new HashMap<>();

        infoPago.put("nombresCliente", prestamo.getNombres());
        infoPago.put("nombresAval", prestamo.getNombresAval());
        infoPago.put("montoPago", pago.getMonto());
        infoPago.put("telefonoCliente", prestamo.getTelefonoCliente());
        infoPago.put("telefonoAval", prestamo.getTelefonoAval());
        infoPago.put("tipoPago", pago.getTipo());
        infoPago.put("pago", pago);

        return new Gson().toJson(infoPago);
    }

    public static void sendPayMessage(String payMessage){
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("message", payMessage)
                .build();
        Request request = new Request.Builder()
                .url("https://n8n.terio.xyz/webhook/91557792-c1f3-442c-88ed-bdee5407d4ce")
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            // Do something with the response.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
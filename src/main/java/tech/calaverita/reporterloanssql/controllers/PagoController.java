package tech.calaverita.reporterloanssql.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import retrofit2.Call;
import tech.calaverita.reporterloanssql.helpers.PagoUtil;
import tech.calaverita.reporterloanssql.models.*;
import tech.calaverita.reporterloanssql.pojos.PagoConLiquidacion;
import tech.calaverita.reporterloanssql.repositories.*;
import tech.calaverita.reporterloanssql.retrofit.RetrofitOdoo;
import tech.calaverita.reporterloanssql.retrofit.pojos.LiquidacionBody;
import tech.calaverita.reporterloanssql.retrofit.pojos.PagoBody;
import tech.calaverita.reporterloanssql.retrofit.pojos.PagoList;
import tech.calaverita.reporterloanssql.retrofit.pojos.ResponseBodyXms;
import tech.calaverita.reporterloanssql.services.RetrofitOdooService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping(path = "/xpress/v1/pays")
public class PagoController {
    @Autowired
    PagoRepository pagoRepository;
    @Autowired
    PrestamoRepository prestamoRepository;
    @Autowired
    LiquidacionRepository liquidacionRepository;
    @Autowired
    RetrofitOdooService retrofitOdooService;
    @Autowired
    GerenciaRepository gerenciaRepository;
    @Autowired
    AgenciaRepository agenciaRepository;

    @GetMapping(path = "/{agencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<ArrayList<PagoModel>> getPagosByAgencia(@PathVariable("agencia") String agencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana) {
        ArrayList<PagoModel> pagos = pagoRepository.findPagoModelsByAgenciaAnioAndSemana(agencia, anio, semana);

        if (pagos.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(pagos, HttpStatus.OK);
    }

    /*
        Dentro de este endpoint se puede recibir el pago con la liquidación,
        si el pago mandado cuenta con liquidacion se hará el proceso correspondiente
        para guardar dicha liquidacion y ajustar la propiedad cierraCon de pago,
        si se manda solo el pago solo se realizará el proceso para guardar el pago.
     */
    @PostMapping(path = "/create-one")
    public @ResponseBody ResponseEntity<String> createPago(@RequestBody PagoConLiquidacion pago) {
        String session = "session_id=76d814874514726176f0615260848da2aab725ea";

        if (pago.getPagoId() == null)
            return new ResponseEntity<>("Debe Ingresar El 'pagoId'", HttpStatus.BAD_REQUEST);

        Optional<PagoModel> pagoAux = pagoRepository.findById(pago.getPagoId());

        if (pago.getPrestamoId() == null || pago.getPrestamoId().equalsIgnoreCase(""))
            return new ResponseEntity<>("Debe Ingresar El 'prestamoId'", HttpStatus.BAD_REQUEST);

        if (!pagoAux.isEmpty())
            return new ResponseEntity<>("El Pago Ya Existe", HttpStatus.CONFLICT);

        if (PagoUtil.isPagoMigracion(pago.getPrestamoId(), pago.getAnio(), pago.getSemana())) {
            return new ResponseEntity<>("El Pago cuenta con migración", HttpStatus.CONFLICT);
        }

        PrestamoModel prestamo = prestamoRepository.getPrestamoByPrestamoId(pago.getPrestamoId());

        if (prestamo == null)
            return new ResponseEntity<>("No Se Encontró Ningún Prestamo Con Ese 'prestamoId'", HttpStatus.NOT_FOUND);

        PagoModel pagoModel = PagoUtil.getPagoModelFromPagoConLiquidacion(pago);
        LiquidacionModel liquidacionModel = pago.getInfoLiquidacion();

        /*
            A continuación se hace una validación para comprobar si el pago mandado lleva liquidación,
            de ser así se hará el proceso dentro del if, sino se hará el proceso dentro del else.
         */
        if (liquidacionModel != null) {
            liquidacionModel.setPagoId(pagoModel.getPagoId());
            pagoModel.setAbreCon(prestamo.getTotalAPagar() - pagoRepository.getSaldoFromPrestamoByPrestamoId(pago.getPrestamoId()));
            pagoModel.setCierraCon(0.0);
            pagoModel.setEsPrimerPago(false);

            Call<ResponseBodyXms> call = RetrofitOdoo.getInstance().getApi().liquidacionCreateOne(session, new LiquidacionBody(liquidacionModel));
            retrofitOdooService.sendCall(call);
        } else {
            pagoModel.setAbreCon(prestamo.getTotalAPagar() - pagoRepository.getSaldoFromPrestamoByPrestamoId(pago.getPrestamoId()));
            pagoModel.setCierraCon(pagoModel.getAbreCon() - pago.getMonto());
            pagoModel.setEsPrimerPago(false);
        }

        try {
            pagoRepository.save(pagoModel);

            AgenciaModel agencia = agenciaRepository.getAgenciaModelByAgenciaId(pago.getAgente());
            GerenciaModel gerencia = gerenciaRepository.findOneByGerenciaId(agencia.getGerenciaId());
            PagoUtil.sendPayMessage(prestamo, pagoModel, gerencia);

            Call<ResponseBodyXms> call = RetrofitOdoo.getInstance().getApi().pagoCreateOne(session, new PagoBody(new PagoList(pagoModel)));
            retrofitOdooService.sendCall(call);

            if (liquidacionModel != null) {
                liquidacionRepository.save(liquidacionModel);
            }
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>("Pago Insertado con Éxito", HttpStatus.CREATED);
    }

    @PostMapping(path = "/create-many")
    public @ResponseBody ResponseEntity<ArrayList<HashMap<String, Object>>> createPagos(@RequestBody ArrayList<PagoConLiquidacion> pagos) {
        String session = "session_id=76d814874514726176f0615260848da2aab725ea";

        ArrayList<HashMap<String, Object>> respuesta = new ArrayList<>();

        for (PagoConLiquidacion pago : pagos) {
            HashMap<String, Object> objeto = new HashMap<>();
            String msg = "OK";
            String msgAux = "";
            Boolean isOnline = true;

            if (pago.getPagoId() == null) {
                msgAux += "Debe Ingresar El 'pagoId'|";
                isOnline = false;
            }

            Optional<PagoModel> pagoAux = pagoRepository.findById(pago.getPagoId());

            if (pago.getPrestamoId() == null || pago.getPrestamoId().equalsIgnoreCase("")) {
                msgAux += "Debe Ingresar El 'prestamoId'|";
                isOnline = false;
            }

            if (!pagoAux.isEmpty()) {
                msgAux += "El Pago Ya Existe|";
                isOnline = false;
            }

            if (PagoUtil.isPagoMigracion(pago.getPrestamoId(), pago.getAnio(), pago.getSemana())) {
                msgAux += "El Pago cuenta con migración|";
                isOnline = false;
            }

            PrestamoModel prestamo = prestamoRepository.getPrestamoByPrestamoId(pago.getPrestamoId());

            if (prestamo == null) {
                msgAux += "No Se Encontró Ningún Prestamo Con Ese 'prestamoId'|";
                isOnline = false;
            }

            PagoModel pagoModel = PagoUtil.getPagoModelFromPagoConLiquidacion(pago);
            LiquidacionModel liquidacionModel = pago.getInfoLiquidacion();

        /*
            A continuación se hace una validación para comprobar si el pago mandado lleva liquidación,
            de ser así se hará el proceso dentro del if, sino se hará el proceso dentro del else.
         */
            if (liquidacionModel != null) {
                liquidacionModel.setPagoId(pagoModel.getPagoId());
                pagoModel.setAbreCon(prestamo.getTotalAPagar() - pagoRepository.getSaldoFromPrestamoByPrestamoId(pago.getPrestamoId()));
                pagoModel.setCierraCon(0.0);
                pagoModel.setEsPrimerPago(false);

                Call<ResponseBodyXms> call = RetrofitOdoo.getInstance().getApi().liquidacionCreateOne(session, new LiquidacionBody(liquidacionModel));
                retrofitOdooService.sendCall(call);
            } else {
                pagoModel.setAbreCon(prestamo.getTotalAPagar() - pagoRepository.getSaldoFromPrestamoByPrestamoId(pago.getPrestamoId()));
                pagoModel.setCierraCon(pagoModel.getAbreCon() - pago.getMonto());
                pagoModel.setEsPrimerPago(false);
            }

            try {
                if (isOnline == true) {
                    pagoRepository.save(pagoModel);

                    AgenciaModel agencia = agenciaRepository.getAgenciaModelByAgenciaId(pago.getAgente());
                    GerenciaModel gerencia = gerenciaRepository.findOneByGerenciaId(agencia.getGerenciaId());
                    PagoUtil.sendPayMessage(prestamo, pagoModel, gerencia);

                    Call<ResponseBodyXms> call = RetrofitOdoo.getInstance().getApi().pagoCreateOne(session, new PagoBody(new PagoList(pagoModel)));
                    retrofitOdooService.sendCall(call);

                    if (liquidacionModel != null)
                        liquidacionRepository.save(liquidacionModel);
                } else
                    msg = msgAux;
            } catch (HttpClientErrorException e) {
                msg = e.toString();
                isOnline = false;
            }

            objeto.put("id", pago.getPagoId());
            objeto.put("isOnline", isOnline);
            objeto.put("msg", msg);
            respuesta.add(objeto);
        }

        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(path = "/history/{prestamoId}")
    public @ResponseBody ResponseEntity<ArrayList<PagoVistaModel>> getHistorialDePagos(@PathVariable("prestamoId") String prestamoId) {
        ArrayList<PagoVistaModel> pagos = pagoRepository.getHistorialDePagosToApp(prestamoId);

        if (pagos.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(pagos, HttpStatus.OK);
    }
}
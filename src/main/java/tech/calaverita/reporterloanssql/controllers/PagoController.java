package tech.calaverita.reporterloanssql.controllers;

import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import tech.calaverita.reporterloanssql.models.LiquidacionModel;
import tech.calaverita.reporterloanssql.models.PagoModel;
import tech.calaverita.reporterloanssql.models.PrestamoModel;
import tech.calaverita.reporterloanssql.pojos.PagoConLiquidacion;
import tech.calaverita.reporterloanssql.repositories.LiquidacionRepository;
import tech.calaverita.reporterloanssql.repositories.PagoRepository;
import tech.calaverita.reporterloanssql.repositories.PrestamoRepository;
import tech.calaverita.reporterloanssql.helpers.PagoConLiquidacionUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping(path = "/xpress/v1/pays")
public class PagoController {
    @Autowired
    private PagoRepository pagoRepository;
    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private LiquidacionRepository liquidacionRepository;

    @GetMapping(path = "/{agencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<ArrayList<PagoModel>> getPagosByAgencia(@PathVariable("agencia") String agencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana) {
        ArrayList<PagoModel> pagos = pagoRepository.getPagoModelsByAgenciaAnioAndSemana(agencia, anio, semana);

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
    public @ResponseBody ResponseEntity<String> setPago(@RequestBody PagoConLiquidacion pago) {
        if (pago.getPagoId() == null)
            return new ResponseEntity<>("Debe Ingresar El 'pagoId'", HttpStatus.BAD_REQUEST);

        Optional<PagoModel> pagoAux = pagoRepository.findById(pago.getPagoId());

        if (pago.getPrestamoId() == null || pago.getPrestamoId().equalsIgnoreCase(""))
            return new ResponseEntity<>("Debe Ingresar El 'prestamoId'", HttpStatus.BAD_REQUEST);

        if (!pagoAux.isEmpty())
            return new ResponseEntity<>("El Pago Ya Existe", HttpStatus.CONFLICT);

        PrestamoModel prestamo = prestamoRepository.getPrestamoModelByPrestamoId(pago.getPrestamoId());

        if (prestamo == null)
            return new ResponseEntity<>("No Se Encontró Ningún Prestamo Con Ese 'prestamoId'", HttpStatus.NOT_FOUND);

        PagoModel pagoModel = PagoConLiquidacionUtil.getPagoModelFromPagoConLiquidacion(pago);
        LiquidacionModel liquidacionModel = pago.getInfoLiquidacion();

        /*
            A continuación se hace una validación para comprobar si el pago mandado lleva liquidación,
            de ser así se hará el proceso dentro del if, sino se hará el proceso dentro del else.
         */
        if (liquidacionModel != null) {
            liquidacionRepository.save(liquidacionModel);
            liquidacionModel.setPagoId(pagoModel.getPagoId());
            pagoModel.setAbreCon(prestamo.getTotalAPagar() - pagoRepository.getSaldoFromPrestamoByPrestamoId(pago.getPrestamoId()));
            pagoModel.setCierraCon(0.0);
            pagoModel.setEsPrimerPago(false);
        } else {
            pagoModel.setAbreCon(prestamo.getTotalAPagar() - pagoRepository.getSaldoFromPrestamoByPrestamoId(pago.getPrestamoId()));
            pagoModel.setCierraCon(pagoModel.getAbreCon() - pago.getMonto());
            pagoModel.setEsPrimerPago(false);
        }

        pagoRepository.save(pagoModel);

        String payMessage = (PagoConLiquidacionUtil.getPayMessage(prestamo, pagoModel));
        PagoConLiquidacionUtil.sendPayMessage(payMessage);

        return new ResponseEntity<>("Pago Insertado con Éxito", HttpStatus.CREATED);
    }

    @PostMapping(path = "/create-many")
    public @ResponseBody ResponseEntity<ArrayList<HashMap<String, Object>>> setPagos(@RequestBody ArrayList<PagoConLiquidacion> pagos) {
        ArrayList<HashMap<String, Object>> respuesta = new ArrayList<>();
        
        for(PagoConLiquidacion pago: pagos){
            HashMap<String, Object> objeto = new HashMap<>();
            String msg = "OK";
            String msgAux = "";
            Boolean isOnline = true;
            
            if (pago.getPagoId() == null){
                msgAux += "Debe Ingresar El 'pagoId' \n";
                isOnline = false;
            }

            Optional<PagoModel> pagoAux = pagoRepository.findById(pago.getPagoId());

            if (pago.getPrestamoId() == null || pago.getPrestamoId().equalsIgnoreCase("")){
                msgAux += "Debe Ingresar El 'prestamoId' \n";
                isOnline = false;
            }

            if (!pagoAux.isEmpty()){
                msgAux += "El Pago Ya Existe \n";
                isOnline = false;
            }

            PrestamoModel prestamo = prestamoRepository.getPrestamoModelByPrestamoId(pago.getPrestamoId());

            if (prestamo == null){
                msgAux += "No Se Encontró Ningún Prestamo Con Ese 'prestamoId' \n";
                isOnline = false;
            }

            PagoModel pagoModel = PagoConLiquidacionUtil.getPagoModelFromPagoConLiquidacion(pago);
            LiquidacionModel liquidacionModel = pago.getInfoLiquidacion();

        /*
            A continuación se hace una validación para comprobar si el pago mandado lleva liquidación,
            de ser así se hará el proceso dentro del if, sino se hará el proceso dentro del else.
         */
            if (liquidacionModel != null) {
                liquidacionRepository.save(liquidacionModel);
                liquidacionModel.setPagoId(pagoModel.getPagoId());
                pagoModel.setAbreCon(prestamo.getTotalAPagar() - pagoRepository.getSaldoFromPrestamoByPrestamoId(pago.getPrestamoId()));
                pagoModel.setCierraCon(0.0);
                pagoModel.setEsPrimerPago(false);
            } else {
                pagoModel.setAbreCon(prestamo.getTotalAPagar() - pagoRepository.getSaldoFromPrestamoByPrestamoId(pago.getPrestamoId()));
                pagoModel.setCierraCon(pagoModel.getAbreCon() - pago.getMonto());
                pagoModel.setEsPrimerPago(false);
            }

            try{
                if(isOnline == true)
                    pagoRepository.save(pagoModel);
            }catch (HttpClientErrorException e){
                msg = e.toString();
                isOnline = false;
            }

            objeto.put("id", pago.getPagoId());
            objeto.put("isOnline", isOnline);
            objeto.put("msg", msg);
            respuesta.add(objeto);

            String payMessage = (PagoConLiquidacionUtil.getPayMessage(prestamo, pagoModel));
            PagoConLiquidacionUtil.sendPayMessage(payMessage);
        }

        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

//    @PutMapping(path = "/liquidacion-pago")
//    public @ResponseBody ResponseEntity<String> setLiquidacionPrestamo(@RequestBody PagoConLiquidacion liquidacion) {
//
//        PrestamoModel prestamo = prestamoRepository.getPrestamoModelByPrestamoId(liquidacion.getPrestamoId());
//
//        if (prestamo == null)
//            return new ResponseEntity<>("No Se Encontró Ningún Prestamo Con Ese 'PrestamoId'", HttpStatus.NOT_FOUND);
//
//        prestamo.setWkDescu(liquidacion.getSemana() + "-" + liquidacion.getAnio());
//        prestamo.setDescuento(liquidacion.getDescuento());
//        prestamo.setSaldo(0.0);
//
//        liquidacion.getPago().setCierraCon(0.0);
//
//        prestamoRepository.save(prestamo);
//        pagoRepository.save(liquidacion.getPago());
//
//        return new ResponseEntity<>("Liquidación Cargada con Éxito", HttpStatus.OK);
//    }

    @GetMapping(path = "history/{prestamoId}")
    public @ResponseBody ResponseEntity<ArrayList<PagoModel>> getHistorialDePagos(@PathVariable("prestamoId") String prestamoId) {
        ArrayList<PagoModel> pagos = pagoRepository.getHistorialDePagos(prestamoId);

        if (pagos.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(pagos, HttpStatus.OK);
    }
}

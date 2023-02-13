package tech.calaverita.reporterloanssql.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.models.PagoModel;
import tech.calaverita.reporterloanssql.models.PrestamoModel;
import tech.calaverita.reporterloanssql.pojos.Liquidacion;
import tech.calaverita.reporterloanssql.repositories.PagoRepository;
import tech.calaverita.reporterloanssql.repositories.PrestamoRepository;

import java.util.ArrayList;

@RestController
@RequestMapping(path = "/xpress/v1/pays")
public class PagoController {
    @Autowired
    private PagoRepository pagoRepository;
    @Autowired
    private PrestamoRepository prestamoRepository;

    @GetMapping(path = "/cobranza-agencia/{agencia}/{anio}/{semana}")
    public @ResponseBody ArrayList<PagoModel> getPagosCobranzaByAgencia(@PathVariable("agencia") String agencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana){
        ArrayList<PagoModel> pagos = pagoRepository.getPagoModelsByAgenciaAnioAndSemana(agencia, anio, semana);

        return pagos;
    }

    @PostMapping(path = "/create-one")
    public @ResponseBody String setPago(@RequestBody PagoModel pago){

        PrestamoModel prestamo = prestamoRepository.getPrestamoModelByPrestamoId(pago.getPrestamoId());

        pago.setAbreCon(prestamo.getSaldo());
        pago.setCierraCon(prestamo.getSaldo() - pago.getMonto());
        pago.setEsPrimerPago(false);

        pagoRepository.save(pago);

        return "Pago Insertado con Éxito";
    }

    @PutMapping(path = "/liquidacion-pago")
    public @ResponseBody String setLiquidacionPrestamo(@RequestBody Liquidacion liquidacion){

        PrestamoModel prestamo = prestamoRepository.getPrestamoModelByPrestamoId(liquidacion.getPrestamoId());
        prestamo.setWkDescu(liquidacion.getSemana() + "-" + liquidacion.getAnio());
        prestamo.setDescuento(liquidacion.getDescuento());
        prestamo.setSaldo(0);

        PagoModel pago = pagoRepository.getPagoModelByPrestamoIdAnioAndSemana(liquidacion.getPrestamoId(), liquidacion.getAnio(), liquidacion.getSemana());
        pago.setCierraCon(0);

        prestamoRepository.save(prestamo);
        pagoRepository.save(pago);

        return "Liquidación Cargada con Éxito";
    }
}

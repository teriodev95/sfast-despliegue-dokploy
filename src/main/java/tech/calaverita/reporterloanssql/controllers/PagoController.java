package tech.calaverita.reporterloanssql.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.models.PagoModel;
import tech.calaverita.reporterloanssql.models.PrestamoModel;
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

    @GetMapping(path = "/dashboard-agencia/{agencia}/{anio}/{semana}")
    public @ResponseBody ArrayList<PagoModel> getPagosForDashboard(@PathVariable("agencia") String agencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana){

        return pagoRepository.getPagoModelsByAgenteAnioSemanaAndEsPrimerPago(agencia, anio, semana);
    }

    @GetMapping(path = "/dashboard-gerencia/{anio}/{semana}")
    public @ResponseBody ArrayList<PagoModel> getPagosForDashboardByGerencia(@PathVariable("anio") int anio, @PathVariable("semana") int semana){

        return pagoRepository.getPagoModelsByAnioAndSemana(anio, semana);
    }

    @GetMapping(path = "/cobranza-agencia/{agencia}/{anio}/{semana}")
    public @ResponseBody ArrayList<PagoModel> getPagosForCobranzaByAgencia(@PathVariable("agencia") String agencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana){

        return pagoRepository.getPagoModelsByAgenteAnioAndSemana(agencia, anio, semana - 1);
    }

    @GetMapping(path = "/cobranza-gerencia/{anio}/{semana}")
    public @ResponseBody ArrayList<PagoModel> getPagosForCobranzaByGerencia(@PathVariable("anio") int anio, @PathVariable("semana") int semana){

        return pagoRepository.getPagoModelsByAnioAndSemana(anio, semana - 1);
    }

    @PostMapping(path = "create-one")
    public @ResponseBody String setPago(@RequestBody PagoModel pago){

        PrestamoModel prestamo = prestamoRepository.getPrestamoModelsByPrestamoId(pago.getPrestamoId());

        pago.setAbreCon(prestamo.getSaldo());
        pago.setCierraCon(prestamo.getSaldo() - pago.getMonto());
        pago.setEsPrimerPago(false);

        pagoRepository.save(pago);

        return "Pago Insertado con Éxito";
    }
}

package tech.calaverita.reporterloanssql.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.calaverita.reporterloanssql.models.PagoModel;
import tech.calaverita.reporterloanssql.repositories.PagoRepository;

import java.util.ArrayList;

@Controller
@RequestMapping(path = "/xpress/v1/pays")
public class PagoController {
    @Autowired
    private PagoRepository pagoRepository;

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


}

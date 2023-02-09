package tech.calaverita.reporterloanssql.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.models.PrestamoModel;
import tech.calaverita.reporterloanssql.pojos.Cobranza;
import tech.calaverita.reporterloanssql.pojos.Dashboard;
import tech.calaverita.reporterloanssql.repositories.PagoRepository;
import tech.calaverita.reporterloanssql.repositories.XpressRepository;
import tech.calaverita.reporterloanssql.repositories.PrestamoRepository;

import java.util.ArrayList;

@RestController()
@RequestMapping(path = "/xpress/v1")
public class XpressController {
    @Autowired
    private XpressRepository prestamoPagoRespository;
    @Autowired
    private PrestamoRepository prestamoRepository;
    @Autowired
    private PagoRepository pagoRepository;

    @GetMapping(path = "/cobranza-agencia/{agencia}/{anio}/{semana}")
    public @ResponseBody Cobranza getCobranzaByAgencia(@PathVariable("agencia") String agencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana) {

        ArrayList<PrestamoModel> prestamoModels = prestamoRepository.getPrestamoModelsForCobranzaByAgencia(agencia, anio, semana);
        String result = prestamoPagoRespository.getCobranzaByAgencia(agencia, anio, semana);

        String[] texto = result.split(",");

        Cobranza cobranza = new Cobranza();

        cobranza.setGerencia(texto[6]);
        cobranza.setAnio(anio);
        cobranza.setSemana(semana);
        cobranza.setAgencia(texto[0]);
        cobranza.setClientes(Integer.parseInt(texto[1]));
        cobranza.setDebitoMiercoles(Double.parseDouble(texto[2]));
        cobranza.setDebitoJueves(Double.parseDouble(texto[3]));
        cobranza.setDebitoViernes(Double.parseDouble(texto[4]));
        cobranza.setDebitoTotal(Double.parseDouble(texto[5]));
        cobranza.setPrestamos(prestamoModels);

        return cobranza;
    }

    @GetMapping(path = "/cobranza-gerencia/{gerencia}/{anio}/{semana}")
    public @ResponseBody ArrayList<Cobranza> getCobranzaByGerencia(@PathVariable("gerencia") String gerencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana) {
        ArrayList<Cobranza> cobranzas = new ArrayList<>();

        ArrayList<PrestamoModel> prestamoModels = prestamoRepository.getPrestamoModelsForCobranzaByGerencia(gerencia, anio, semana);
        String[] agencias = prestamoPagoRespository.getCobranzaByGerencia(gerencia, anio, semana);

        for (int i = 0; i < agencias.length; i++) {
            String[] texto = agencias[i].split(",");

            ArrayList<PrestamoModel> prestamoModelsAux = new ArrayList<>();

            for (PrestamoModel prestamo : prestamoModels) {
                if (prestamo.getAgente().equalsIgnoreCase(texto[0]))
                    prestamoModelsAux.add(prestamo);
            }

            Cobranza cobranza = new Cobranza();

            cobranza.setGerencia(gerencia);
            cobranza.setAnio(anio);
            cobranza.setSemana(semana);
            cobranza.setAgencia(texto[0]);
            cobranza.setClientes(Integer.parseInt(texto[1]));
            cobranza.setDebitoMiercoles(Double.parseDouble(texto[2]));
            cobranza.setDebitoJueves(Double.parseDouble(texto[3]));
            cobranza.setDebitoViernes(Double.parseDouble(texto[4]));
            cobranza.setDebitoTotal(Double.parseDouble(texto[5]));
            cobranza.setPrestamos(prestamoModelsAux);

            cobranzas.add(cobranza);
        }

        return cobranzas;
    }

    @GetMapping(path = "/dashboard-agencia/{agencia}/{anio}/{semana}")
    public @ResponseBody Dashboard getDashboardByAgencia(@PathVariable("agencia") String agencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana){

        String result = prestamoPagoRespository.getDashboardByAgencia(agencia, anio, semana);

        String[] texto = result.split(",");

        Dashboard dashboard = new Dashboard();

        dashboard.setGerencia(texto[17]);
        dashboard.setAnio(anio);
        dashboard.setSemana(semana);
        dashboard.setAgencia(texto[0]);
        dashboard.setClientes(Integer.parseInt(texto[1]));
        dashboard.setNoPagos(Integer.parseInt(texto[2]));
        dashboard.setNumeroLiquidaciones(Integer.parseInt(texto[3]));
        dashboard.setPagosReducidos(Integer.parseInt(texto[4]));
        dashboard.setDebitoMiercoles(Double.parseDouble(texto[5]));
        dashboard.setDebitoJueves(Double.parseDouble(texto[6]));
        dashboard.setDebitoViernes(Double.parseDouble(texto[7]));
        dashboard.setDebitoTotal(Double.parseDouble(texto[8]));
        dashboard.setRendimiento(Double.parseDouble(texto[9]));
        dashboard.setTotalDeDescuento(Double.parseDouble(texto[10]));
        dashboard.setTotalCobranzaPura(Double.parseDouble(texto[11]));
        dashboard.setMontoExcedente(Double.parseDouble(texto[12]));
        dashboard.setMultas(Double.parseDouble(texto[13]));
        dashboard.setLiquidaciones(Double.parseDouble(texto[14]));
        dashboard.setCobranzaTotal(Double.parseDouble(texto[15]));
        dashboard.setMontoDeDebitoFaltante(Double.parseDouble(texto[16]));

        return dashboard;
    }

    @GetMapping(path = "/dashboard-gerencia/{gerencia}/{anio}/{semana}")
    public @ResponseBody ArrayList<Dashboard> getDashboardByGerencia(@PathVariable("gerencia") String gerencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana) {
        ArrayList<Dashboard> dashboards = new ArrayList<>();

        String[] agencias = prestamoPagoRespository.getDashboardByGerencia(gerencia, anio, semana);

        for (String agencia : agencias) {
            String[] texto = agencia.split(",");

            Dashboard dashboard = new Dashboard();

            dashboard.setGerencia(gerencia);
            dashboard.setAnio(anio);
            dashboard.setSemana(semana);
            dashboard.setAgencia(texto[0]);
            dashboard.setClientes(Integer.parseInt(texto[1]));
            dashboard.setNoPagos(Integer.parseInt(texto[2]));
            dashboard.setNumeroLiquidaciones(Integer.parseInt(texto[3]));
            dashboard.setPagosReducidos(Integer.parseInt(texto[4]));
            dashboard.setDebitoMiercoles(Double.parseDouble(texto[5]));
            dashboard.setDebitoJueves(Double.parseDouble(texto[6]));
            dashboard.setDebitoViernes(Double.parseDouble(texto[7]));
            dashboard.setDebitoTotal(Double.parseDouble(texto[8]));
            dashboard.setRendimiento(Double.parseDouble(texto[9]));
            dashboard.setTotalDeDescuento(Double.parseDouble(texto[10]));
            dashboard.setTotalCobranzaPura(Double.parseDouble(texto[11]));
            dashboard.setMontoExcedente(Double.parseDouble(texto[12]));
            dashboard.setMultas(Double.parseDouble(texto[13]));
            dashboard.setLiquidaciones(Double.parseDouble(texto[14]));
            dashboard.setCobranzaTotal(Double.parseDouble(texto[15]));
            dashboard.setMontoDeDebitoFaltante(Double.parseDouble(texto[16]));

            dashboards.add(dashboard);
        }

        return dashboards;
    }
}
package tech.calaverita.reporterloanssql.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.models.PagoModel;
import tech.calaverita.reporterloanssql.models.PrestamoModel;
import tech.calaverita.reporterloanssql.pojos.BalanceAgencia;
import tech.calaverita.reporterloanssql.pojos.Cobranza;
import tech.calaverita.reporterloanssql.pojos.Dashboard;
import tech.calaverita.reporterloanssql.repositories.*;
import tech.calaverita.reporterloanssql.helpers.BalanceAgenciaScript;
import tech.calaverita.reporterloanssql.services.FileManagerService;

import java.io.*;
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
    @Autowired
    private AsignacionRepository asignacionRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private FileManagerService fileManagerService;

    @GetMapping(path = "/cobranza/{agencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<Cobranza> getCobranzaByAgencia(@PathVariable("agencia") String agencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana) {
        Cobranza cobranza = new Cobranza();

        cobranza.setAgencia(agencia);
        cobranza.setAnio(anio);
        cobranza.setSemana(semana);

        int anioAux = anio;
        int semanaAux = semana;

        if(semana == 1){
            anioAux -= 1;
            semanaAux = 54;
        }

        ArrayList<PrestamoModel> prestamoModels = prestamoRepository.getPrestamoModelsForCobranzaByAgencia(agencia, anioAux, semanaAux);

        if (prestamoModels.isEmpty() && semanaAux == 54){
            semanaAux = 53;
            prestamoModels = prestamoRepository.getPrestamoModelsForCobranzaByAgencia(agencia, anioAux, semanaAux);
        }else if(prestamoModels.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        String result = prestamoPagoRespository.getCobranzaByAgencia(agencia, anioAux, semanaAux);

        String[] texto = result.split(",");

        cobranza.setGerencia(texto[6]);
        cobranza.setAnio(anio);
        cobranza.setSemana(semana);
        cobranza.setAgencia(texto[0]);
        cobranza.setClientes(prestamoPagoRespository.getClientesPorCobrarByAgenciaAnioAndSemana(agencia, anioAux, semanaAux));
        cobranza.setDebitoMiercoles(Double.parseDouble(texto[2]));
        cobranza.setDebitoJueves(Double.parseDouble(texto[3]));
        cobranza.setDebitoViernes(Double.parseDouble(texto[4]));
        cobranza.setDebitoTotal(Double.parseDouble(texto[5]));
        cobranza.setPrestamos(prestamoModels);

        return new ResponseEntity<>(cobranza, HttpStatus.OK);
    }

    @GetMapping(path = "/cobranza-gerencia/{gerencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<ArrayList<Cobranza>> getCobranzaByGerencia(@PathVariable("gerencia") String gerencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana) {
        ArrayList<Cobranza> cobranzas = new ArrayList<>();

        int anioAux = anio;
        int semanaAux = semana;

        if(semana == 1){
            anioAux -= 1;
            semanaAux = 54;
        }

        ArrayList<PrestamoModel> prestamoModels = prestamoRepository.getPrestamoModelsForCobranzaByGerencia(gerencia, anioAux, semanaAux);

        if (prestamoModels.isEmpty() && semanaAux == 54){
            semanaAux = 53;
            prestamoModels = prestamoRepository.getPrestamoModelsForCobranzaByAgencia(gerencia, anioAux, semanaAux);
        }else if(prestamoModels.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        String[] agencias = prestamoPagoRespository.getCobranzaByGerencia(gerencia, anioAux, semanaAux);

        if (agencias.length == 0)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

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
            cobranza.setClientes(prestamoPagoRespository.getClientesPorCobrarByAgenciaAnioAndSemana(texto[0], anioAux, semanaAux));
            cobranza.setDebitoMiercoles(Double.parseDouble(texto[2]));
            cobranza.setDebitoJueves(Double.parseDouble(texto[3]));
            cobranza.setDebitoViernes(Double.parseDouble(texto[4]));
            cobranza.setDebitoTotal(Double.parseDouble(texto[5]));
            cobranza.setPrestamos(prestamoModelsAux);

            cobranzas.add(cobranza);
        }

        return new ResponseEntity<>(cobranzas, HttpStatus.OK);
    }

    @GetMapping(path = "/dashboard-agencia/{agencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<Dashboard> getDashboardByAgencia(@PathVariable("agencia") String agencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana) {
        Dashboard dashboard = new Dashboard();

        ArrayList<PagoModel> pagos = pagoRepository.getPagoModelsByAgenciaAnioAndSemana(agencia, anio, semana);

        if (pagos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        String result = prestamoPagoRespository.getDashboardByAgencia(agencia, anio, semana);
        ResponseEntity<Cobranza> cobranza = getCobranzaByAgencia(agencia, anio, semana);

        String[] texto = result.split(",");

        dashboard.setGerencia(texto[11]);
        dashboard.setAnio(anio);
        dashboard.setSemana(semana);
        dashboard.setAgencia(texto[0]);
        dashboard.setClientes(cobranza.getBody().getClientes());
        dashboard.setNoPagos(Integer.parseInt(texto[1]));
        dashboard.setNumeroLiquidaciones(Integer.parseInt(texto[2]));
        dashboard.setPagosReducidos(Integer.parseInt(texto[3]));
        dashboard.setDebitoMiercoles(cobranza.getBody().getDebitoMiercoles());
        dashboard.setDebitoJueves(cobranza.getBody().getDebitoJueves());
        dashboard.setDebitoViernes(cobranza.getBody().getDebitoViernes());
        dashboard.setDebitoTotal(cobranza.getBody().getDebitoTotal());

        double rendimiento = Double.parseDouble(texto[5]) / cobranza.getBody().getDebitoTotal() * 100;

        dashboard.setRendimiento(Math.round(rendimiento * 100.0) / 100.0);
        dashboard.setTotalDeDescuento(Double.parseDouble(texto[4]));
        dashboard.setTotalCobranzaPura(Double.parseDouble(texto[5]));
        dashboard.setMontoExcedente(Double.parseDouble(texto[6]));
        dashboard.setMultas(Double.parseDouble(texto[7]));
        dashboard.setLiquidaciones(Double.parseDouble(texto[8]));
        dashboard.setCobranzaTotal(Double.parseDouble(texto[9]));
        dashboard.setMontoDeDebitoFaltante(Double.parseDouble(texto[10]));
        dashboard.setClientesCobrados(prestamoPagoRespository.getClientesCobradosByAgenciaAnioAndSemana(agencia, anio, semana).toString() + '/' + cobranza.getBody().getClientes());
        if(asignacionRepository.getSumaDeAsigancionesByAgenciaAnioAndSemana(agencia, anio, semana) != null)
            dashboard.setEfectivoEnCampo(dashboard.getCobranzaTotal() - asignacionRepository.getSumaDeAsigancionesByAgenciaAnioAndSemana(agencia, anio, semana));
        else
            dashboard.setEfectivoEnCampo(dashboard.getCobranzaTotal());

        return new ResponseEntity<>(dashboard, HttpStatus.OK);
    }

    @GetMapping(path = "/dashboard-gerencia/{gerencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<ArrayList<Dashboard>> getDashboardByGerencia(@PathVariable("gerencia") String gerencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana) {
        ArrayList<Dashboard> dashboards = new ArrayList<>();

        String[] agencias = prestamoPagoRespository.getDashboardByGerencia(gerencia, anio, semana);

        if (agencias.length == 0)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        for (String agencia : agencias) {
            String[] texto = agencia.split(",");

            ResponseEntity<Cobranza> cobranza = getCobranzaByAgencia(texto[0], anio, semana);

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
            dashboard.setClientesCobrados(prestamoPagoRespository.getClientesCobradosByAgenciaAnioAndSemana(agencia, anio, semana).toString() + '/' + cobranza.getBody().getClientes());
            if(asignacionRepository.getSumaDeAsigancionesByAgenciaAnioAndSemana(texto[0], anio, semana) != null)
                dashboard.setEfectivoEnCampo(dashboard.getCobranzaTotal() - asignacionRepository.getSumaDeAsigancionesByAgenciaAnioAndSemana(texto[0], anio, semana));
            else
                dashboard.setEfectivoEnCampo(dashboard.getCobranzaTotal());

            dashboards.add(dashboard);
        }

        return new ResponseEntity<>(dashboards, HttpStatus.OK);
    }

    @GetMapping(path = "/balance-de-agencia/{agencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<ResponseEntity<byte[]>> getBalanceDeAgencia(@PathVariable("agencia") String agencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana) {
        String uri = "src\\main\\java\\tech\\calaverita\\reporterloanssql\\resources\\balancesDeAgencias\\balance-de-agencia.pdf";
        String fileName = "balance-de-agencia.pdf";
        File file = new File(uri);

        BalanceAgencia balanceAgencia = new BalanceAgencia();

        balanceAgencia.setDashboard(getDashboardByAgencia(agencia, anio, semana));
        balanceAgencia.setAgente(usuarioRepository.getUsuarioByUsuario(agencia));
        balanceAgencia.setGerente(usuarioRepository.getUsuarioByUsuario(balanceAgencia.getDashboard().getBody().getGerencia()));
        balanceAgencia.setAsignaciones(asignacionRepository.getSumaDeAsigancionesByAgenciaAnioAndSemana(agencia, anio, semana));

        BalanceAgenciaScript balanceAgenciaScript = new BalanceAgenciaScript(balanceAgencia);
        balanceAgenciaScript.writePdf();

        return new ResponseEntity<>(fileManagerService.getPdf(uri, fileName), HttpStatus.CREATED);
    }
}
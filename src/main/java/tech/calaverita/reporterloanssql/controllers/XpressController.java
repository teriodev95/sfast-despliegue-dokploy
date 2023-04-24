package tech.calaverita.reporterloanssql.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.pojos.Dashboard;
import tech.calaverita.reporterloanssql.pojos.Repositories;
import tech.calaverita.reporterloanssql.threads.CobranzaGerenciaThread;
import tech.calaverita.reporterloanssql.threads.CobranzaThread;
import tech.calaverita.reporterloanssql.pojos.Cobranza;
import tech.calaverita.reporterloanssql.pojos.Models;
import tech.calaverita.reporterloanssql.repositories.*;
import tech.calaverita.reporterloanssql.services.FileManagerService;
import tech.calaverita.reporterloanssql.threads.DashboardGerenciaThread;
import tech.calaverita.reporterloanssql.threads.DashboardThread;

import java.util.ArrayList;

@RestController()
@RequestMapping(path = "/xpress/v1")
public class XpressController {
    @Autowired
    private XpressRepository xpressRepository;
    @Autowired
    private PrestamoRepository prestamoRepository;
    @Autowired
    private PagoRepository pagoRepository;
    @Autowired
    private AsignacionRepository asignacionRepository;
    @Autowired
    private LiquidacionRepository liquidacionRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private FileManagerService fileManagerService;

    Repositories repositories;

    @GetMapping(path = "/cobranza/{agencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<Cobranza> getCobranzaByAgencia(@PathVariable("agencia") String agencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana) {
        Cobranza cobranza = new Cobranza();
        Models models = new Models();

        repositories = new Repositories();
        repositories.setPrestamoRepository(prestamoRepository);
        repositories.setPagoRepository(pagoRepository);

        models.setCobranza(cobranza);
        models.getCobranza().setAgencia(agencia);
        models.getCobranza().setAnio(anio);
        models.getCobranza().setSemana(semana);

        Thread[] threads = new Thread[8];

        for(int i = 0; i < 8; i++){
            threads[i] = new Thread(new CobranzaThread(repositories, models, i));
        }

        threads[0].start();
        threads[1].start();

        while(models.getPrestamosToCobranza() == null){

        }

        threads[2].start();
        threads[3].start();

        while(models.getPagosVistaToCobranza() == null){

        }

        threads[4].start();
        threads[5].start();
        threads[6].start();
        threads[7].start();

        for (int i = 4; i < 8; i++){
            while(threads[i].isAlive()){
            }
        }

//        Thread cobranzaThread1 = new Thread(new CobranzaUtil(xpressRepository, cobranza, models, 0));
//        cobranzaThread1.start();
//        Thread cobranzaThread2 = new Thread(new CobranzaUtil(xpressRepository, cobranza, models, 1));
//        cobranzaThread2.start();

//        Cobranza cobranza = new Cobranza();
//
//        cobranza.setAgencia(agencia);
//        cobranza.setAnio(anio);
//        cobranza.setSemana(semana);
//
//        int anioAux = anio;
//        int semanaAux = semana;
//
//        if(semana == 1){
//            anioAux -= 1;
//            semanaAux = 54;
//        }
//
//        ArrayList<PrestamoModel> prestamoModels = prestamoRepository.getPrestamoModelsForCobranzaByAgencia(agencia, anioAux, semanaAux);
//
//        if (prestamoModels.isEmpty() && semanaAux == 54){
//            semanaAux = 53;
//            prestamoModels = prestamoRepository.getPrestamoModelsForCobranzaByAgencia(agencia, anioAux, semanaAux);
//        }else if(prestamoModels.isEmpty())
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//
//        String result = prestamoPagoRespository.getCobranzaByAgencia(agencia, anioAux, semanaAux);
//
//        String[] texto = result.split(",");
//
//        cobranza.setGerencia(texto[6]);
//        cobranza.setAnio(anio);
//        cobranza.setSemana(semana);
//        cobranza.setAgencia(texto[0]);
//        cobranza.setClientes(prestamoPagoRespository.getClientesPorCobrarByAgenciaAnioAndSemana(agencia, anioAux, semanaAux));
//        cobranza.setDebitoMiercoles(Double.parseDouble(texto[2]));
//        cobranza.setDebitoJueves(Double.parseDouble(texto[3]));
//        cobranza.setDebitoViernes(Double.parseDouble(texto[4]));
//        cobranza.setDebitoTotal(Double.parseDouble(texto[5]));
//        cobranza.setPrestamos(prestamoModels);

        return new ResponseEntity<>(models.getCobranza(), HttpStatus.OK);
    }

    @GetMapping(path = "/cobranza-gerencia/{gerencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<ArrayList<Cobranza>> getCobranzaByGerencia(@PathVariable("gerencia") String gerencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana) {
        ArrayList<String> agencias = xpressRepository.getAgenciasByGerencia(gerencia, anio, semana);

        repositories = new Repositories();
        repositories.setPrestamoRepository(prestamoRepository);
        repositories.setPagoRepository(pagoRepository);

        Thread[] threads = new Thread[agencias.size()];

        ArrayList<Models> models = new ArrayList<>();
        ArrayList<Cobranza> cobranzas = new ArrayList<>();

        Cobranza[] cobranzaArray = new Cobranza[threads.length];
        Models[] modelArray = new Models[threads.length];

        for(int i = 0; i < threads.length; i++){
            cobranzaArray[i] = new Cobranza();
            modelArray[i] = new Models();

            cobranzas.add(cobranzaArray[i]);
            models.add(modelArray[i]);
        }

        for(int i = 0; i < threads.length; i++){
            models.get(i).setCobranza(cobranzas.get(i));
            models.get(i).getCobranza().setAgencia(agencias.get(i));
            models.get(i).getCobranza().setAnio(anio);
            models.get(i).getCobranza().setSemana(semana);

            threads[i] = new Thread(new CobranzaGerenciaThread(repositories, models.get(i)));
        }

        for(int i = 0; i < threads.length; i++){
            threads[i].start();
        }

        for(int i = 0; i < threads.length; i++){
            while(threads[i].isAlive()){

            }
        }
//
//        int anioAux = anio;
//        int semanaAux = semana;
//
//        if(semana == 1){
//            anioAux -= 1;
//            semanaAux = 54;
//        }
//
//        ArrayList<PrestamoModel> prestamoModels = prestamoRepository.getPrestamoModelsForCobranzaByGerencia(gerencia, anioAux, semanaAux);
//
//        if (prestamoModels.isEmpty() && semanaAux == 54){
//            semanaAux = 53;
//            prestamoModels = prestamoRepository.getPrestamoModelsForCobranzaByAgencia(gerencia, anioAux, semanaAux);
//        }else if(prestamoModels.isEmpty())
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//
//        String[] agencias = prestamoPagoRespository.getCobranzaByGerencia(gerencia, anioAux, semanaAux);
//
//        if (agencias.length == 0)
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//
//        for (int i = 0; i < agencias.length; i++) {
//            String[] texto = agencias[i].split(",");
//
//            ArrayList<PrestamoModel> prestamoModelsAux = new ArrayList<>();
//
//            for (PrestamoModel prestamo : prestamoModels) {
//                if (prestamo.getAgente().equalsIgnoreCase(texto[0]))
//                    prestamoModelsAux.add(prestamo);
//            }
//
//            Cobranza cobranza = new Cobranza();
//
//            cobranza.setGerencia(gerencia);
//            cobranza.setAnio(anio);
//            cobranza.setSemana(semana);
//            cobranza.setAgencia(texto[0]);
//            cobranza.setClientes(prestamoPagoRespository.getClientesPorCobrarByAgenciaAnioAndSemana(texto[0], anioAux, semanaAux));
//            cobranza.setDebitoMiercoles(Double.parseDouble(texto[2]));
//            cobranza.setDebitoJueves(Double.parseDouble(texto[3]));
//            cobranza.setDebitoViernes(Double.parseDouble(texto[4]));
//            cobranza.setDebitoTotal(Double.parseDouble(texto[5]));
//            cobranza.setPrestamos(prestamoModelsAux);
//
//            cobranzas.add(cobranza);
//        }
//
        return new ResponseEntity<>(cobranzas, HttpStatus.OK);
    }

    @GetMapping(path = "/dashboard-agencia/{agencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<Dashboard> getDashboardByAgencia(@PathVariable("agencia") String agencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana) {
        Dashboard dashboard = new Dashboard();
        Models models = new Models();

        repositories = new Repositories();
        repositories.setPrestamoRepository(prestamoRepository);
        repositories.setPagoRepository(pagoRepository);
        repositories.setAsignacionRepository(asignacionRepository);
        repositories.setLiquidacionRepository(liquidacionRepository);

        models.setDashboard(dashboard);
        models.getDashboard().setAgencia(agencia);
        models.getDashboard().setAnio(anio);
        models.getDashboard().setSemana(semana);

        Thread[] threads = new Thread[24];

        for(int i = 0; i < threads.length; i++){
            threads[i] = new Thread(new DashboardThread(repositories, models, i));
        }

        for(int i = 0; i < 6; i++){
            threads[i].start();
        }

        while(models.getPrestamosToCobranza() == null){

        }

        for(int i = 6; i < 8; i++){
            threads[i].start();
        }

        while(models.getPagosVistaToCobranza() == null){

        }

        for(int i = 8; i < 12; i++){
            threads[i].start();
        }

        while(models.getLiquidaciones() == null || models.getPagosOfLiquidaciones() == null){

        }

        for(int i = 12; i < 15; i++){
            threads[i].start();
        }

        while(models.getPagosVistaToDashboard() == null){

        }

        if (models.getPagosVistaToDashboard().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        for(int i = 15; i < 20; i++){
            threads[i].start();
        }

        while(models.getDashboard().getCobranzaTotal() == null || models.getDashboard().getMontoExcedente() == null){

        }

        for(int i = 20; i < 22; i++){
            threads[i].start();
        }

        while(models.getDashboard().getTotalCobranzaPura() == null){

        }

        for(int i = 22; i < 24; i++){
            threads[i].start();
        }

        while(models.getDashboard().getRendimiento() == null || models.getDashboard().getMontoDeDebitoFaltante() == null){

        }

        return new ResponseEntity<>(models.getDashboard(), HttpStatus.OK);
    }

    @GetMapping(path = "/dashboard-gerencia/{gerencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<ArrayList<Dashboard>> getDashboardByGerencia(@PathVariable("gerencia") String gerencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana) {
        ArrayList<String> agencias = xpressRepository.getAgenciasByGerencia(gerencia, anio, semana);

        repositories = new Repositories();
        repositories.setPrestamoRepository(prestamoRepository);
        repositories.setPagoRepository(pagoRepository);
        repositories.setAsignacionRepository(asignacionRepository);
        repositories.setLiquidacionRepository(liquidacionRepository);

        Thread[] threads = new Thread[agencias.size()];

        ArrayList<Models> models = new ArrayList<>();
        ArrayList<Dashboard> dashboards = new ArrayList<>();

        Dashboard[] dashboardArray = new Dashboard[threads.length];
        Models[] modelArray = new Models[threads.length];

        for(int i = 0; i < threads.length; i++){
            dashboardArray[i] = new Dashboard();
            modelArray[i] = new Models();

            dashboards.add(dashboardArray[i]);
            models.add(modelArray[i]);
        }

        for(int i = 0; i < threads.length; i++){
            models.get(i).setDashboard(dashboards.get(i));
            models.get(i).getDashboard().setAgencia(agencias.get(i));
            models.get(i).getDashboard().setAnio(anio);
            models.get(i).getDashboard().setSemana(semana);

            threads[i] = new Thread(new DashboardGerenciaThread(repositories, models.get(i)));
        }

        for(int i = 0; i < threads.length; i++){
            threads[i].start();
        }

        for(int i = 0; i < threads.length; i++){
            while(threads[i].isAlive()){

            }
        }

//            Dashboard dashboard = new Dashboard();
//
//            dashboard.setGerencia(gerencia);
//            dashboard.setAnio(anio);
//            dashboard.setSemana(semana);
//            dashboard.setAgencia(texto[0]);
//            dashboard.setClientes(Integer.parseInt(texto[1]));
//            dashboard.setNoPagos(Integer.parseInt(texto[2]));
//            dashboard.setNumeroLiquidaciones(Integer.parseInt(texto[3]));
//            dashboard.setPagosReducidos(Integer.parseInt(texto[4]));
//            dashboard.setDebitoMiercoles(Double.parseDouble(texto[5]));
//            dashboard.setDebitoJueves(Double.parseDouble(texto[6]));
//            dashboard.setDebitoViernes(Double.parseDouble(texto[7]));
//            dashboard.setDebitoTotal(Double.parseDouble(texto[8]));
//            dashboard.setRendimiento(Double.parseDouble(texto[9]));
//            dashboard.setTotalDeDescuento(Double.parseDouble(texto[10]));
//            dashboard.setTotalCobranzaPura(Double.parseDouble(texto[11]));
//            dashboard.setMontoExcedente(Double.parseDouble(texto[12]));
//            dashboard.setMultas(Double.parseDouble(texto[13]));
//            dashboard.setLiquidaciones(Double.parseDouble(texto[14]));
//            dashboard.setCobranzaTotal(Double.parseDouble(texto[15]));
//            dashboard.setMontoDeDebitoFaltante(Double.parseDouble(texto[16]));
//            dashboard.setClientesCobrados(prestamoPagoRespository.getClientesCobradosByAgenciaAnioAndSemana(agencia, anio, semana));
//            if(asignacionRepository.getSumaDeAsigancionesByAgenciaAnioAndSemana(texto[0], anio, semana) != null)
//                dashboard.setEfectivoEnCampo(dashboard.getCobranzaTotal() - asignacionRepository.getSumaDeAsigancionesByAgenciaAnioAndSemana(texto[0], anio, semana));
//            else
//                dashboard.setEfectivoEnCampo(dashboard.getCobranzaTotal());
//
//            dashboards.add(dashboard);
//        }

        return new ResponseEntity<>(dashboards, HttpStatus.OK);
    }

//    @GetMapping(path = "/balance-de-agencia/{agencia}/{anio}/{semana}")
//    public @ResponseBody ResponseEntity<ResponseEntity<byte[]>> getBalanceDeAgencia(@PathVariable("agencia") String agencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana) {
//        String uri = "src\\main\\java\\tech\\calaverita\\reporterloanssql\\resources\\balancesDeAgencias\\balance-de-agencia.pdf";
//        String fileName = "balance-de-agencia.pdf";
//        File file = new File(uri);
//
//        BalanceAgencia balanceAgencia = new BalanceAgencia();
//
//        balanceAgencia.setDashboard(getDashboardByAgencia(agencia, anio, semana));
//        balanceAgencia.setAgente(usuarioRepository.getUsuarioByUsuario(agencia));
//        balanceAgencia.setGerente(usuarioRepository.getUsuarioByUsuario(balanceAgencia.getDashboard().getBody().getGerencia()));
//        balanceAgencia.setAsignaciones(asignacionRepository.getSumaDeAsigancionesByAgenciaAnioAndSemana(agencia, anio, semana));
//
//        BalanceAgenciaScript balanceAgenciaScript = new BalanceAgenciaScript(balanceAgencia);
//        balanceAgenciaScript.writePdf();
//
//        return new ResponseEntity<>(fileManagerService.getPdf(uri, fileName), HttpStatus.CREATED);
//    }
}
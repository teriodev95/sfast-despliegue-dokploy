package tech.calaverita.reporterloanssql.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.helpers.DashboardPorDiaUtil;
import tech.calaverita.reporterloanssql.helpers.pwa.PWAUtil;
import tech.calaverita.reporterloanssql.models.CalendarioModel;
import tech.calaverita.reporterloanssql.models.GerenciaModel;
import tech.calaverita.reporterloanssql.models.UsuarioModel;
import tech.calaverita.reporterloanssql.pojos.Dashboard;
import tech.calaverita.reporterloanssql.pojos.ObjectsContainer;
import tech.calaverita.reporterloanssql.pojos.pwa.CobranzaPWA;
import tech.calaverita.reporterloanssql.pojos.pwa.HistoricoPWA;
import tech.calaverita.reporterloanssql.security.AuthCredentials;
import tech.calaverita.reporterloanssql.services.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@CrossOrigin
@RestController
@RequestMapping("/xpress/v1/pwa")
public class PWAController {
    @Autowired
    private RepositoriesContainer repositoriesContainer;
    ObjectsContainer[] objectsContainers;

    @GetMapping("/gerencias")
    public ResponseEntity<ArrayList<GerenciaModel>> getGerenciaModels() {
        ArrayList<GerenciaModel> gerenciaModels = GerenciaService.getGerenciaModels();

        if (gerenciaModels.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(gerenciaModels, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthCredentials login) {
        UsuarioModel usuarioModel = UsuarioService.findOneByUsuarioAndPin(login.getUsername(), login.getPassword());

        if (usuarioModel == null) {
            return new ResponseEntity<>("Usuario y/o contraseña incorrecto", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(usuarioModel, HttpStatus.OK);
    }

    @GetMapping("/agencias/{gerencia}")
    public ResponseEntity<ArrayList<String>> getAgenciasByGerencia(@PathVariable("gerencia") String gerencia) {
        ArrayList<String> agencias = AgenciaService.getAgenciasByGerencia(gerencia);

        if (agencias.isEmpty()) {
            return new ResponseEntity<>(agencias, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(agencias, HttpStatus.OK);
    }

    @GetMapping("/gerencias/{filtro}")
    public ResponseEntity<ArrayList<String>> getGerenciasBySeguridad(@PathVariable("filtro") String filtro) {
        ArrayList<String> gerencias = GerenciaService.findManyBySeguridad(filtro).isEmpty() ? GerenciaService.findManyByRegional(filtro) : GerenciaService.findManyBySeguridad(filtro);

        if (gerencias.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(gerencias, HttpStatus.OK);
    }

    @GetMapping("/cobranza/{agencia}/{anio}/{semana}")
    public ResponseEntity<CobranzaPWA> getCobranzaByAgenciaAnioAndSemana(@PathVariable("agencia") String agencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana) {
        CobranzaPWA cobranzaPwa = new CobranzaPWA();

        cobranzaPwa.setCobranza(PWAUtil.getPrestamoCobranzaPwasFromPrestamoModelsAndPagoModels(agencia, anio, semana));

        return new ResponseEntity<>(cobranzaPwa, HttpStatus.OK);
    }

    @GetMapping("/semana_actual")
    public ResponseEntity<CalendarioModel> getSemanaActual() {
        String fechaActual = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        CalendarioModel calendarioModel = CalendarioService.getSemanaActualXpressByFechaActual(fechaActual);

        return new ResponseEntity<>(calendarioModel, HttpStatus.OK);
    }

    @GetMapping("/historico/{agencia}/{anio}/{semana}")
    public ResponseEntity<HistoricoPWA> getHistorico(@PathVariable("agencia") String agencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana) {
        HistoricoPWA historicoPWA = new HistoricoPWA();
        historicoPWA.setHistorico(PWAUtil.getPagoHistoricoPWAsFromPagoVistaModels(agencia, anio, semana));

        if (historicoPWA.getHistorico().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(historicoPWA, HttpStatus.OK);
    }

    @GetMapping(path = "/dashboard-fecha/{agencia}/{dia}-{mes}-{anio}")
    public @ResponseBody ResponseEntity<Dashboard> getDashboardByAgencia(@PathVariable("agencia") String agencia, @PathVariable("dia") String dia, @PathVariable("mes") String mes, @PathVariable("anio") String anio) {
        Dashboard dashboard = new Dashboard();
        ObjectsContainer objectsContainer = new ObjectsContainer();

        dashboard.setAgencia(agencia);

        if(dia.length() == 1){
            dia = "0" + dia;
        }

        if(mes.length() == 1){
            mes = "0" + mes;
        }

        objectsContainer.setDashboard(dashboard);
        objectsContainer.setFechaPago(anio + "-" + mes + "-" + dia);

        try{
            objectsContainer.setDia(new SimpleDateFormat("EEEE").format(new SimpleDateFormat("yyyy-MM-dd").parse(objectsContainer.getFechaPago())));
        }catch (ParseException e){

        }

        DashboardPorDiaUtil dashboardPorDiaUtil = new DashboardPorDiaUtil(repositoriesContainer, objectsContainer);

        dashboardPorDiaUtil.run();

        return new ResponseEntity<>(objectsContainer.getDashboard(), HttpStatus.OK);
    }
}

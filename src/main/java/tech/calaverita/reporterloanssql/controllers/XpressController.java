package tech.calaverita.reporterloanssql.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.helpers.CobranzaUtil;
import tech.calaverita.reporterloanssql.helpers.DashboardUtil;
import tech.calaverita.reporterloanssql.helpers.XpressUtil;
import tech.calaverita.reporterloanssql.models.UsuarioModel;
import tech.calaverita.reporterloanssql.pojos.Cobranza;
import tech.calaverita.reporterloanssql.pojos.Dashboard;
import tech.calaverita.reporterloanssql.pojos.LoginResponse;
import tech.calaverita.reporterloanssql.pojos.ObjectsContainer;
import tech.calaverita.reporterloanssql.security.AuthCredentials;
import tech.calaverita.reporterloanssql.services.AgenciaService;
import tech.calaverita.reporterloanssql.services.UsuarioService;

import java.util.ArrayList;

@RestController()
@RequestMapping(path = "/xpress/v1")
public class XpressController {
    @PostMapping(path = "/login")
    public @ResponseBody ResponseEntity<?> login(@RequestBody AuthCredentials login) {
        UsuarioModel usuarioModel = UsuarioService.findOneByUsuarioAndPin(login.getUsername(), login.getPassword());
        LoginResponse loginResponse = new LoginResponse();

        if (usuarioModel == null) {
            return new ResponseEntity<>("Usuario y/o contraseña incorrecto", HttpStatus.BAD_REQUEST);
        }

        loginResponse.setSolicitante(usuarioModel);

        if (usuarioModel.getTipo().equals("Agente")) {
            XpressUtil.setInvolucradosLoginResponse(loginResponse);
        }

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @GetMapping(path = "/cobranza/{agencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<Cobranza> getCobranzaByAgencia(@PathVariable("agencia") String agencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana) {
        Cobranza cobranza = new Cobranza();
        ObjectsContainer objectsContainer = new ObjectsContainer();

        objectsContainer.setCobranza(cobranza);
        objectsContainer.getCobranza().setAgencia(agencia);
        objectsContainer.getCobranza().setAnio(anio);
        objectsContainer.getCobranza().setSemana(semana);

        CobranzaUtil cobranzaUtil = new CobranzaUtil(objectsContainer);

        cobranzaUtil.run();

        return new ResponseEntity<>(objectsContainer.getCobranza(), HttpStatus.OK);
    }

    @GetMapping(path = "/cobranza-gerencia/{gerencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<Cobranza[]> getCobranzaByGerencia(@PathVariable("gerencia") String gerencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana) {
        ArrayList<String> agencias = AgenciaService.getAgenciasByGerencia(gerencia);

        Thread[] threads = new Thread[agencias.size()];
        Cobranza[] cobranzas = new Cobranza[agencias.size()];
        ObjectsContainer[] objectsContainers = new ObjectsContainer[agencias.size()];

        for (int i = 0; i < agencias.size(); i++) {
            cobranzas[i] = new Cobranza();
            objectsContainers[i] = new ObjectsContainer();

            objectsContainers[i].setCobranza(cobranzas[i]);
            objectsContainers[i].getCobranza().setAgencia(agencias.get(i));
            objectsContainers[i].getCobranza().setAnio(anio);
            objectsContainers[i].getCobranza().setSemana(semana);

            threads[i] = new Thread(new CobranzaUtil(objectsContainers[i]));
        }

        for (int i = 0; i < agencias.size(); i++) {
            threads[i].start();
        }

        for (int i = 0; i < agencias.size(); i++) {
            while (threads[i].isAlive()) {
            }
        }

        return new ResponseEntity<>(cobranzas, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(path = "/dashboard-agencia/{agencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<Dashboard> getDashboardByAgencia(@PathVariable("agencia") String agencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana) {
        Dashboard dashboard = new Dashboard();
        ObjectsContainer objectsContainer = new ObjectsContainer();

        objectsContainer.setDashboard(dashboard);
        objectsContainer.getDashboard().setAgencia(agencia);
        objectsContainer.getDashboard().setAnio(anio);
        objectsContainer.getDashboard().setSemana(semana);

        DashboardUtil dashboardUtil = new DashboardUtil(objectsContainer);

        dashboardUtil.run();

        return new ResponseEntity<>(objectsContainer.getDashboard(), HttpStatus.OK);
    }

    @GetMapping(path = "/dashboard-gerencia/{gerencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<Dashboard[]> getDashboardByGerencia(@PathVariable("gerencia") String gerencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana) {
        ArrayList<String> agencias = AgenciaService.getAgenciasByGerencia(gerencia);

        Thread[] threads = new Thread[agencias.size()];
        Dashboard[] dashboards = new Dashboard[agencias.size()];
        ObjectsContainer[] objectsContainers = new ObjectsContainer[agencias.size()];

        for (int i = 0; i < agencias.size(); i++) {
            dashboards[i] = new Dashboard();
            objectsContainers[i] = new ObjectsContainer();

            objectsContainers[i].setDashboard(dashboards[i]);
            objectsContainers[i].getDashboard().setAgencia(agencias.get(i));
            objectsContainers[i].getDashboard().setAnio(anio);
            objectsContainers[i].getDashboard().setSemana(semana);

            threads[i] = new Thread(new DashboardUtil(objectsContainers[i]));
        }

        for (int i = 0; i < agencias.size(); i++) {
            threads[i].start();

            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        for (int i = 0; i < agencias.size(); i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return new ResponseEntity<>(dashboards, HttpStatus.OK);
    }
}
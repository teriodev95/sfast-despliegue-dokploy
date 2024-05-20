package tech.calaverita.reporterloanssql.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.Constants;
import tech.calaverita.reporterloanssql.models.mariaDB.UsuarioModel;
import tech.calaverita.reporterloanssql.pojos.Cobranza;
import tech.calaverita.reporterloanssql.pojos.Dashboard;
import tech.calaverita.reporterloanssql.pojos.LoginResponse;
import tech.calaverita.reporterloanssql.pojos.ObjectsContainer;
import tech.calaverita.reporterloanssql.security.AuthCredentials;
import tech.calaverita.reporterloanssql.services.AgenciaService;
import tech.calaverita.reporterloanssql.services.UsuarioService;
import tech.calaverita.reporterloanssql.utils.CobranzaUtil;
import tech.calaverita.reporterloanssql.utils.DashboardUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

@RestController()
@RequestMapping(path = "/xpress/v1")
public final class XpressController {
    private final AgenciaService agenciaService;
    private final UsuarioService usuarioService;

    public XpressController(AgenciaService agenciaService, UsuarioService usuarioService) {
        this.agenciaService = agenciaService;
        this.usuarioService = usuarioService;
    }

    @ModelAttribute
    public void setResponseHeader(HttpServletResponse response) {
        response.setHeader("Version", Constants.VERSION);
        response.setHeader("Last-Modified", Constants.LAST_MODIFIED);
    }

    @PostMapping(path = "/login")
    public @ResponseBody ResponseEntity<?> login(@RequestBody AuthCredentials login) {
        UsuarioModel usuarioModel = this.usuarioService.findByUsuarioAndPin(login.getUsername(), login.getPassword());
        LoginResponse loginResponse = new LoginResponse();

        if (usuarioModel == null) {
            return new ResponseEntity<>("Usuario y/o contraseña incorrecto", HttpStatus.BAD_REQUEST);
        }

        loginResponse.setSolicitante(usuarioModel);
        loginResponse.setInvolucrados(this.usuarioService.findByGerencia(usuarioModel.getGerencia()));

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @GetMapping(path = "/cobranza/{agencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<Cobranza> getCobranzaByAgencia(@PathVariable String agencia,
                                                                       @PathVariable int anio,
                                                                       @PathVariable int semana) {
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
    public @ResponseBody ResponseEntity<Cobranza[]> getCobranzaByGerencia(@PathVariable String gerencia,
                                                                          @PathVariable int anio,
                                                                          @PathVariable int semana) {
        ArrayList<String> agencias = this.agenciaService.findIdsByGerenciaId(gerencia);

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
            threads[i].setPriority(3);
        }

        for (int i = 0; i < agencias.size(); i++) {
            threads[i].start();
        }

        for (int i = 0; i < agencias.size(); i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return new ResponseEntity<>(cobranzas, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(path = "/dashboard-agencia/{agencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<Dashboard> getDashboardByAgenciaAnioAndSemana(@PathVariable String agencia,
                                                                                      @PathVariable int anio,
                                                                                      @PathVariable int semana) {
        Dashboard dashboard = new tech.calaverita.reporterloanssql.pojos.Dashboard();
        dashboard.setStatusAgencia(this.agenciaService.findStatusById(agencia));
        ObjectsContainer objectsContainer = new ObjectsContainer();

        objectsContainer.setDashboard(dashboard);
        objectsContainer.getDashboard().setAgencia(agencia);
        objectsContainer.getDashboard().setAnio(anio);
        objectsContainer.getDashboard().setSemana(semana);

        DashboardUtil dashboardUtil = new DashboardUtil(objectsContainer);

        dashboardUtil.run();

        return new ResponseEntity<>(objectsContainer.getDashboard(), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(path = "/dashboard-gerencia/{gerencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<Dashboard> getDashboardByGerencia(@PathVariable String gerencia,
                                                                          @PathVariable int anio,
                                                                          @PathVariable int semana) {
        tech.calaverita.reporterloanssql.pojos.Dashboard dashboardResponse;

        ArrayList<String> agencias = this.agenciaService.findIdsByGerenciaId(gerencia);

        Thread[] threads = new Thread[agencias.size()];
        tech.calaverita.reporterloanssql.pojos.Dashboard[] dashboards = new tech.calaverita.reporterloanssql.pojos
                .Dashboard[agencias.size()];
        ObjectsContainer[] objectsContainers = new ObjectsContainer[agencias.size()];

        for (int i = 0; i < agencias.size(); i++) {
            dashboards[i] = new tech.calaverita.reporterloanssql.pojos.Dashboard();
            objectsContainers[i] = new ObjectsContainer();

            objectsContainers[i].setDashboard(dashboards[i]);
            objectsContainers[i].getDashboard().setAgencia(agencias.get(i));
            objectsContainers[i].getDashboard().setGerencia(gerencia);
            objectsContainers[i].getDashboard().setAnio(anio);
            objectsContainers[i].getDashboard().setSemana(semana);

            threads[i] = new Thread(new DashboardUtil(objectsContainers[i]));
            threads[i].setPriority(3);
        }

        for (int i = 0; i < agencias.size(); i++) {
            threads[i].start();
        }

        for (int i = 0; i < agencias.size(); i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        dashboardResponse = DashboardUtil.dashboard(dashboards);

        return new ResponseEntity<>(dashboardResponse, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/local_date_time")
    public ResponseEntity<HashMap<String, String>> getLocalDateTime() {
        String fechaYHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String[] campos = fechaYHora.split(" ");

        HashMap<String, String> fechaYHoraHM = new HashMap<>();
        fechaYHoraHM.put("Fecha", campos[0]);
        fechaYHoraHM.put("Hora", campos[1]);

        return new ResponseEntity<>(fechaYHoraHM, HttpStatus.OK);
    }
}
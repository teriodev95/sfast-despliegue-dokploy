package tech.calaverita.reporterloanssql.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.persistence.dto.reporte_diario_agencias.*;
import tech.calaverita.reporterloanssql.persistence.dto.reporte_general_gerencia.AvanceReporteGeneralGerencia;
import tech.calaverita.reporterloanssql.persistence.dto.reporte_general_gerencia.DashboardReporteGeneralGerencia;
import tech.calaverita.reporterloanssql.persistence.dto.reporte_general_gerencia.EncabezadoReporteGeneralGerencia;
import tech.calaverita.reporterloanssql.persistence.dto.reporte_general_gerencia.ReporteGeneralGerencia;
import tech.calaverita.reporterloanssql.persistence.entities.UsuarioEntity;
import tech.calaverita.reporterloanssql.pojos.Cobranza;
import tech.calaverita.reporterloanssql.pojos.Dashboard;
import tech.calaverita.reporterloanssql.pojos.LoginResponse;
import tech.calaverita.reporterloanssql.pojos.ObjectsContainer;
import tech.calaverita.reporterloanssql.security.AuthCredentials;
import tech.calaverita.reporterloanssql.services.AgenciaService;
import tech.calaverita.reporterloanssql.services.UsuarioService;
import tech.calaverita.reporterloanssql.utils.CobranzaUtil;
import tech.calaverita.reporterloanssql.utils.DashboardUtil;

import java.util.ArrayList;

@RestController()
@RequestMapping(path = "/xpress/v1")
public final class XpressController {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private final AgenciaService agencServ;
    private final UsuarioService usuarServ;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    private XpressController(
            AgenciaService agencServ_S,
            UsuarioService usuarServ_S
    ) {
        this.agencServ = agencServ_S;
        this.usuarServ = usuarServ_S;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @PostMapping(path = "/login")
    public @ResponseBody ResponseEntity<?> login(
            @RequestBody AuthCredentials login
    ) {
        UsuarioEntity usuarioEntity = this.usuarServ.usuarModFindByUsuarioAndPin(login.getUsername(), login.getPassword());
        LoginResponse loginResponse = new LoginResponse();

        if (usuarioEntity == null) {
            return new ResponseEntity<>("Usuario y/o contraseña incorrecto", HttpStatus.BAD_REQUEST);
        }

        loginResponse.setSolicitante(usuarioEntity);
        loginResponse.setInvolucrados(this.usuarServ.darrUsuarModFindByGerencia(usuarioEntity.getGerencia()));

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @GetMapping(path = "/cobranza/{agencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<Cobranza> getCobranzaByAgencia(
            @PathVariable("agencia") String agencia,
            @PathVariable("anio") int anio,
            @PathVariable("semana") int semana
    ) {
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

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @GetMapping(path = "/cobranza-gerencia/{gerencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<Cobranza[]> getCobranzaByGerencia(
            @PathVariable("gerencia") String gerencia, @PathVariable("anio") int anio,
            @PathVariable("semana") int semana
    ) {
        ArrayList<String> agencias = this.agencServ.darrstrAgenciaIdFindByGerenciaId(gerencia);

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

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @CrossOrigin
    @GetMapping(path = "/dashboard-agencia/{agencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<tech.calaverita.reporterloanssql.pojos.Dashboard> getDashboardByAgenciaAnioAndSemana(
            @PathVariable("agencia") String agencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana
    ) {
        tech.calaverita.reporterloanssql.pojos.Dashboard dashboard = new tech.calaverita.reporterloanssql.pojos.Dashboard();
        ObjectsContainer objectsContainer = new ObjectsContainer();

        objectsContainer.setDashboard(dashboard);
        objectsContainer.getDashboard().setAgencia(agencia);
        objectsContainer.getDashboard().setAnio(anio);
        objectsContainer.getDashboard().setSemana(semana);

        DashboardUtil dashboardUtil = new DashboardUtil(objectsContainer);

        dashboardUtil.run();

        return new ResponseEntity<>(objectsContainer.getDashboard(), HttpStatus.OK);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @CrossOrigin
    @GetMapping(path = "/dashboard-gerencia/{gerencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<tech.calaverita.reporterloanssql.pojos.Dashboard> getDashboardByGerencia(
            @PathVariable("gerencia") String gerencia, @PathVariable("anio") int anio,
            @PathVariable("semana") int semana
    ) {
        tech.calaverita.reporterloanssql.pojos.Dashboard dashboardResponse;

        ArrayList<String> agencias = this.agencServ.darrstrAgenciaIdFindByGerenciaId(gerencia);

        Thread[] threads = new Thread[agencias.size()];
        tech.calaverita.reporterloanssql.pojos.Dashboard[] dashboards = new tech.calaverita.reporterloanssql.pojos.Dashboard[agencias.size()];
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

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @CrossOrigin
    @GetMapping(path = "/reporte_diario_agencias")
    public @ResponseBody ResponseEntity<ReporteDiarioAgencias> getReporteDiarioAgencias() {
        ReporteDiarioAgencias reporteDiarioAgencias = new ReporteDiarioAgencias();
        {
            reporteDiarioAgencias.setEncabezado(new EncabezadoReporteDiarioAgencias());

            ArrayList<AgenciaReporteDiarioAgencias> agencias = new ArrayList<>();
            {
                AgenciaReporteDiarioAgencias agencia = new AgenciaReporteDiarioAgencias();
                {
                    agencia.setSemanaActual(new DashboardSemanaActualReporteDiarioAgencias());
                    agencia.setSemanaAnterior(new DashboardSemanaAnteriorReporteDiarioAgencias());
                }
                agencias.add(agencia);
            }
            reporteDiarioAgencias.setAgencias(agencias);
        }

        return new ResponseEntity<>(reporteDiarioAgencias, HttpStatus.OK);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @CrossOrigin
    @GetMapping(path = "/reporte_general_gerencia")
    public @ResponseBody ResponseEntity<ReporteGeneralGerencia> getReporteGeneralGerencia() {
        ReporteGeneralGerencia reporteGeneralGerencia = new ReporteGeneralGerencia();
        {
            reporteGeneralGerencia.setEncabezado(new EncabezadoReporteGeneralGerencia());

            ArrayList<DashboardReporteGeneralGerencia> dashboards = new ArrayList<>();
            {
                DashboardReporteGeneralGerencia dashboard = new DashboardReporteGeneralGerencia();
                dashboards.add(dashboard);
            }
            reporteGeneralGerencia.setDashboards(dashboards);

            ArrayList<AvanceReporteGeneralGerencia> avances = new ArrayList<>();
            {
                AvanceReporteGeneralGerencia avance = new AvanceReporteGeneralGerencia();
                avances.add(avance);
            }
            reporteGeneralGerencia.setAvances(avances);
        }

        return new ResponseEntity<>(reporteGeneralGerencia, HttpStatus.OK);
    }
}
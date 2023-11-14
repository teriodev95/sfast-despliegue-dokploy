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
            EncabezadoReporteDiarioAgencias encabezado = new EncabezadoReporteDiarioAgencias();
            {
                encabezado.setSucursal("EFECTIVO XPRESS");
                encabezado.setZona("EZ06");
                encabezado.setGerente("MARIO ALFREDO CONDE SANTOYA");
                encabezado.setSeguridad("PABLUSSHA GUEVARA MENDEZ");
                encabezado.setFecha("viernes, 10 de noviembre de 2023");
                encabezado.setSemana("VIERNES del SEM. 45 2023 al SEM. 44 2023");
                encabezado.setHora("8:14");
            }
            reporteDiarioAgencias.setEncabezado(encabezado);

            ArrayList<AgenciaReporteDiarioAgencias> agencias = new ArrayList<>();
            {
                AgenciaReporteDiarioAgencias agencia = new AgenciaReporteDiarioAgencias();
                {
                    agencia.setNombre("AG73");
                    agencia.setAgente("GUILLERMINA");

                    DashboardSemanaActualReporteDiarioAgencias semanaActual =
                            new DashboardSemanaActualReporteDiarioAgencias();
                    {
                        semanaActual.setDebitoTotal(42597.00);
                        semanaActual.setCobranzaPura(38631.00);
                        semanaActual.setPorcentajeCobranzaPura(90.69);
                        semanaActual.setFaltante(3966.00);
                        semanaActual.setDiferenciaFaltanteActualVsFaltanteAnterior(366.00);
                        semanaActual.setDiferenciaAcumuladaFaltantes(366.00);
                        semanaActual.setClientesTotalesCobrados(73);
                        semanaActual.setClientesCobradosMiercoles(50);
                        semanaActual.setClientesCobradosJueves(23);
                        semanaActual.setClientesTotales(80);
                        semanaActual.setCobranzaTotal(39193.00);
                        semanaActual.setExcedente(562.00);
                        semanaActual.setVentasTotales(5);
                        semanaActual.setTotalVentas(40000.00);
                        semanaActual.setEfectivoActualAgente(0.00);
                        semanaActual.setIsAgenciaCerrada(true);
                    }
                    agencia.setSemanaActual(semanaActual);

                    DashboardSemanaAnteriorReporteDiarioAgencias semanaAnterior =
                            new DashboardSemanaAnteriorReporteDiarioAgencias();
                    {
                        semanaAnterior.setDebitoTotal(44435.00);
                        semanaAnterior.setCobranzaPura(40103.00);
                        semanaAnterior.setPorcentajeCobranzaPura(90.25);
                        semanaAnterior.setFaltante(4332.00);
                        semanaAnterior.setDiferenciaAcumuladaFaltantes(0.00);
                        semanaAnterior.setClientesTotalesCobrados(72);
                        semanaAnterior.setClientesCobradosMiercoles(50);
                        semanaAnterior.setClientesCobradosJueves(22);
                        semanaAnterior.setClientesTotales(82);
                        semanaAnterior.setCobranzaTotal(41962.00);
                        semanaAnterior.setExcedente(1859.00);
                        semanaAnterior.setTotalVentas(10000.00);
                    }
                    agencia.setSemanaAnterior(semanaAnterior);

                    agencias.add(agencia);
                    agencias.add(agencia);
                    agencias.add(agencia);
                    agencias.add(agencia);
                    agencias.add(agencia);
                    agencias.add(agencia);
                    agencias.add(agencia);
                    agencias.add(agencia);
                    agencias.add(agencia);
                    agencias.add(agencia);
                    agencias.add(agencia);
                    agencias.add(agencia);
                }
                reporteDiarioAgencias.setAgencias(agencias);
            }
        }

        return new ResponseEntity<>(reporteDiarioAgencias, HttpStatus.OK);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @CrossOrigin
    @GetMapping(path = "/reporte_general_gerencia")
    public @ResponseBody ResponseEntity<ReporteGeneralGerencia> getReporteGeneralGerencia() {
        ReporteGeneralGerencia reporteGeneralGerencia = new ReporteGeneralGerencia();
        {
            EncabezadoReporteGeneralGerencia encabezado = new EncabezadoReporteGeneralGerencia();
            {
                encabezado.setSucursal("EFECTIVO XPRESS");
                encabezado.setZona("EZ06");
                encabezado.setGerente("MARIO ALFREDO CONDE SANTOS");
                encabezado.setSeguridad("PABLUSSHA GUEVARA MENDEZ");
                encabezado.setFecha("viernes, 10 de noviembre de 2023");
                encabezado.setSemana(45);
                encabezado.setHora("8:14");
            }
            reporteGeneralGerencia.setEncabezado(encabezado);

            ArrayList<DashboardReporteGeneralGerencia> dashboards = new ArrayList<>();
            {
                DashboardReporteGeneralGerencia dashboard = new DashboardReporteGeneralGerencia();

                dashboard.setConcepto("SEM. ACTUAL");
                dashboard.setDebitoTotal(239561.00);
                dashboard.setClientesTotalesCobrados(369);
                dashboard.setClientesCobradosMiercoles(260);
                dashboard.setClientesCobradosJueves(120);
                dashboard.setClientesTotales(380);
                dashboard.setCobranzaPura(178101.00);
                dashboard.setDiferenciaCobranzaPuraVsDebitoTotal(61460.00);
                dashboard.setPorcentajeCobranzaPura(74.34);
                dashboard.setDiferenciaActualVsDiferenciaAnterior(735.00);
                dashboard.setCobranzaTotal(178.762);
                dashboard.setExcedente(661.00);
                dashboard.setTotalVentas(120500.00);

                dashboards.add(dashboard);
                dashboards.add(dashboard);
                dashboards.add(dashboard);
                dashboards.add(dashboard);
                dashboards.add(dashboard);
            }
            reporteGeneralGerencia.setDashboards(dashboards);

            ArrayList<AvanceReporteGeneralGerencia> avances = new ArrayList<>();
            {
                AvanceReporteGeneralGerencia avance = new AvanceReporteGeneralGerencia();
                avance.setConcepto("SEM. ACTUAL");
                avance.setDebitoTotal(240239.00);
                avance.setPorcentajeCobranzaPura(74.14);

                avances.add(avance);
                avances.add(avance);
                avances.add(avance);
                avances.add(avance);
                avances.add(avance);
            }
            reporteGeneralGerencia.setAvances(avances);

            reporteGeneralGerencia.setPerdidaAcumulada(343.00);
            reporteGeneralGerencia.setEfectivoGerente(34127.00);
            reporteGeneralGerencia.setEfectivoCampo(24207.00);
            reporteGeneralGerencia.setTotalEfectivo(58334.24);
        }

        return new ResponseEntity<>(reporteGeneralGerencia, HttpStatus.OK);
    }
}
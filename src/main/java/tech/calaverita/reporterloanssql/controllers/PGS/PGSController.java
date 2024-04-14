package tech.calaverita.reporterloanssql.controllers.PGS;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.Constants;
import tech.calaverita.reporterloanssql.models.mariaDB.AsignacionModel;
import tech.calaverita.reporterloanssql.models.mariaDB.CalendarioModel;
import tech.calaverita.reporterloanssql.pojos.Dashboard;
import tech.calaverita.reporterloanssql.pojos.ObjectsContainer;
import tech.calaverita.reporterloanssql.pojos.PWA.CobranzaPWA;
import tech.calaverita.reporterloanssql.pojos.PWA.HistoricoPWA;
import tech.calaverita.reporterloanssql.services.AgenciaService;
import tech.calaverita.reporterloanssql.services.AsignacionService;
import tech.calaverita.reporterloanssql.services.CalendarioService;
import tech.calaverita.reporterloanssql.utils.DashboardPorDiaUtil;
import tech.calaverita.reporterloanssql.utils.pwa.PWAUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@CrossOrigin
@RestController
@RequestMapping("/xpress/v1/pwa")
public final class PGSController {
    private final AsignacionService asignacionService;
    private final CalendarioService calendarioService;
    private final AgenciaService agenciaService;

    public PGSController(AsignacionService asignacionService, CalendarioService calendarioService,
                         AgenciaService agenciaService) {
        this.asignacionService = asignacionService;
        this.calendarioService = calendarioService;
        this.agenciaService = agenciaService;
    }

    @ModelAttribute
    public void setResponseHeader(HttpServletResponse response) {
        response.setHeader("Version", Constants.VERSION);
        response.setHeader("Last-Modified", Constants.LAST_MODIFIED);
    }

    @GetMapping(path = "/cobranza/{agencia}/{anio}/{semana}")
    public ResponseEntity<CobranzaPWA> getCobranzaByAgenciaAnioAndSemana(@PathVariable String agencia,
                                                                         @PathVariable int anio,
                                                                         @PathVariable int semana) {
        CobranzaPWA cobranzaPwa = new CobranzaPWA();

        cobranzaPwa.setCobranza(PWAUtil.darrprestamoCobranzaPwaFromPrestamoModelsAndPagoModels(agencia, anio, semana));

        return new ResponseEntity<>(cobranzaPwa, HttpStatus.OK);
    }

    @GetMapping(path = "/semana_actual")
    public ResponseEntity<CalendarioModel> getSemanaActual() {
        String fechaActual = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        CalendarioModel calendarioModel = this.calendarioService.findByFechaActual(fechaActual);

        return new ResponseEntity<>(calendarioModel, HttpStatus.OK);
    }

    @GetMapping(path = "/dashboard-fecha/{agencia}/{fecha}")
    public @ResponseBody ResponseEntity<Dashboard> getDashboardByAgencia(@PathVariable String agencia,
                                                                         @PathVariable String fecha) {
        Dashboard dashboard = new Dashboard();
        dashboard.setStatusAgencia(this.agenciaService.findStatusById(agencia));
        ObjectsContainer objectsContainer = new ObjectsContainer();

        dashboard.setAgencia(agencia);

        String[] fechaArray = fecha.split("-");
        String dia = fechaArray[0];
        String mes = fechaArray[1];
        String anio = fechaArray[2];

        if (dia.length() == 1) {
            dia = "0" + dia;
        }

        if (mes.length() == 1) {
            mes = "0" + mes;
        }

        objectsContainer.setDashboard(dashboard);
        objectsContainer.setFechaPago(anio + "-" + mes + "-" + dia);

        try {
            objectsContainer.setDia(new SimpleDateFormat("EEEE")
                    .format(new SimpleDateFormat("yyyy-MM-dd").parse(objectsContainer.getFechaPago())));
        } catch (ParseException e) {

        }

        DashboardPorDiaUtil dashboardPorDiaUtil = new DashboardPorDiaUtil(objectsContainer);

        dashboardPorDiaUtil.run();

        return new ResponseEntity<>(objectsContainer.getDashboard(), HttpStatus.OK);
    }

    @GetMapping(path = "/historico/{prestamoId}")
    public ResponseEntity<HistoricoPWA> getHistorial(@PathVariable String prestamoId) {
        HistoricoPWA historicoPWA = new HistoricoPWA();
        historicoPWA.setHistorico(PWAUtil.darrpagoHistoricoPwaFromPagoVistaModelsByPrestamoId(prestamoId));

        if (historicoPWA.getHistorico().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(historicoPWA, HttpStatus.OK);
    }

    @GetMapping(path = "/{agencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<ArrayList<HashMap<String, Object>>> getAsignacionesByAgenciaAnioAndSemana(
            @PathVariable String agencia, @PathVariable int anio, @PathVariable int semana) {
        ArrayList<AsignacionModel> darrasignEnt = this.asignacionService
                .findByAgenciaAnioAndSemana(agencia, anio, semana);

        if (darrasignEnt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        ArrayList<HashMap<String, Object>> asignacionModelsPWA = PWAUtil.darrdicasignacionModelPwa(darrasignEnt);

        return new ResponseEntity<>(asignacionModelsPWA, HttpStatus.OK);
    }
}

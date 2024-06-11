package tech.calaverita.sfast_xpress.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.sfast_xpress.Constants;
import tech.calaverita.sfast_xpress.models.mariaDB.CalendarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.GerenciaModel;
import tech.calaverita.sfast_xpress.models.mongoDB.ReporteDiarioAgenciasDocument;
import tech.calaverita.sfast_xpress.models.mongoDB.ReporteGeneralGerenciaDocument;
import tech.calaverita.sfast_xpress.services.CalendarioService;
import tech.calaverita.sfast_xpress.services.GerenciaService;
import tech.calaverita.sfast_xpress.services.reportes.ReporteDiarioAgenciasService;
import tech.calaverita.sfast_xpress.services.reportes.ReporteGeneralGerenciaService;
import tech.calaverita.sfast_xpress.utils.reportes.ReporteDiarioAgenciasUtil;
import tech.calaverita.sfast_xpress.utils.reportes.ReporteGeneralGerenciaMigracionUtil;
import tech.calaverita.sfast_xpress.utils.reportes.ReporteGeneralGerenciaUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(path = "/xpress/v1/reportes")
public final class ReporteController {
    private final ReporteDiarioAgenciasService reporteDiarioAgenciasService;
    private final ReporteGeneralGerenciaService reporteGeneralGerenciaService;
    private final GerenciaService gerenciaService;
    private final CalendarioService calendarioService;

    public ReporteController(ReporteDiarioAgenciasService reporteDiarioAgenciasService,
                             ReporteGeneralGerenciaService reporteGeneralGerenciaService,
                             GerenciaService gerenciaService, CalendarioService calendarioService) {
        this.reporteDiarioAgenciasService = reporteDiarioAgenciasService;
        this.reporteGeneralGerenciaService = reporteGeneralGerenciaService;
        this.gerenciaService = gerenciaService;
        this.calendarioService = calendarioService;
    }

    @ModelAttribute
    public void setResponseHeader(HttpServletResponse response) {
        response.setHeader("Version", Constants.VERSION);
        response.setHeader("Last-Modified", Constants.LAST_MODIFIED);
    }

    @CrossOrigin
    @PostMapping(path = "/diario_agencias/create-one/{gerencia}")
    public @ResponseBody ResponseEntity<ReporteDiarioAgenciasDocument> getReporteDiarioAgencias(
            @PathVariable String gerencia) throws ExecutionException, InterruptedException {
        CompletableFuture<GerenciaModel> gerenciaEntity = this.gerenciaService
                .findByIdAsync(gerencia);

        gerenciaEntity.join();

        ReporteDiarioAgenciasDocument reporte = new ReporteDiarioAgenciasDocument();

        if (gerenciaEntity.get() != null) {
            reporte = this.reporteDiarioAgenciasService.insert(ReporteDiarioAgenciasUtil
                    .getReporte(gerenciaEntity.get()));
        }

        return new ResponseEntity<>(reporte, HttpStatus.CREATED);
    }

    @CrossOrigin
    @GetMapping(path = "/general_gerencia/create-one/{gerencia}")
    public @ResponseBody ResponseEntity<ReporteGeneralGerenciaDocument> getReporteGeneralGerencia(
            @PathVariable String gerencia) throws ExecutionException, InterruptedException {
        CompletableFuture<GerenciaModel> gerenciaEntity = this.gerenciaService
                .findByIdAsync(gerencia);

        gerenciaEntity.join();

        ReporteGeneralGerenciaDocument reporte = new ReporteGeneralGerenciaDocument();

        if (gerenciaEntity.get() != null) {
            String id = ReporteGeneralGerenciaUtil.getId(gerenciaEntity.get());

            if (this.reporteGeneralGerenciaService.existsById(id)) {
                this.reporteGeneralGerenciaService.deleteById(id);
            }

            reporte = this.reporteGeneralGerenciaService.insert(ReporteGeneralGerenciaUtil
                    .getReporte(gerenciaEntity.get()));
        }

        return new ResponseEntity<>(reporte, HttpStatus.OK);
    }

    @GetMapping
    public @ResponseBody List<ReporteDiarioAgenciasDocument> createReportesDiariosAgencias()
            throws ExecutionException, InterruptedException {
        ArrayList<ReporteDiarioAgenciasDocument> reportes = new ArrayList<>();
        {
            ArrayList<GerenciaModel> gerenciasEntities = this.gerenciaService.findAll();

            for (GerenciaModel gerenciaModel : gerenciasEntities) {
                reportes.add(ReporteDiarioAgenciasUtil.getReporte(gerenciaModel));
            }
        }

        return this.reporteDiarioAgenciasService.insert(reportes);
    }

    @GetMapping(path = "/rgg/{anio}/{semana}")
    public void createReportesGeneralesGerencia(@PathVariable int anio, @PathVariable int semana)
            throws ExecutionException, InterruptedException {
        CompletableFuture<CalendarioModel> calendarioEntity = this.calendarioService
                .findByAnioAndSemanaAsync(anio, semana);

        calendarioEntity.join();

        ArrayList<String> fechaDiasSemana = ReporteGeneralGerenciaMigracionUtil
                .getFechaDiasSemana(calendarioEntity.get());

        ArrayList<ReporteGeneralGerenciaDocument> reportes = new ArrayList<>();
        {
            ArrayList<GerenciaModel> gerenciasEntities = this.gerenciaService.findAll();

            for (GerenciaModel gerenciaModel : gerenciasEntities) {
                for (String fechaDiaSemana : fechaDiasSemana) {

                    reportes.add(ReporteGeneralGerenciaMigracionUtil.getReporte(gerenciaModel, calendarioEntity.get(),
                            fechaDiaSemana));

                    calendarioEntity = this.calendarioService
                            .findByAnioAndSemanaAsync(anio, semana);

                    calendarioEntity.join();
                }
            }
        }

        this.reporteGeneralGerenciaService.insert(reportes);
    }
}

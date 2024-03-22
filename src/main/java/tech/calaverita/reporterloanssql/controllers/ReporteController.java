package tech.calaverita.reporterloanssql.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.models.mariaDB.CalendarioModel;
import tech.calaverita.reporterloanssql.models.mariaDB.GerenciaModel;
import tech.calaverita.reporterloanssql.models.mongoDB.ReporteDiarioAgenciasDocument;
import tech.calaverita.reporterloanssql.models.mongoDB.ReporteGeneralGerenciaDocument;
import tech.calaverita.reporterloanssql.services.CalendarioService;
import tech.calaverita.reporterloanssql.services.GerenciaService;
import tech.calaverita.reporterloanssql.services.reportes.ReporteDiarioAgenciasService;
import tech.calaverita.reporterloanssql.services.reportes.ReporteGeneralGerenciaService;
import tech.calaverita.reporterloanssql.utils.reportes.ReporteDiarioAgenciasUtil;
import tech.calaverita.reporterloanssql.utils.reportes.ReporteGeneralGerenciaMigracionUtil;
import tech.calaverita.reporterloanssql.utils.reportes.ReporteGeneralGerenciaUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(path = "/xpress/v1/reportes")
public final class ReporteController {
    private final ReporteDiarioAgenciasService reporteDiarioAgenciasService;
    private final ReporteGeneralGerenciaService reporteGeneralGerenciaService;
    private final GerenciaService gerenciaService;
    private final CalendarioService calendarioService;

    private ReporteController(
            ReporteDiarioAgenciasService reporteDiarioAgenciasService,
            ReporteGeneralGerenciaService reporteGeneralGerenciaService,
            GerenciaService gerenciaService,
            CalendarioService calendarioService
    ) {
        this.reporteDiarioAgenciasService = reporteDiarioAgenciasService;
        this.reporteGeneralGerenciaService = reporteGeneralGerenciaService;
        this.gerenciaService = gerenciaService;
        this.calendarioService = calendarioService;
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @CrossOrigin
    @PostMapping(path = "/diario_agencias/create-one/{gerencia}")
    public @ResponseBody ResponseEntity<ReporteDiarioAgenciasDocument> getReporteDiarioAgencias(
            @PathVariable("gerencia") String gerencia
    ) throws ExecutionException, InterruptedException {
        CompletableFuture<Optional<GerenciaModel>> gerenciaEntity = this.gerenciaService
                .findByIdAsync(gerencia);

        gerenciaEntity.join();

        ReporteDiarioAgenciasDocument reporte = new ReporteDiarioAgenciasDocument();

        if (gerenciaEntity.get().isPresent()) {
            reporte = this.reporteDiarioAgenciasService.insert(ReporteDiarioAgenciasUtil
                    .getReporte(gerenciaEntity.get().get()));
        }

        return new ResponseEntity<>(reporte, HttpStatus.CREATED);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @CrossOrigin
    @GetMapping(path = "/general_gerencia/create-one/{gerencia}")
    public @ResponseBody ResponseEntity<ReporteGeneralGerenciaDocument> getReporteGeneralGerencia(
            @PathVariable("gerencia") String gerencia
    ) throws ExecutionException, InterruptedException {
        CompletableFuture<Optional<GerenciaModel>> gerenciaEntity = this.gerenciaService
                .findByIdAsync(gerencia);

        gerenciaEntity.join();

        ReporteGeneralGerenciaDocument reporte = new ReporteGeneralGerenciaDocument();

        if (gerenciaEntity.get().isPresent()) {
            String id = ReporteGeneralGerenciaUtil.getId(gerenciaEntity.get().get());

            if (
                    this.reporteGeneralGerenciaService.existsById(id)
            ) {
                this.reporteGeneralGerenciaService.deleteById(id);
            }

            reporte = this.reporteGeneralGerenciaService.insert(ReporteGeneralGerenciaUtil
                    .getReporte(gerenciaEntity.get().get()));
        }

        return new ResponseEntity<>(reporte, HttpStatus.OK);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @GetMapping
    public @ResponseBody List<ReporteDiarioAgenciasDocument> createReportesDiariosAgencias() throws ExecutionException, InterruptedException, ParseException {
        ArrayList<ReporteDiarioAgenciasDocument> reportes = new ArrayList<>();
        {
            ArrayList<GerenciaModel> gerenciasEntities = this.gerenciaService.darrGerModFindAll();

            for (GerenciaModel gerenciaModel : gerenciasEntities) {
                reportes.add(ReporteDiarioAgenciasUtil.getReporte(gerenciaModel));
            }
        }

        return this.reporteDiarioAgenciasService.insert(reportes);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @GetMapping(path = "/rgg/{anio}/{semana}")
    public void createReportesGeneralesGerencia(
            @PathVariable("anio") int anio,
            @PathVariable("semana") int semana
    ) throws ExecutionException, InterruptedException {
        CompletableFuture<CalendarioModel> calendarioEntity = this.calendarioService
                .findByAnioAndSemanaAsync(anio, semana);

        calendarioEntity.join();

        ArrayList<String> fechaDiasSemana = ReporteGeneralGerenciaMigracionUtil
                .getFechaDiasSemana(calendarioEntity.get());

        ArrayList<ReporteGeneralGerenciaDocument> reportes = new ArrayList<>();
        {
            ArrayList<GerenciaModel> gerenciasEntities = this.gerenciaService.darrGerModFindAll();

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

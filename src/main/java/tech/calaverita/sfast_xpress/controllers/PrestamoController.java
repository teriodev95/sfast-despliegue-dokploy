package tech.calaverita.sfast_xpress.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.sfast_xpress.Constants;
import tech.calaverita.sfast_xpress.models.mariaDB.GerenciaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoViewModel;
import tech.calaverita.sfast_xpress.services.GerenciaService;
import tech.calaverita.sfast_xpress.services.views.PrestamoViewService;
import tech.calaverita.sfast_xpress.utils.PrestamoUtil;

import java.util.ArrayList;

@RestController
@RequestMapping(path = "/xpress/v1/loans")
public final class PrestamoController {
    private final PrestamoViewService prestamoViewService;
    private final GerenciaService gerenciaService;

    public PrestamoController(PrestamoViewService prestamoViewService, GerenciaService gerenciaService) {
        this.prestamoViewService = prestamoViewService;
        this.gerenciaService = gerenciaService;
    }

    @ModelAttribute
    public void setResponseHeader(HttpServletResponse response) {
        response.setHeader("Version", Constants.VERSION);
        response.setHeader("Last-Modified", Constants.LAST_MODIFIED);
    }

    @CrossOrigin
    @GetMapping(path = "/{id}")
    public ResponseEntity<PrestamoViewModel> represtModGetByStrId(@PathVariable String id) {
        PrestamoViewModel prestMod_O = prestamoViewService.prestModFindByPrestamoId(id);

        if (prestMod_O == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        PrestamoUtil.prestamoModelAsignarPorcentajeCobrado(prestMod_O);

        return new ResponseEntity<>(prestMod_O, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(path = "/por_finalizar_by_agencia/{agencia}/{anio}/{semana}")
    public ResponseEntity<PrestamosPorFinalizar> prestamosPorFinalizarByAgencia(@PathVariable String agencia,
                                                                                @PathVariable int anio,
                                                                                @PathVariable int semana) {
        // To easy code
        ArrayList<PrestamoViewModel> prestamoModels = this.prestamoViewService
                .findPorFinalizarByAgenciaAnioAndSemana(agencia, anio, semana);
        PrestamosPorFinalizar prestamosPorFinalizar = new PrestamosPorFinalizar(prestamoModels);

        return new ResponseEntity<>(prestamosPorFinalizar, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(path = "/por_finalizar_by_gerencia/{gerencia}/{anio}/{semana}")
    public ResponseEntity<PrestamosPorFinalizar> prestamosPorFinalizarByGerencia(@PathVariable String gerencia,
                                                                                 @PathVariable int anio,
                                                                                 @PathVariable int semana) {
        // To easy code
        GerenciaModel gerenciaModel = this.gerenciaService.findById(gerencia);
        ArrayList<PrestamoViewModel> prestamoModels = this.prestamoViewService
                .findPorFinalizarByGerenciaAnioAndSemana(gerenciaModel.getSucursal(), gerenciaModel.getDeprecatedName(),
                        anio, semana);
        PrestamosPorFinalizar prestamosPorFinalizar = new PrestamosPorFinalizar(prestamoModels);

        return new ResponseEntity<>(prestamosPorFinalizar, HttpStatus.OK);
    }

    @Data
    public static class PrestamosPorFinalizar {
        private int porFinalizar;
        private ArrayList<PrestamoViewModel> prestamos;

        public PrestamosPorFinalizar() {

        }

        public PrestamosPorFinalizar(ArrayList<PrestamoViewModel> prestamoModels) {
            this.porFinalizar = prestamoModels.size();
            this.prestamos = prestamoModels;
        }
    }
}

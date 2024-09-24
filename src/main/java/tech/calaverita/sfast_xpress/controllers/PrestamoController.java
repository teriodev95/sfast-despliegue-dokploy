package tech.calaverita.sfast_xpress.controllers;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import tech.calaverita.sfast_xpress.Constants;
import tech.calaverita.sfast_xpress.DTOs.PrestamoDTO;
import tech.calaverita.sfast_xpress.models.mariaDB.GerenciaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.PersonaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoViewModel;
import tech.calaverita.sfast_xpress.services.GerenciaService;
import tech.calaverita.sfast_xpress.services.PersonaService;
import tech.calaverita.sfast_xpress.services.views.PrestamoViewService;
import tech.calaverita.sfast_xpress.utils.PersonaUtil;
import tech.calaverita.sfast_xpress.utils.PrestamoUtil;

@RestController
@RequestMapping(path = "/xpress/v1/loans")
public final class PrestamoController {
    private final PrestamoViewService prestamoViewService;
    private final GerenciaService gerenciaService;
    private final PersonaService personaService;

    public PrestamoController(PrestamoViewService prestamoViewService, GerenciaService gerenciaService,
            PersonaService personaService) {
        this.prestamoViewService = prestamoViewService;
        this.gerenciaService = gerenciaService;
        this.personaService = personaService;
    }

    @ModelAttribute
    public void setResponseHeader(HttpServletResponse response) {
        response.setHeader("Version", Constants.VERSION);
        response.setHeader("Last-Modified", Constants.LAST_MODIFIED);
    }

    @CrossOrigin
    @GetMapping(path = "/{id}")
    public ResponseEntity<PrestamoViewModel> represtModGetByStrId(@PathVariable String id) {
        PrestamoViewModel prestMod_O = prestamoViewService.findById(id);

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

    @CrossOrigin
    @GetMapping(path = "/persona/{nombres}/{apellidoPaterno}/{apellidoMaterno}")
    public ResponseEntity<HashMap<String, Object>> prestamosPorFinalizarByCliente(@PathVariable String nombres,
            @PathVariable String apellidoPaterno,
            @PathVariable String apellidoMaterno) {
        nombres = nombres.toUpperCase();
        apellidoPaterno = apellidoPaterno.toUpperCase();
        apellidoMaterno = apellidoMaterno.toUpperCase();

        // To easy code
        int longitudNombres = nombres.length() >= 2 ? 2 : 0;
        int longitudApellidoPaterno = apellidoPaterno.length() >= 2 ? 2 : 0;
        int longitudApellidoMaterno = apellidoMaterno.length() >= 2 ? 2 : 0;
        int indiceFinalNombres = nombres.length();
        int indiceFinalApellidoPaterno = apellidoPaterno.length();
        int indiceFinalApellidoMaterno = apellidoMaterno.length();
        String inicioNombres = nombres.substring(0, longitudNombres);
        String finalNombres = nombres.substring(indiceFinalNombres - longitudNombres, indiceFinalNombres);
        String inicioApellidoPaterno = apellidoPaterno.substring(0, longitudApellidoPaterno);
        String finalApellidoPaterno = apellidoPaterno.substring(indiceFinalApellidoPaterno - longitudApellidoPaterno,
                indiceFinalApellidoPaterno);
        String inicioApellidoMaterno = apellidoMaterno.substring(0, longitudApellidoMaterno);
        String finalApellidoMaterno = apellidoMaterno.substring(indiceFinalApellidoMaterno - longitudApellidoMaterno,
                indiceFinalApellidoMaterno);

        ArrayList<PersonaModel> personaModels = this.personaService
                .findByNombresOrApellidoPaternoOrApellidoMaterno(
                        inicioNombres, finalNombres, inicioApellidoPaterno, finalApellidoPaterno, inicioApellidoMaterno,
                        finalApellidoMaterno);

        HashMap<String, Object> response = new HashMap<>();

        for (PersonaModel personaModel : personaModels) {
            boolean isPersonaIgual = PersonaUtil.comparadorPersonas(personaModel, nombres, apellidoPaterno,
                    apellidoMaterno);
                    
            if (isPersonaIgual) {
                PrestamoViewModel prestamoViewModel = this.prestamoViewService
                        .findByClienteId(personaModel.getXpressId());

                response.put("personaEncontrada", personaModel);
                if (prestamoViewModel != null) {
                    response.put("demasDatosPrestamo", new PrestamoDTO(prestamoViewModel));
                }
                break;
            }
        }

        return new ResponseEntity<>(response, response.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
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

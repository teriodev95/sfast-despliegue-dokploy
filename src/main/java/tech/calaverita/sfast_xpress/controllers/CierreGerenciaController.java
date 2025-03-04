package tech.calaverita.sfast_xpress.controllers;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.calaverita.sfast_xpress.services.CierreGerenciaService;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v2/cierres-gerencias/")
public class CierreGerenciaController {
    private final CierreGerenciaService cierreGerenciaService;

    public CierreGerenciaController(CierreGerenciaService cierreGerenciaService) {
        this.cierreGerenciaService = cierreGerenciaService;
    }

    @PostMapping(path = "/usuarioId/{usuarioId}/gerencia/{gerencia}")
    public ResponseEntity<?> createByUsuarioIdAndGerencia(@PathVariable Integer usuarioId,
            @PathVariable String gerencia) {
        // To easy code
        Object respuesta = this.cierreGerenciaService.createByUsuarioIdGerencia(usuarioId, gerencia);
        if (((HashMap) respuesta).containsValue("Creado exitosamente")) {
            return new ResponseEntity<>(respuesta, HttpStatus.OK);
        }

        return new ResponseEntity<>(respuesta, HttpStatus.CONFLICT);
    }
}

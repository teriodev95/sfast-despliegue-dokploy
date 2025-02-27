package tech.calaverita.sfast_xpress.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.calaverita.sfast_xpress.DTOs.numeros_gerencia.NumerosGerenciaDto;
import tech.calaverita.sfast_xpress.services.NumerosGerenciaService;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v2/numeros-gerencias")
public class NumerosGerenciaController {
    private final NumerosGerenciaService numerosGerenciaService;

    public NumerosGerenciaController(NumerosGerenciaService numerosGerenciaService) {
        this.numerosGerenciaService = numerosGerenciaService;
    }

    @GetMapping(path = "/gerencia/{gerencia}")
    public NumerosGerenciaDto getNumerosGerenciaByGerencia(@PathVariable String gerencia) {
        return this.numerosGerenciaService.getNumerosGerenciaByGerencia(gerencia);
    }
}

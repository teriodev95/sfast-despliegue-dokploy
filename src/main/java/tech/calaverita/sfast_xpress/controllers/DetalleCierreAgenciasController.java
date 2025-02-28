package tech.calaverita.sfast_xpress.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.calaverita.sfast_xpress.DTOs.detalle_cierre_agencias.DetalleCierreAgenciasDto;
import tech.calaverita.sfast_xpress.services.DetalleCierreAgenciasService;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v2/detalles-cierres-agencias")
public class DetalleCierreAgenciasController {
    private final DetalleCierreAgenciasService balanceGeneralService;

    public DetalleCierreAgenciasController(DetalleCierreAgenciasService balanceGeneralService) {
        this.balanceGeneralService = balanceGeneralService;
    }

    @GetMapping(path = "/gerencia/{gerencia}")
    public DetalleCierreAgenciasDto getBalanceGeneralByGerencia(@PathVariable String gerencia) {
        return this.balanceGeneralService.getBalanceGeneralByGerencia(gerencia);
    }
}

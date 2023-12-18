package tech.calaverita.reporterloanssql.controllers;

import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.persistence.dto.LiquidacionDTO;
import tech.calaverita.reporterloanssql.persistence.entities.LiquidacionEntity;

@RestController
@RequestMapping("/xpress/v1/payoffs")
public class LiquidacionController {
    @GetMapping(path = "/data/{prestamoId}")
    public @ResponseBody LiquidacionDTO getDatosLiquidacion(
            @PathVariable(name = "prestamoId") String prestamoId
    ) {
        return null;
    }
}

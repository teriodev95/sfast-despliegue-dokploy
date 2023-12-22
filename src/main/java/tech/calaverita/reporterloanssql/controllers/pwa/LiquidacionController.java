package tech.calaverita.reporterloanssql.controllers.pwa;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.persistence.dto.LiquidacionDTO;
import tech.calaverita.reporterloanssql.persistence.entities.LiquidacionEntity;
import tech.calaverita.reporterloanssql.persistence.entities.PagoEntity;
import tech.calaverita.reporterloanssql.services.LiquidacionService;
import tech.calaverita.reporterloanssql.services.PagoService;
import tech.calaverita.reporterloanssql.utils.LiquidacionUtil;
import tech.calaverita.reporterloanssql.utils.PagoUtil;

@RestController
@RequestMapping("/xpress/v1/pwa/payoffs")
public class LiquidacionController {
    private final LiquidacionService liquidacionService;
    private final PagoService pagoService;

    public LiquidacionController(
            LiquidacionService liquidacionService,
            PagoService pagoService
    ) {
        this.liquidacionService = liquidacionService;
        this.pagoService = pagoService;
    }

    @GetMapping(path = "/data/{prestamoId}")
    public @ResponseBody ResponseEntity<LiquidacionDTO> getDatosLiquidacion(
            @PathVariable(name = "prestamoId") String prestamoId
    ) {
        return new ResponseEntity<>(LiquidacionUtil.getLiquidacionDTO(prestamoId), HttpStatus.OK);
    }

    @PostMapping(path = "/create-one")
    public @ResponseBody ResponseEntity<String> createLiquidacion(
            @RequestBody LiquidacionDTO liquidacionDTO
    ) {
        String responseText = "Se creó la liquidación correctamente";
        HttpStatus responseStatus = HttpStatus.CREATED;

        PagoEntity pagoEntity = PagoUtil.getPagoEntity(liquidacionDTO);
        LiquidacionEntity liquidacionEntity = LiquidacionUtil.getLiquidacionEntity(liquidacionDTO, pagoEntity);

        this.pagoService.save(pagoEntity);
        this.liquidacionService.save(liquidacionEntity);

        return new ResponseEntity<>(responseText, responseStatus);
    }
}

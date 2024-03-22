package tech.calaverita.reporterloanssql.controllers.PGS;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.dto.LiquidacionDTO;
import tech.calaverita.reporterloanssql.models.mariaDB.LiquidacionModel;
import tech.calaverita.reporterloanssql.models.mariaDB.PagoModel;
import tech.calaverita.reporterloanssql.services.LiquidacionService;
import tech.calaverita.reporterloanssql.services.PagoService;
import tech.calaverita.reporterloanssql.utils.LiquidacionUtil;
import tech.calaverita.reporterloanssql.utils.PagoUtil;

@CrossOrigin
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

        PagoModel pagoModel = PagoUtil.getPagoEntity(liquidacionDTO);
        LiquidacionModel liquidacionModel = LiquidacionUtil.getLiquidacionEntity(liquidacionDTO, pagoModel);

        this.pagoService.save(pagoModel);
        this.liquidacionService.save(liquidacionModel);

        return new ResponseEntity<>(responseText, responseStatus);
    }
}

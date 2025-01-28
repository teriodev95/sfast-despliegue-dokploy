package tech.calaverita.sfast_xpress.controllers.PGS;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tech.calaverita.sfast_xpress.models.mariaDB.VentaModel;
import tech.calaverita.sfast_xpress.services.VentaService;

@CrossOrigin
@RestController
@RequestMapping(path = "/xpress/v1/ventas")
public class VentaController {
    private VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @PostMapping(path = "/create")
    public @ResponseBody ResponseEntity<VentaModel> create(@RequestBody VentaModel ventaDTO) {

        VentaModel ventaModel = this.ventaService.save(ventaDTO);

        if (ventaModel == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(ventaModel, HttpStatus.CREATED);
    }

    @GetMapping(path = "/by_gerencia_anio_and_semana/{gerencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<List<VentaModel>> getByGerenciaAnioAndSemana(@PathVariable String gerencia,
            @PathVariable Integer anio,
            @PathVariable Integer semana) {

        List<VentaModel> ventaModels = this.ventaService.findByGerenciaAnioAndSemana(gerencia, anio, semana);

        return new ResponseEntity<>(ventaModels, HttpStatus.OK);
    }
}

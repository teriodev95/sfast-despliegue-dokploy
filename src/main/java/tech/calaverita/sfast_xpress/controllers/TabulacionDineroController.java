package tech.calaverita.sfast_xpress.controllers;

import java.util.HashMap;

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

import tech.calaverita.sfast_xpress.models.TabulacionDineroModel;
import tech.calaverita.sfast_xpress.services.TabulacionDineroService;
import tech.calaverita.sfast_xpress.utils.TabulacionDineroUtil;

@CrossOrigin
@RestController
@RequestMapping(path = "/xpress/v1/pwa/tabulaciones")
public class TabulacionDineroController {
    private TabulacionDineroService tabulacionDineroService;

    public TabulacionDineroController(TabulacionDineroService tabulacionDineroService) {
        this.tabulacionDineroService = tabulacionDineroService;
    }

    @GetMapping(path = "/by_usuario-id_anio_and_semana/{usuarioId}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<Object> getByUsuarioIdAnioAndSemana(@PathVariable Integer usuarioId,
            @PathVariable Integer anio, @PathVariable Integer semana) {
        Object response;

        TabulacionDineroModel tabulacionDineroModel = this.tabulacionDineroService
                .findByUsuarioIdAnioAndSemana(usuarioId, anio, semana);

        if (tabulacionDineroModel != null) {
            response = tabulacionDineroModel;
        } else {
            HashMap<String, String> noContent = new HashMap<>();
            noContent.put("result", "No se encontró un registro para los parámetros especificados.");
            response = noContent;
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/create")
    public @ResponseBody ResponseEntity<Object> create(@RequestBody TabulacionDineroModel tabulacionDineroModel) {
        Object response;

        HashMap<String, Object> resultValidation = TabulacionDineroUtil.validateTabulacionDinero(tabulacionDineroModel);

        if (resultValidation.isEmpty()) {
            response = this.tabulacionDineroService.save(tabulacionDineroModel);
        } else {
            response = resultValidation;
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

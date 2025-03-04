package tech.calaverita.sfast_xpress.controllers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
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

import tech.calaverita.sfast_xpress.models.mariaDB.TabulacionDineroModel;
import tech.calaverita.sfast_xpress.services.TabulacionDineroService;
import tech.calaverita.sfast_xpress.utils.TabulacionDineroUtil;
import org.springframework.web.bind.annotation.PutMapping;


@CrossOrigin
@RestController
@RequestMapping(path = "/xpress/v1/pwa/tabulaciones")
public class TabulacionDineroController {
    private TabulacionDineroService tabulacionDineroService;

    public TabulacionDineroController(TabulacionDineroService tabulacionDineroService) {
        this.tabulacionDineroService = tabulacionDineroService;
    }

    @GetMapping(path = "/gerencia/{gerencia}/anio/{anio}/semana/{semana}")
    public @ResponseBody ResponseEntity<Object> getByGerenciaAnioAndSemana(@PathVariable String gerencia,
            @PathVariable Integer anio, @PathVariable Integer semana) {
        Object response;

        TabulacionDineroModel tabulacionDineroModel = this.tabulacionDineroService
                .findByGerenciaAnioAndSemana(gerencia, anio, semana);

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

    @PutMapping(path = "/update/by_usuario-id_anio_and_semana/{usuarioId}/{anio}/{semana}{id}")
    public @ResponseBody ResponseEntity<Object> updateByUsuarioIdAnioAndSemana(
        @RequestBody TabulacionDineroModel tabulacionDineroModelUpdate,
        @PathVariable Integer usuarioId,
        @PathVariable Integer anio, 
        @PathVariable Integer semana) {
        HttpStatus status = HttpStatus.OK;
        Object response;
        try {
            if (tabulacionDineroModelUpdate == null) {
                status = HttpStatus.BAD_REQUEST;
                throw new Exception("Body vacio");
            }

            TabulacionDineroModel tabulacionDineroFound = this.tabulacionDineroService.findByUsuarioIdAnioAndSemana(usuarioId, anio, semana);
            if (tabulacionDineroFound == null) {
                status = HttpStatus.NO_CONTENT;
                throw new Exception("No se encontro el registro");
            }

            BeanUtils.copyProperties(tabulacionDineroModelUpdate, tabulacionDineroFound, "id", "anio", "semana", "usuarioId");
            response = this.tabulacionDineroService.save(tabulacionDineroFound);
            return new ResponseEntity<>(response, status);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", status,
                "error", "Internal Server Error",
                "message", e.getMessage()
            ));
        }
    }

}

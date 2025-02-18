package tech.calaverita.sfast_xpress.controllers;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.calaverita.sfast_xpress.models.mariaDB.EstadoAgenciaModel;
import tech.calaverita.sfast_xpress.services.AgenciaService;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v2/agencias")
public class AgenciaController {
    private final AgenciaService agenciaService;

    public AgenciaController(AgenciaService agenciaService) {
        this.agenciaService = agenciaService;
    }

    @GetMapping(path = "/agencia/{agenciaId}/estado-agencia")
    public ResponseEntity<Object> getEstadoAgenciaModelByAgencia(@PathVariable String agenciaId) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        HashMap<String, Object> responseHm = new HashMap<>();
        responseHm.put("respuesta", "La agencia ingresada no es válida");
        Object response = responseHm;

        EstadoAgenciaModel estadoAgenciaModel = null;

        if (this.agenciaService.existsById(agenciaId)) {
            estadoAgenciaModel = this.agenciaService.findEstadoAgenciaModelByAgencia(agenciaId);

            if (estadoAgenciaModel != null) {
                httpStatus = HttpStatus.OK;
                response = estadoAgenciaModel;
            }
        }

        return new ResponseEntity<>(response, httpStatus);
    }
}

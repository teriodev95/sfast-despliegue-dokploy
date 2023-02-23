package tech.calaverita.reporterloanssql.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.models.AsignacionModel;
import tech.calaverita.reporterloanssql.models.UsuarioModel;
import tech.calaverita.reporterloanssql.repositories.AsignacionRepository;
import tech.calaverita.reporterloanssql.repositories.UsuarioRepository;

import java.util.Optional;

@RestController
@RequestMapping(path = "/xpress/v1/assignments")
public class AsignacionController {
    @Autowired
    AsignacionRepository asignacionRepository;
    @Autowired
    UsuarioRepository usuarioRepository;

    @GetMapping(path = "/all")
    public @ResponseBody ResponseEntity<Iterable<AsignacionModel>> getAsignaciones() {
        Iterable<AsignacionModel> asignaciones = asignacionRepository.findAll();

        if(!asignaciones.iterator().hasNext())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(asignaciones, HttpStatus.OK);
    }

    @GetMapping(path = "/one/{asignacionId}")
    public @ResponseBody ResponseEntity<Optional<AsignacionModel>> getAsignacionById(@PathVariable("asignacionId") String id) {
        Optional<AsignacionModel> asignacion = asignacionRepository.findById(id);

        if(asignacion.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(asignacion, HttpStatus.OK);
    }

    @PostMapping(path = "/create-one")
    public @ResponseBody ResponseEntity<String> setAsignacion(@RequestBody AsignacionModel asignacion) {

        Optional<UsuarioModel> usuarioModel = usuarioRepository.findById(asignacion.getQuienRecibioUsuarioId());

        if (usuarioModel.isEmpty()) {
            return new ResponseEntity<>("Debe ingresar un quienRecibioUsuarioId válido", HttpStatus.BAD_REQUEST);
        }

        usuarioModel = usuarioRepository.findById(asignacion.getQuienEntregoUsuarioId());

        if (usuarioModel.isEmpty()) {
            return new ResponseEntity<>("Debe ingresar un quienEntregoUsuarioId válido", HttpStatus.BAD_REQUEST);
        }

        if (!asignacion.getLog().contains("{"))
            return new ResponseEntity<>("Debe ingresar un log con formato json", HttpStatus.BAD_REQUEST);

        if (!asignacion.getLog().contains("}"))
            return new ResponseEntity<>("Debe ingresar un log con formato json", HttpStatus.BAD_REQUEST);

        asignacionRepository.save(asignacion);

        return new ResponseEntity<>("Asignación Creada con Éxito", HttpStatus.CREATED);
    }
}

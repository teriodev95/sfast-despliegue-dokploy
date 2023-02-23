package tech.calaverita.reporterloanssql.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Path;
import tech.calaverita.reporterloanssql.models.AsignacionModel;
import tech.calaverita.reporterloanssql.models.UsuarioModel;
import tech.calaverita.reporterloanssql.repositories.AsignacionRepository;
import tech.calaverita.reporterloanssql.repositories.UsuarioRepository;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping(path = "/xpress/v1/assignments")
public class AsignacionController {
    @Autowired
    AsignacionRepository asignacionRepository;
    @Autowired
    UsuarioRepository usuarioRepository;

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<AsignacionModel> getAsignaciones(){

        return asignacionRepository.findAll();
    }

    @GetMapping(path = "/one/{asignacionId}")
    public @ResponseBody Optional<AsignacionModel> getAsignacionById(@PathVariable("asignacionId") String id){

        return asignacionRepository.findById(id);
    }

    @PostMapping(path = "/create-one")
    public @ResponseBody String setAsignacion(@RequestBody AsignacionModel asignacion){

        Optional<UsuarioModel> usuarioModel = usuarioRepository.findById(asignacion.getQuienRecibioUsuarioId());

        if(usuarioModel.isEmpty()) {
            return "Debe ingresar un quienRecibioUsuarioId válido";
        }

        usuarioModel = usuarioRepository.findById(asignacion.getQuienEntregoUsuarioId());

        if(usuarioModel.isEmpty()) {
            return "Debe ingresar un quienEntregoUsuarioId válido";
        }

        if(!asignacion.getLog().contains("{"))
            return "Debe ingresar un log con formato json";

        if(!asignacion.getLog().contains("}"))
            return "Debe ingresar un log con formato json";

        asignacionRepository.save(asignacion);

        return "Asignación Creada con Éxito";
    }
}

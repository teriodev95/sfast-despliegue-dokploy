package tech.calaverita.reporterloanssql.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Path;
import tech.calaverita.reporterloanssql.models.AsignacionModel;
import tech.calaverita.reporterloanssql.repositories.AsignacionRepository;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping(path = "/xpress/v1/assignments")
public class AsignacionController {
    @Autowired
    AsignacionRepository asignacionRepository;

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

        System.out.println(asignacion);

        asignacionRepository.save(asignacion);

        return "Asignación Creada con Éxito";
    }
}

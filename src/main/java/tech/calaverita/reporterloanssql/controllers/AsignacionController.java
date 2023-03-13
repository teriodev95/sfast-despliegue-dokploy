package tech.calaverita.reporterloanssql.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import tech.calaverita.reporterloanssql.models.AsignacionModel;
import tech.calaverita.reporterloanssql.models.UsuarioModel;
import tech.calaverita.reporterloanssql.repositories.AsignacionRepository;
import tech.calaverita.reporterloanssql.repositories.UsuarioRepository;

import java.util.ArrayList;
import java.util.HashMap;
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
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(asignaciones, HttpStatus.OK);
    }

    @GetMapping(path = "/{agencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<Iterable<AsignacionModel>> getAsignacionesByAgenciaAnioAndSemana(@PathVariable("agencia") String agencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana){
        Iterable<AsignacionModel> asignaciones = asignacionRepository.getAsignacionesByAgenciaAnioAndSemana(agencia, anio, semana);

        if(!asignaciones.iterator().hasNext())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(asignaciones, HttpStatus.OK);
    }


    @GetMapping(path = "/one/{asignacionId}")
    public @ResponseBody ResponseEntity<Optional<AsignacionModel>> getAsignacionById(@PathVariable("asignacionId") String id) {
        Optional<AsignacionModel> asignacion = asignacionRepository.findById(id);

        if(asignacion.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(asignacion, HttpStatus.OK);
    }

    @PostMapping(path = "/create-one")
    public @ResponseBody ResponseEntity<String> setAsignacion(@RequestBody AsignacionModel asignacion) {
        Optional<AsignacionModel> asignacionAux = asignacionRepository.findById(asignacion.getAsignacionId());

        if(!asignacionAux.isEmpty()){
            return new ResponseEntity<>("La Asignación Ya Existe", HttpStatus.CONFLICT);
        }

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

    @PostMapping(path = "/create-many")
    public @ResponseBody ResponseEntity<ArrayList<HashMap<String, Object>>> setAsignaciones(@RequestBody ArrayList<AsignacionModel> asignaciones) {
        ArrayList<HashMap<String, Object>> respuesta = new ArrayList<>();

        for(AsignacionModel asignacion: asignaciones){
            HashMap<String, Object> objeto = new HashMap<>();
            String msg = "OK";
            Boolean isOnline = true;

            Optional<AsignacionModel> asignacionAux = asignacionRepository.findById(asignacion.getAsignacionId());

            if(!asignacionAux.isEmpty()){
                msg = "La Asignación Ya Existe";
                isOnline = false;
            }

            Optional<UsuarioModel> usuarioModel = usuarioRepository.findById(asignacion.getQuienRecibioUsuarioId());

            if (usuarioModel.isEmpty()) {
                msg = "Debe ingresar un quienRecibioUsuarioId válido";
                isOnline = false;
            }

            usuarioModel = usuarioRepository.findById(asignacion.getQuienEntregoUsuarioId());

            if (usuarioModel.isEmpty()) {
                msg = "Debe ingresar un quienEntregoUsuarioId válido";
                isOnline = false;
            }

            if (!asignacion.getLog().contains("{")){
                msg = "Debe ingresar un log con formato json";
                isOnline = false;
            }

            if (!asignacion.getLog().contains("}")){
                msg = "Debe ingresar un log con formato json";
                isOnline = false;
            }

            try {
                asignacionRepository.save(asignacion);
            }catch (HttpClientErrorException e){
                msg = e.toString();
                isOnline = false;
            }

            objeto.put("id", asignacion.getAsignacionId());
            objeto.put("isOnline", isOnline);
            objeto.put("msg", msg);
            respuesta.add(objeto);
        }

        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }
}
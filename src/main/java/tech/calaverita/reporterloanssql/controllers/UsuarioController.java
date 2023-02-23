package tech.calaverita.reporterloanssql.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.models.UsuarioModel;
import tech.calaverita.reporterloanssql.repositories.UsuarioRepository;

import java.util.Optional;

@RestController
@RequestMapping(path = "/xpress/v1/users")
public class UsuarioController {
    @Autowired
    UsuarioRepository usuarioRepository;

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<UsuarioModel> getAllUsers(){

        return usuarioRepository.findAll();
    }

    @GetMapping(path = "/one/{usuarioId}")
    public @ResponseBody Optional<UsuarioModel> getOneUser(@PathVariable("usuarioId") Integer usuarioId){

        return usuarioRepository.findById(usuarioId);
    }
}

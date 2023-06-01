package tech.calaverita.reporterloanssql.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public @ResponseBody ResponseEntity<Iterable<UsuarioModel>> getAllUsers() {
        Iterable<UsuarioModel> usuarios = usuarioRepository.findAll();

        if (!usuarios.iterator().hasNext())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @GetMapping(path = "/one/{usuarioId}")
    public @ResponseBody ResponseEntity<Optional<UsuarioModel>> getOneUser(@PathVariable("usuarioId") Integer usuarioId) {
        Optional<UsuarioModel> usuario = usuarioRepository.findById(usuarioId);

        if (usuario.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }
}

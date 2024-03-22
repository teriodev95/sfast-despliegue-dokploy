package tech.calaverita.reporterloanssql.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.models.mariaDB.UsuarioModel;
import tech.calaverita.reporterloanssql.services.UsuarioService;

import java.util.Optional;

@RestController
@RequestMapping(path = "/xpress/v1/users")
public final class UsuarioController {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private final UsuarioService usuarServ;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    private UsuarioController(
            UsuarioService usuarServ_S
    ) {
        this.usuarServ = usuarServ_S;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @GetMapping(path = "/all")
    public @ResponseBody ResponseEntity<Iterable<UsuarioModel>> getAllUsers() {
        Iterable<UsuarioModel> usuarios = this.usuarServ.findAll();

        if (!usuarios.iterator().hasNext())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @GetMapping(path = "/one/{usuarioId}")
    public @ResponseBody ResponseEntity<Optional<UsuarioModel>> getOneUser(
            @PathVariable("usuarioId") Integer usuarioId
    ) {
        Optional<UsuarioModel> usuario = this.usuarServ.optusuarEntFindById(usuarioId);

        if (usuario.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }
}

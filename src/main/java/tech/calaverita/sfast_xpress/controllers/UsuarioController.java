package tech.calaverita.sfast_xpress.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.sfast_xpress.Constants;
import tech.calaverita.sfast_xpress.models.mariaDB.UsuarioModel;
import tech.calaverita.sfast_xpress.services.UsuarioService;

@RestController
@RequestMapping(path = "/xpress/v1/users")
public final class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @ModelAttribute
    public void setResponseHeader(HttpServletResponse response) {
        response.setHeader("Version", Constants.VERSION);
        response.setHeader("Last-Modified", Constants.LAST_MODIFIED);
    }

    @GetMapping(path = "/all")
    public @ResponseBody ResponseEntity<Iterable<UsuarioModel>> getAllUsers() {
        Iterable<UsuarioModel> usuarios = this.usuarioService.findAll();

        if (!usuarios.iterator().hasNext())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @GetMapping(path = "/one/{usuarioId}")
    public @ResponseBody ResponseEntity<UsuarioModel> getOneUser(@PathVariable Integer usuarioId) {
        UsuarioModel usuario = this.usuarioService.findById(usuarioId);

        if (usuario == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }
}

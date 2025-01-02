package tech.calaverita.sfast_xpress.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
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

    @GetMapping(path = "/seguridad_by_gerencia/{gerencia}")
    public @ResponseBody ResponseEntity<UsuarioModel> getByGerenciaInnerJoinUsuarioGerenciaModel(
            @PathVariable String gerencia) {
        UsuarioModel usuarioModel = this.usuarioService.findByGerenciaInnerJoinUsuarioGerenciaModel(gerencia,
                "Seguridad", true);

        if (usuarioModel == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(usuarioModel, HttpStatus.OK);
    }
}

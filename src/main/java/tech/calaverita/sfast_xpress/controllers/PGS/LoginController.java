package tech.calaverita.sfast_xpress.controllers.PGS;

import jakarta.persistence.Tuple;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.sfast_xpress.Constants;
import tech.calaverita.sfast_xpress.models.mariaDB.SucursalModel;
import tech.calaverita.sfast_xpress.models.mariaDB.UsuarioModel;
import tech.calaverita.sfast_xpress.security.AuthCredentials;
import tech.calaverita.sfast_xpress.services.AgenciaService;
import tech.calaverita.sfast_xpress.services.GerenciaService;
import tech.calaverita.sfast_xpress.services.SucursalService;
import tech.calaverita.sfast_xpress.services.UsuarioService;
import tech.calaverita.sfast_xpress.services.relation.UsuarioSucursalService;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@CrossOrigin
@RestController
@RequestMapping("/xpress/v1/pwa")
public final class LoginController {
    private final AgenciaService agenciaService;
    private final GerenciaService gerenciaService;
    private final SucursalService sucursalService;
    private final UsuarioService usuarioService;
    private final UsuarioSucursalService usuarioSucursalService;

    public LoginController(AgenciaService agenciaService, GerenciaService gerenciaService,
                           SucursalService sucursalService, UsuarioService usuarioService,
                           UsuarioSucursalService usuarioSucursalService) {
        this.agenciaService = agenciaService;
        this.gerenciaService = gerenciaService;
        this.sucursalService = sucursalService;
        this.usuarioService = usuarioService;
        this.usuarioSucursalService = usuarioSucursalService;
    }

    @ModelAttribute
    public void setResponseHeader(HttpServletResponse response) {
        response.setHeader("Version", Constants.VERSION);
        response.setHeader("Last-Modified", Constants.LAST_MODIFIED);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> regenericLogin(@RequestBody AuthCredentials authCredentials) {
        UsuarioModel usuarMod_O = this.usuarioService.findByUsuarioAndPin(authCredentials.getUsername(),
                authCredentials.getPassword());

        if (usuarMod_O == null) {
            return new ResponseEntity<>("Usuario y/o contraseña incorrecto", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(usuarMod_O, HttpStatus.OK);
    }

    @GetMapping(path = "/agencias/{gerencia}")
    public ResponseEntity<ArrayList<String>> redarrstrAgenciaIdGetByStrGerencia(@PathVariable String gerencia) {
        CompletableFuture<ArrayList<String>> agenciasCF = this.agenciaService.findIdsByGerenciaIdAsync(gerencia);

        ArrayList<String> agenciasYAgentesHM = new ArrayList<>(agenciasCF.join());

        if (agenciasCF.join() == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(agenciasYAgentesHM, HttpStatus.OK);
    }

    @GetMapping(path = "/agencias")
    public ResponseEntity<ArrayList<LoginController.Agencia>> getAgenciasByGerencia(
            @RequestParam String gerencia) throws ExecutionException, InterruptedException {
        CompletableFuture<ArrayList<String>> agenciasCF = this.agenciaService.findIdsByGerenciaIdAsync(gerencia);
        CompletableFuture<ArrayList<Tuple>> agentesCF = this.usuarioService.findByGerenciaTipoAndStatusAsync(gerencia,
                "Agente", true);

        CompletableFuture.allOf(agenciasCF, agentesCF);

        ArrayList<LoginController.Agencia> agencias = new ArrayList<>();
        for (int i = 0; i < agenciasCF.get().size(); i++) {
            Agencia agencia = new LoginController.Agencia(agenciasCF.get().get(i));

            for (int j = 0; j < agentesCF.get().size(); j++) {
                if (agenciasCF.get().get(i).equals(agentesCF.get().get(j).get("agencia"))) {
                    agencia = new LoginController.Agencia(agenciasCF.get().get(i),
                            agentesCF.get().get(j).get("agente").toString());
                    break;
                }
            }

            agencias.add(agencia);
        }

        if (agenciasCF.get() == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(agencias, HttpStatus.OK);
    }

    @GetMapping(path = "/sucursales/usuarios/{usuario}")
    public ResponseEntity<ArrayList<SucursalModel>> getSucursalIdByUsuario(@PathVariable String usuario) {
        UsuarioModel usuarioModel;
        ArrayList<SucursalModel> sucEntSucursalEntities = new ArrayList<>();

        try {
            usuarioModel = this.usuarioService.findByUsuario(usuario);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        ArrayList<String> darrintSucursalId = this.usuarioSucursalService
                .darrstrSucursalIdFindByUsuarioId(usuarioModel.getUsuarioId());

        for (String strSucursalId : darrintSucursalId) {
            sucEntSucursalEntities.add(this.sucursalService.findBySucursalId(strSucursalId));
        }

        return new ResponseEntity<>(sucEntSucursalEntities, HttpStatus.OK);
    }

    @GetMapping(path = "gerencias/sucursales/{id}")
    public ResponseEntity<ArrayList<String>> getGerenciaIdsBySucursalId(@PathVariable int id) {
        ArrayList<String> darrstrGerencia = this.gerenciaService.findIdsBySucursalId(id);

        if (darrstrGerencia.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(darrstrGerencia, HttpStatus.OK);
    }

    @Data
    public static class Agencia {
        private String agencia;
        private String agente = "SIN AGENTE ASIGNADO";

        public Agencia(String agencia, String agente) {
            this.agencia = agencia;
            this.agente = agente;
        }

        public Agencia(String agencia) {
            this.agencia = agencia;
        }
    }
}

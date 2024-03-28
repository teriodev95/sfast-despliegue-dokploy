package tech.calaverita.reporterloanssql.controllers.PGS;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.models.mariaDB.SucursalModel;
import tech.calaverita.reporterloanssql.models.mariaDB.UsuarioModel;
import tech.calaverita.reporterloanssql.security.AuthCredentials;
import tech.calaverita.reporterloanssql.services.AgenciaService;
import tech.calaverita.reporterloanssql.services.GerenciaService;
import tech.calaverita.reporterloanssql.services.SucursalService;
import tech.calaverita.reporterloanssql.services.UsuarioService;
import tech.calaverita.reporterloanssql.services.relation.UsuarioSucursalService;

import java.util.ArrayList;
import java.util.HashMap;
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

    public LoginController(
            AgenciaService agenciaService,
            GerenciaService gerenciaService,
            SucursalService sucursalService,
            UsuarioService usuarioService,
            UsuarioSucursalService usuarioSucursalService
    ) {
        this.agenciaService = agenciaService;
        this.gerenciaService = gerenciaService;
        this.sucursalService = sucursalService;
        this.usuarioService = usuarioService;
        this.usuarioSucursalService = usuarioSucursalService;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> regenericLogin(@RequestBody AuthCredentials authCredentials) {
        UsuarioModel usuarMod_O = this.usuarioService.findByUsuarioAndPin(authCredentials.getUsername(),
                authCredentials.getPassword());

        if (
                usuarMod_O == null
        ) {
            return new ResponseEntity<>("Usuario y/o contraseña incorrecto", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(usuarMod_O, HttpStatus.OK);
    }

    @GetMapping(path = "/agencias/{gerencia}")
    public ResponseEntity<ArrayList<String>> redarrstrAgenciaIdGetByStrGerencia(
            @PathVariable("gerencia") String gerencia) throws ExecutionException, InterruptedException {
        CompletableFuture<ArrayList<String>> agenciasCF = this.agenciaService.findIdsByGerenciaIdAsync(gerencia);
        CompletableFuture<ArrayList<String>> agentesCF = this.usuarioService.findAgentesByGerenciaAsync(gerencia);

        CompletableFuture.allOf(agenciasCF, agentesCF);

        ArrayList<String> agenciasYAgentesHM = new ArrayList<>();
        for (int i = 0; i < agenciasCF.get().size(); i++) {
            HashMap<String, String> agenciaYAgenteHM = new HashMap<>();
//            agenciaYAgenteHM.put("agencia", agenciasCF.get().get(i));
//            agenciaYAgenteHM.put("agente", agentesCF.get().get(i));
            agenciasYAgentesHM.add(agenciasCF.get().get(i));
        }

        if (
                agenciasCF.get() == null
        ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(agenciasYAgentesHM, HttpStatus.OK);
    }

    @GetMapping(path = "/agencias")
    public ResponseEntity<ArrayList<HashMap<String, String>>> getAgenciasByGerencia(
            @RequestParam String gerencia) throws ExecutionException, InterruptedException {
        CompletableFuture<ArrayList<String>> agenciasCF = this.agenciaService.findIdsByGerenciaIdAsync(gerencia);
        CompletableFuture<ArrayList<String>> agentesCF = this.usuarioService.findAgentesByGerenciaAsync(gerencia);

        CompletableFuture.allOf(agenciasCF, agentesCF);

        ArrayList<HashMap<String, String>> agenciasYAgentesHM = new ArrayList<>();
        for (int i = 0; i < agenciasCF.get().size(); i++) {
            HashMap<String, String> agenciaYAgenteHM = new HashMap<>();
            agenciaYAgenteHM.put("agencia", agenciasCF.get().get(i));
            agenciaYAgenteHM.put("agente", agentesCF.get().get(i));
            agenciasYAgentesHM.add(agenciaYAgenteHM);
        }

        if (
                agenciasCF.get() == null
        ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(agenciasYAgentesHM, HttpStatus.OK);
    }

    @GetMapping(path = "/sucursales/usuarios/{usuario}")
    public ResponseEntity<ArrayList<SucursalModel>> getSucursalIdByUsuario(@PathVariable("usuario") String usuario) {
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
    public ResponseEntity<ArrayList<String>> getGerenciaIdsBySucursalId(@PathVariable("id") int id) {
        ArrayList<String> darrstrGerencia = this.gerenciaService.findIdsBySucursalId(id);

        if (
                darrstrGerencia.isEmpty()
        ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(darrstrGerencia, HttpStatus.OK);
    }
}

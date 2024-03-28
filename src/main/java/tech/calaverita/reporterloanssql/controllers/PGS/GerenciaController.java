package tech.calaverita.reporterloanssql.controllers.PGS;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.models.mariaDB.GerenciaModel;
import tech.calaverita.reporterloanssql.models.mariaDB.SucursalModel;
import tech.calaverita.reporterloanssql.models.mariaDB.UsuarioModel;
import tech.calaverita.reporterloanssql.services.GerenciaService;
import tech.calaverita.reporterloanssql.services.SucursalService;
import tech.calaverita.reporterloanssql.services.UsuarioService;
import tech.calaverita.reporterloanssql.services.relation.UsuarioGerenciaService;
import tech.calaverita.reporterloanssql.services.relation.UsuarioSucursalService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

@CrossOrigin
@RestController
@RequestMapping("/xpress/v1/pwa/gerencias")
public final class GerenciaController {
    private final GerenciaService gerenciaService;
    private final SucursalService sucursalService;
    private final UsuarioService usuarioService;
    private final UsuarioGerenciaService usuarioGerenciaService;
    private final UsuarioSucursalService usuarioSucursalService;

    public GerenciaController(
            GerenciaService gerenciaService,
            SucursalService sucursalService,
            UsuarioService usuarioService,
            UsuarioGerenciaService usuarioGerenciaService,
            UsuarioSucursalService usuarioSucursalService
    ) {
        this.gerenciaService = gerenciaService;
        this.sucursalService = sucursalService;
        this.usuarioService = usuarioService;
        this.usuarioGerenciaService = usuarioGerenciaService;
        this.usuarioSucursalService = usuarioSucursalService;
    }

//    @GetMapping(path = "")
//    public ResponseEntity<ArrayList<GerenciaModel>> redarrgerModGet() {
//        ArrayList<GerenciaModel> darrgerMod_O = this.gerenciaService.findAll();
//
//        if (
//                darrgerMod_O.isEmpty()
//        ) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//
//        return new ResponseEntity<>(darrgerMod_O, HttpStatus.OK);
//    }

    @GetMapping(path = "/{usuario}")
    public ResponseEntity<ArrayList<String>> getGerenciaIdsByUsuario(@PathVariable("usuario") String usuario) {
        UsuarioModel usuarMod;

        try {
            usuarMod = this.usuarioService.findByUsuario(usuario);
        } //
        catch (
                NoSuchElementException e
        ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // To easy code
        ArrayList<String> darrstrGerencia_O = this.usuarioGerenciaService.darrstrGerenciaIdFindByUsuarioId(usuarMod
                .getUsuarioId());

        return new ResponseEntity<>(darrstrGerencia_O, HttpStatus.OK);
    }

    @GetMapping(path = "/usuarios/{usuario}")
    public ResponseEntity<HashMap<String, ArrayList<String>>> redicdarrstrGerenciaIdFromSucursalGetByStrUsuario(
            @PathVariable("usuario") String usuario) {
        UsuarioModel usuarioModel;
        HashMap<String, ArrayList<String>> gerenciasYGerentesBySucursalHM = new HashMap<>();

        try {
            usuarioModel = this.usuarioService.findByUsuario(usuario);
        } //
        catch (
                NoSuchElementException e
        ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        ArrayList<String> sucursalIds = this.usuarioSucursalService
                .darrstrSucursalIdFindByUsuarioId(usuarioModel.getUsuarioId());
        ArrayList<String> gerenciaIds = this.usuarioGerenciaService
                .darrstrGerenciaIdFindByUsuarioId(usuarioModel.getUsuarioId());
        ArrayList<String> gerentes = this.usuarioService
                .findGerentesByGerencias(gerenciaIds);

        for (String sucursalId : sucursalIds) {
            ArrayList<String> gerenciasYGerentesHM = new ArrayList<>();

            for (int i = 0; i < gerenciaIds.size(); i++) {
                if (gerenciaIds.get(i).length() == 7) {
                    if (
                            sucursalId.equals(gerenciaIds.get(i).substring(0, 4))
                    ) {
                        HashMap<String, String> gerenciaYGerenteHM = new HashMap<>();
                        gerenciaYGerenteHM.put("gerencia", gerenciaIds.get(i));
                        gerenciaYGerenteHM.put("gerente", gerentes.get(i));
                        gerenciasYGerentesHM.add(gerenciaIds.get(i));
                    }
                } else if (gerenciaIds.get(i).length() == 8) {
                    if (
                            sucursalId.equals(gerenciaIds.get(i).substring(0, 5))
                    ) {
                        HashMap<String, String> gerenciaYGerenteHM = new HashMap<>();
                        gerenciaYGerenteHM.put("gerencia", gerenciaIds.get(i));
                        gerenciaYGerenteHM.put("gerente", gerentes.get(i));
                        gerenciasYGerentesHM.add(gerenciaIds.get(i));
                    }
                }
            }

            SucursalModel sucursalModel = this.sucursalService.findBySucursalId(sucursalId);
            gerenciasYGerentesBySucursalHM.put(sucursalModel.getNombre(), gerenciasYGerentesHM);
        }

        return new ResponseEntity<>(gerenciasYGerentesBySucursalHM, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<HashMap<String, ArrayList<HashMap<String, String>>>> getGerenciasByUsuario(
            @RequestParam String usuario) {
        UsuarioModel usuarioModel;
        HashMap<String, ArrayList<HashMap<String, String>>> gerenciasYGerentesBySucursalHM = new HashMap<>();

        try {
            usuarioModel = this.usuarioService.findByUsuario(usuario);
        } //
        catch (
                NoSuchElementException e
        ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        ArrayList<String> sucursalIds = this.usuarioSucursalService
                .darrstrSucursalIdFindByUsuarioId(usuarioModel.getUsuarioId());
        ArrayList<String> gerenciaIds = this.usuarioGerenciaService
                .darrstrGerenciaIdFindByUsuarioId(usuarioModel.getUsuarioId());
        ArrayList<String> gerentes = this.usuarioService
                .findGerentesByGerencias(gerenciaIds);

        for (String sucursalId : sucursalIds) {
            ArrayList<HashMap<String, String>> gerenciasYGerentesHM = new ArrayList<>();

            for (int i = 0; i < gerenciaIds.size(); i++) {
                if (gerenciaIds.get(i).length() == 7) {
                    if (
                            sucursalId.equals(gerenciaIds.get(i).substring(0, 4))
                    ) {
                        HashMap<String, String> gerenciaYGerenteHM = new HashMap<>();
                        gerenciaYGerenteHM.put("gerencia", gerenciaIds.get(i));
                        gerenciaYGerenteHM.put("gerente", gerentes.get(i));
                        gerenciasYGerentesHM.add(gerenciaYGerenteHM);
                    }
                } else if (gerenciaIds.get(i).length() == 8) {
                    if (
                            sucursalId.equals(gerenciaIds.get(i).substring(0, 5))
                    ) {
                        HashMap<String, String> gerenciaYGerenteHM = new HashMap<>();
                        gerenciaYGerenteHM.put("gerencia", gerenciaIds.get(i));
                        gerenciaYGerenteHM.put("gerente", gerentes.get(i));
                        gerenciasYGerentesHM.add(gerenciaYGerenteHM);
                    }
                }
            }

            SucursalModel sucursalModel = this.sucursalService.findBySucursalId(sucursalId);
            gerenciasYGerentesBySucursalHM.put(sucursalModel.getNombre(), gerenciasYGerentesHM);
        }

        return new ResponseEntity<>(gerenciasYGerentesBySucursalHM, HttpStatus.OK);
    }
}

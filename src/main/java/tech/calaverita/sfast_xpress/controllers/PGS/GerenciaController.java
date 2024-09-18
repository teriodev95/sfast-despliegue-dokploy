package tech.calaverita.sfast_xpress.controllers.PGS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.Tuple;
import jakarta.servlet.http.HttpServletResponse;
import tech.calaverita.sfast_xpress.Constants;
import tech.calaverita.sfast_xpress.models.mariaDB.SucursalModel;
import tech.calaverita.sfast_xpress.models.mariaDB.UsuarioModel;
import tech.calaverita.sfast_xpress.services.SucursalService;
import tech.calaverita.sfast_xpress.services.UsuarioService;
import tech.calaverita.sfast_xpress.services.relation.UsuarioGerenciaService;
import tech.calaverita.sfast_xpress.services.relation.UsuarioSucursalService;

@CrossOrigin
@RestController
@RequestMapping("/xpress/v1/pwa/gerencias")
public final class GerenciaController {
    private final SucursalService sucursalService;
    private final UsuarioService usuarioService;
    private final UsuarioGerenciaService usuarioGerenciaService;
    private final UsuarioSucursalService usuarioSucursalService;

    public GerenciaController(SucursalService sucursalService, UsuarioService usuarioService,
            UsuarioGerenciaService usuarioGerenciaService,
            UsuarioSucursalService usuarioSucursalService) {
        this.sucursalService = sucursalService;
        this.usuarioService = usuarioService;
        this.usuarioGerenciaService = usuarioGerenciaService;
        this.usuarioSucursalService = usuarioSucursalService;
    }

    @ModelAttribute
    public void setResponseHeader(HttpServletResponse response) {
        response.setHeader("Version", Constants.VERSION);
        response.setHeader("Last-Modified", Constants.LAST_MODIFIED);
    }

    @GetMapping(path = "/{usuario}")
    public ResponseEntity<ArrayList<String>> getGerenciaIdsByUsuario(@PathVariable String usuario) {
        UsuarioModel usuarMod;

        try {
            usuarMod = this.usuarioService.findByUsuario(usuario);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // To easy code
        ArrayList<String> darrstrGerencia_O = this.usuarioGerenciaService.darrstrGerenciaIdFindByUsuarioId(usuarMod
                .getUsuarioId());

        return new ResponseEntity<>(darrstrGerencia_O, HttpStatus.OK);
    }

    @GetMapping(path = "/usuarios/{usuario}")
    public ResponseEntity<HashMap<String, ArrayList<String>>> redicdarrstrGerenciaIdFromSucursalGetByStrUsuario(
            @PathVariable String usuario) {
        UsuarioModel usuarioModel;
        HashMap<String, ArrayList<String>> gerenciasYGerentesBySucursalHM = new HashMap<>();

        try {
            usuarioModel = this.usuarioService.findByUsuario(usuario);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        ArrayList<String> sucursalIds = this.usuarioSucursalService
                .darrstrSucursalIdFindByUsuarioId(usuarioModel.getUsuarioId());
        ArrayList<String> gerenciaIds = this.usuarioGerenciaService
                .darrstrGerenciaIdFindByUsuarioId(usuarioModel.getUsuarioId());

        for (String sucursalId : sucursalIds) {
            ArrayList<String> gerenciasYGerentesHM = new ArrayList<>();

            for (int i = 0; i < gerenciaIds.size(); i++) {
                if (gerenciaIds.get(i).length() == 7) {
                    if (sucursalId.equals(gerenciaIds.get(i).substring(0, 4))) {
                        gerenciasYGerentesHM.add(gerenciaIds.get(i));
                    }
                } else if (gerenciaIds.get(i).length() == 8) {
                    if (sucursalId.equals(gerenciaIds.get(i).substring(0, 5))) {
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
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        ArrayList<String> sucursalIds = this.usuarioSucursalService
                .darrstrSucursalIdFindByUsuarioId(usuarioModel.getUsuarioId());
        ArrayList<String> gerenciaIds = this.usuarioGerenciaService
                .darrstrGerenciaIdFindByUsuarioId(usuarioModel.getUsuarioId());
        ArrayList<Tuple> gerentes = this.usuarioService
                .findByGerenciasTipoAndStatus(gerenciaIds);

        for (String sucursalId : sucursalIds) {
            ArrayList<HashMap<String, String>> gerenciasYGerentesHM = new ArrayList<>();

            for (int i = 0; i < gerenciaIds.size(); i++) {
                if (gerenciaIds.get(i).length() == 7) {
                    if (sucursalId.equals(gerenciaIds.get(i).substring(0, 4))) {
                        HashMap<String, String> gerenciaYGerenteHM = new HashMap<>();
                        gerenciaYGerenteHM.put("gerencia", gerenciaIds.get(i));
                        for (Tuple gerente : gerentes) {
                            if (gerente.get("gerencia").equals(gerenciaIds.get(i))) {
                                gerenciaYGerenteHM.put("gerente", gerente.get("nombre").toString());
                                gerentes.remove(gerente);
                                break;
                            }
                        }

                        if (!gerenciaYGerenteHM.containsKey("gerente")) {
                            gerenciaYGerenteHM.put("gerente", "SIN GERENTE ASIGNADO");
                        }

                        gerenciasYGerentesHM.add(gerenciaYGerenteHM);
                    }
                } else if (gerenciaIds.get(i).length() == 8) {
                    if (sucursalId.equals(gerenciaIds.get(i).substring(0, 5))) {
                        HashMap<String, String> gerenciaYGerenteHM = new HashMap<>();
                        gerenciaYGerenteHM.put("gerencia", gerenciaIds.get(i));
                        for (Tuple gerente : gerentes) {
                            if (gerente.get("gerencia").equals(gerenciaIds.get(i))) {
                                gerenciaYGerenteHM.put("gerente", gerente.get("nombre").toString());
                                gerentes.remove(gerente);
                                break;
                            }
                        }

                        if (!gerenciaYGerenteHM.containsKey("gerente")) {
                            gerenciaYGerenteHM.put("gerente", "SIN GERENTE ASIGNADO");
                        }
                        
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

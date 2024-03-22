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
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private final GerenciaService gerServ;
    private final SucursalService sucServ;
    private final UsuarioService usuarServ;
    private final UsuarioGerenciaService usuarGerServ;
    private final UsuarioSucursalService usuarSucServ;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    public GerenciaController(
            GerenciaService gerServ_S,
            SucursalService sucServ_S,
            UsuarioService usuarServ_S,
            UsuarioGerenciaService usuarGerServ_S,
            UsuarioSucursalService usuarSucServ_S
    ) {
        this.gerServ = gerServ_S;
        this.sucServ = sucServ_S;
        this.usuarServ = usuarServ_S;
        this.usuarGerServ = usuarGerServ_S;
        this.usuarSucServ = usuarSucServ_S;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @GetMapping(path = "")
    public ResponseEntity<ArrayList<GerenciaModel>> redarrgerModGet() {
        ArrayList<GerenciaModel> darrgerMod_O = this.gerServ.darrGerModFindAll();

        if (
                darrgerMod_O.isEmpty()
        ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(darrgerMod_O, HttpStatus.OK);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @GetMapping(path = "/{usuario}")
    public ResponseEntity<ArrayList<String>> getGerenciaIdsByUsuario(
            @PathVariable("usuario") String strUsuario_I
    ) {
        UsuarioModel usuarMod;

        try {
            usuarMod = this.usuarServ.usuarModFindByUsuario(strUsuario_I);
        } //
        catch (
                NoSuchElementException e
        ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        //                                                  //To easy code
        ArrayList<String> darrstrGerencia_O = this.usuarGerServ.darrstrGerenciaIdFindByUsuarioId(usuarMod
                .getUsuarioId());

        return new ResponseEntity<>(darrstrGerencia_O, HttpStatus.OK);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @GetMapping(path = "/usuarios/{usuario}")
    public ResponseEntity<HashMap<String, ArrayList<String>>> redicdarrstrGerenciaIdFromSucursalGetByStrUsuario(
            @PathVariable("usuario") String strUsuario_I
    ) {
        UsuarioModel usuarMod;
        HashMap<String, ArrayList<String>> dicdarrstrGerencia_O = new HashMap<>();

        try {
            usuarMod = this.usuarServ.usuarModFindByUsuario(strUsuario_I);
        } //
        catch (
                NoSuchElementException e
        ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        ArrayList<String> darrintSucursalId = this.usuarSucServ
                .darrstrSucursalIdFindByUsuarioId(usuarMod.getUsuarioId());
        ArrayList<String> darrstrGerenciaId = this.usuarGerServ
                .darrstrGerenciaIdFindByUsuarioId(usuarMod.getUsuarioId());

        for (String strSucursalId : darrintSucursalId) {
            ArrayList<String> darrstrGerenciaIdAux = new ArrayList<>();

            for (String strGerenciaId : darrstrGerenciaId) {
                if (strGerenciaId.length() == 7) {
                    if (
                            strSucursalId.equals(strGerenciaId.substring(0, 4))
                    ) {
                        darrstrGerenciaIdAux.add(strGerenciaId);
                    }
                } else if (strGerenciaId.length() == 8) {
                    if (
                            strSucursalId.equals(strGerenciaId.substring(0, 5))
                    ) {
                        darrstrGerenciaIdAux.add(strGerenciaId);
                    }
                }
            }

            SucursalModel sucMod = this.sucServ.findBySucursalId(strSucursalId);
            dicdarrstrGerencia_O.put(sucMod.getNombre(), darrstrGerenciaIdAux);
        }

        return new ResponseEntity<>(dicdarrstrGerencia_O, HttpStatus.OK);
    }
}

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
import java.util.NoSuchElementException;

@CrossOrigin
@RestController
@RequestMapping("/xpress/v1/pwa")
public final class LoginController {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private final AgenciaService agencServ;
    private final GerenciaService gerServ;
    private final SucursalService sucServ;
    private final UsuarioService usuarServ;
    private final UsuarioSucursalService usuarSucServ;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    LoginController(
            AgenciaService agencServ_S,
            GerenciaService gerServ_S,
            SucursalService sucServ_S,
            UsuarioService usuarServ_S,
            UsuarioSucursalService usuarSucServ_S
    ) {
        this.agencServ = agencServ_S;
        this.gerServ = gerServ_S;
        this.sucServ = sucServ_S;
        this.usuarServ = usuarServ_S;
        this.usuarSucServ = usuarSucServ_S;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @PostMapping(path = "/login")
    public ResponseEntity<?> regenericLogin(
            @RequestBody AuthCredentials login_I
    ) {
        UsuarioModel usuarMod_O = this.usuarServ.usuarModFindByUsuarioAndPin(login_I.getUsername(),
                login_I.getPassword());

        if (
                usuarMod_O == null
        ) {
            return new ResponseEntity<>("Usuario y/o contraseña incorrecto", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(usuarMod_O, HttpStatus.OK);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @GetMapping(path = "/agencias/{gerencia}")
    public ResponseEntity<ArrayList<String>> redarrstrAgenciaIdGetByStrGerencia(
            @PathVariable("gerencia") String strGerencia_I
    ) {
        ArrayList<String> darrstrAgencia = this.agencServ.darrstrAgenciaIdFindByGerenciaId(strGerencia_I);

        if (
                darrstrAgencia.isEmpty()
        ) {
            return new ResponseEntity<>(darrstrAgencia, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(darrstrAgencia, HttpStatus.OK);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @GetMapping(path = "/sucursales/usuarios/{usuario}")
    public ResponseEntity<ArrayList<SucursalModel>> getSucursalIdByUsuario(
            @PathVariable("usuario") String usuario
    ) {
        UsuarioModel usuarioModel;
        ArrayList<SucursalModel> sucEntSucursalEntities = new ArrayList<>();

        try {
            usuarioModel = this.usuarServ.usuarModFindByUsuario(usuario);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        ArrayList<String> darrintSucursalId = this.usuarSucServ
                .darrstrSucursalIdFindByUsuarioId(usuarioModel.getUsuarioId());

        for (String strSucursalId : darrintSucursalId) {
            sucEntSucursalEntities.add(this.sucServ.findBySucursalId(strSucursalId));
        }

        return new ResponseEntity<>(sucEntSucursalEntities, HttpStatus.OK);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @GetMapping(path = "gerencias/sucursales/{id}")
    public ResponseEntity<ArrayList<String>> getGerenciaIdsBySucursalId(
            @PathVariable("id") int intId_I
    ) {
        ArrayList<String> darrstrGerencia = this.gerServ.darrstrGerenciaIdFindBySucursalId(intId_I);

        if (
                darrstrGerencia.isEmpty()
        ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(darrstrGerencia, HttpStatus.OK);
    }
}

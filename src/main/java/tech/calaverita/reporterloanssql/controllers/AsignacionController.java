package tech.calaverita.reporterloanssql.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import retrofit2.Call;
import tech.calaverita.reporterloanssql.models.mariaDB.AsignacionModel;
import tech.calaverita.reporterloanssql.models.mariaDB.UsuarioModel;
import tech.calaverita.reporterloanssql.retrofit.RetrofitOdoo;
import tech.calaverita.reporterloanssql.retrofit.pojos.AsignacionBody;
import tech.calaverita.reporterloanssql.retrofit.pojos.AsignacionList;
import tech.calaverita.reporterloanssql.retrofit.pojos.ResponseBodyXms;
import tech.calaverita.reporterloanssql.services.AsignacionService;
import tech.calaverita.reporterloanssql.services.UsuarioService;
import tech.calaverita.reporterloanssql.utils.RetrofitOdooUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping(path = "/xpress/v1/assignments")
public class AsignacionController {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private final AsignacionService asignServ;
    private final UsuarioService usuarServ;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    private AsignacionController(
            AsignacionService asignServ_S,
            UsuarioService usuarServ_S
    ) {
        this.asignServ = asignServ_S;
        this.usuarServ = usuarServ_S;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @GetMapping(path = "/all")
    public @ResponseBody ResponseEntity<Iterable<AsignacionModel>> reiteasignModGet() {
        Iterable<AsignacionModel> iteasignMod_O = this.asignServ.findAll();

        if (
                !iteasignMod_O.iterator().hasNext()
        ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(iteasignMod_O, HttpStatus.OK);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @GetMapping(path = "/{agencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<Iterable<AsignacionModel>>
    reiteasignModGetByStrAgenciaIntAnioAndIntSemana(
            @PathVariable("agencia") String strAgencia_I,
            @PathVariable("anio") int intAnio_I,
            @PathVariable("semana") int intSemana_I
    ) {
        Iterable<AsignacionModel> iteasignMod_O = this.asignServ
                .findByAgenciaAnioAndSemana(strAgencia_I, intAnio_I, intSemana_I);

        if (
                !iteasignMod_O.iterator().hasNext()
        ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(iteasignMod_O, HttpStatus.OK);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @GetMapping(path = "/one/{id}")
    public @ResponseBody ResponseEntity<AsignacionModel> reoptasignModGetById(
            @PathVariable("id") String strId_I
    ) {
        AsignacionModel asignacionModel = this.asignServ.findById(strId_I);

        if (
                asignacionModel == null
        ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(asignacionModel, HttpStatus.OK);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @PostMapping(path = "/create-one")
    public @ResponseBody ResponseEntity<String> restrAsignModCreate(
            @RequestBody AsignacionModel asignMod_I
    ) {
        String strSession = "session_id=76d814874514726176f0615260848da2aab725ea";

        AsignacionModel asignacionModel = this.asignServ
                .findById(asignMod_I.getAsignacionId());

        if (
                asignacionModel != null
        ) {
            return new ResponseEntity<>("La Asignación Ya Existe", HttpStatus.CONFLICT);
        }

        UsuarioModel optusuarMod = this.usuarServ.findById(asignMod_I.getQuienRecibioUsuarioId());

        if (
                optusuarMod == null
        ) {
            return new ResponseEntity<>("Debe ingresar un quienRecibioUsuarioId válido", HttpStatus.BAD_REQUEST);
        }

        optusuarMod = this.usuarServ.findById(asignMod_I.getQuienEntregoUsuarioId());

        if (
                optusuarMod == null
        ) {
            return new ResponseEntity<>("Debe ingresar un quienEntregoUsuarioId válido", HttpStatus.BAD_REQUEST);
        }

        if (
                !asignMod_I.getLog().contains("{")
        ) {
            return new ResponseEntity<>("Debe ingresar un log con formato json", HttpStatus.BAD_REQUEST);
        }

        if (
                !asignMod_I.getLog().contains("}")
        ) {
            return new ResponseEntity<>("Debe ingresar un log con formato json", HttpStatus.BAD_REQUEST);
        }

        this.asignServ.save(asignMod_I);

        Call<ResponseBodyXms> callrespBodyXms = RetrofitOdoo.getInstance().getApi().asignacionCreateOne(strSession,
                new AsignacionBody(new AsignacionList(asignMod_I)));
        RetrofitOdooUtil.sendCall(callrespBodyXms);

        return new ResponseEntity<>("Asignación Creada con Éxito", HttpStatus.CREATED);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @PostMapping(path = "/create-many")
    public @ResponseBody ResponseEntity<ArrayList<HashMap<String, Object>>> redarrdicasignModCreate(
            @RequestBody ArrayList<AsignacionModel> darrasignMod_I
    ) {
        String strSession = "session_id=76d814874514726176f0615260848da2aab725ea";

        ArrayList<HashMap<String, Object>> darrdicasignMod_O = new ArrayList<>();

        for (AsignacionModel asignMod : darrasignMod_I) {
            HashMap<String, Object> objeto = new HashMap<>();
            String strMsg = "OK";
            String strMsgAux = "";
            boolean boolIsOnline = true;

            AsignacionModel optasignMod = this.asignServ.findById(asignMod.getAsignacionId());

            if (
                    optasignMod != null
            ) {
                strMsgAux += "La Asignación Ya Existe|";
                boolIsOnline = false;
            }

            UsuarioModel optusuarMod = this.usuarServ.findById(asignMod.getQuienRecibioUsuarioId());

            if (
                    optusuarMod == null
            ) {
                strMsgAux += "Debe ingresar un quienRecibioUsuarioId válido|";
                boolIsOnline = false;
            }

            optusuarMod = this.usuarServ.findById(asignMod.getQuienEntregoUsuarioId());

            if (
                    optusuarMod == null
            ) {
                strMsgAux += "Debe ingresar un quienEntregoUsuarioId válido|";
                boolIsOnline = false;
            }

            if (
                    !asignMod.getLog().contains("{")
            ) {
                strMsgAux += "Debe ingresar un log con formato json|";
                boolIsOnline = false;
            }

            if (
                    !asignMod.getLog().contains("}")
            ) {
                strMsgAux += "Debe ingresar un log con formato json|";
                boolIsOnline = false;
            }

            try {
                if (
                        boolIsOnline
                ) {
                    this.asignServ.save(asignMod);

                    Call<ResponseBodyXms> callrespBodyXms = RetrofitOdoo.getInstance().getApi()
                            .asignacionCreateOne(strSession,
                                    new AsignacionBody(new AsignacionList(asignMod)));
                    RetrofitOdooUtil.sendCall(callrespBodyXms);
                } //
                else {
                    strMsg = strMsgAux;
                }
            } //
            catch (
                    HttpClientErrorException e
            ) {
                strMsg = e.toString();
                boolIsOnline = false;
            }

            objeto.put("id", asignMod.getAsignacionId());
            objeto.put("isOnline", boolIsOnline);
            objeto.put("msg", strMsg);
            darrdicasignMod_O.add(objeto);
        }

        return new ResponseEntity<>(darrdicasignMod_O, HttpStatus.OK);
    }
}
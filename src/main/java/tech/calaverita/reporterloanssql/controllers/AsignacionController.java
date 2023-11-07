package tech.calaverita.reporterloanssql.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import retrofit2.Call;
import tech.calaverita.reporterloanssql.persistence.entities.AsignacionEntity;
import tech.calaverita.reporterloanssql.persistence.entities.UsuarioEntity;
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
public final class AsignacionController {
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
    public @ResponseBody ResponseEntity<Iterable<AsignacionEntity>> reiteasignModGet() {
        Iterable<AsignacionEntity> iteasignMod_O = this.asignServ.iteasignModFindAll();

        if (
                !iteasignMod_O.iterator().hasNext()
        ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(iteasignMod_O, HttpStatus.OK);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @GetMapping(path = "/{agencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<Iterable<AsignacionEntity>>
    reiteasignModGetByStrAgenciaIntAnioAndIntSemana(
            @PathVariable("agencia") String strAgencia_I,
            @PathVariable("anio") int intAnio_I,
            @PathVariable("semana") int intSemana_I
    ) {
        Iterable<AsignacionEntity> iteasignMod_O = this.asignServ
                .darrasignModFindByAgenciaAnioAndSemana(strAgencia_I, intAnio_I, intSemana_I);

        if (
                !iteasignMod_O.iterator().hasNext()
        ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(iteasignMod_O, HttpStatus.OK);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @GetMapping(path = "/one/{id}")
    public @ResponseBody ResponseEntity<Optional<AsignacionEntity>> reoptasignModGetById(
            @PathVariable("id") String strId_I
    ) {
        Optional<AsignacionEntity> optAsignMod_O = this.asignServ.optasignModFindById(strId_I);

        if (
                optAsignMod_O.isEmpty()
        ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(optAsignMod_O, HttpStatus.OK);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @PostMapping(path = "/create-one")
    public @ResponseBody ResponseEntity<String> restrAsignModCreate(
            @RequestBody AsignacionEntity asignMod_I
    ) {
        String strSession = "session_id=76d814874514726176f0615260848da2aab725ea";

        Optional<AsignacionEntity> optasignMod = this.asignServ
                .optasignModFindById(asignMod_I.getAsignacionId());

        if (
                !optasignMod.isEmpty()
        ) {
            return new ResponseEntity<>("La Asignación Ya Existe", HttpStatus.CONFLICT);
        }

        Optional<UsuarioEntity> optusuarMod = this.usuarServ.optusuarEntFindById(asignMod_I.getQuienRecibioUsuarioId());

        if (
                optusuarMod.isEmpty()
        ) {
            return new ResponseEntity<>("Debe ingresar un quienRecibioUsuarioId válido", HttpStatus.BAD_REQUEST);
        }

        optusuarMod = this.usuarServ.optusuarEntFindById(asignMod_I.getQuienEntregoUsuarioId());

        if (
                optusuarMod.isEmpty()
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
            @RequestBody ArrayList<AsignacionEntity> darrasignMod_I
    ) {
        String strSession = "session_id=76d814874514726176f0615260848da2aab725ea";

        ArrayList<HashMap<String, Object>> darrdicasignMod_O = new ArrayList<>();

        for (AsignacionEntity asignMod : darrasignMod_I) {
            HashMap<String, Object> objeto = new HashMap<>();
            String strMsg = "OK";
            String strMsgAux = "";
            boolean boolIsOnline = true;

            Optional<AsignacionEntity> optasignMod = this.asignServ.optasignModFindById(asignMod.getAsignacionId());

            if (
                    !optasignMod.isEmpty()
            ) {
                strMsgAux += "La Asignación Ya Existe|";
                boolIsOnline = false;
            }

            Optional<UsuarioEntity> optusuarMod = this.usuarServ.optusuarEntFindById(asignMod.getQuienRecibioUsuarioId());

            if (
                    optusuarMod.isEmpty()
            ) {
                strMsgAux += "Debe ingresar un quienRecibioUsuarioId válido|";
                boolIsOnline = false;
            }

            optusuarMod = this.usuarServ.optusuarEntFindById(asignMod.getQuienEntregoUsuarioId());

            if (
                    optusuarMod.isEmpty()
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
package tech.calaverita.sfast_xpress.controllers;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import jakarta.servlet.http.HttpServletResponse;
import retrofit2.Call;
import tech.calaverita.sfast_xpress.Constants;
import tech.calaverita.sfast_xpress.models.mariaDB.AsignacionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.UsuarioModel;
import tech.calaverita.sfast_xpress.retrofit.RetrofitOdoo;
import tech.calaverita.sfast_xpress.retrofit.pojos.AsignacionBody;
import tech.calaverita.sfast_xpress.retrofit.pojos.AsignacionList;
import tech.calaverita.sfast_xpress.retrofit.pojos.ResponseBodyXms;
import tech.calaverita.sfast_xpress.services.AsignacionService;
import tech.calaverita.sfast_xpress.services.UsuarioService;
import tech.calaverita.sfast_xpress.utils.RetrofitOdooUtil;
import tech.calaverita.sfast_xpress.utils.pwa.PWAUtil;

@CrossOrigin
@RestController
@RequestMapping(path = "/xpress/v1/assignments")
public final class AsignacionController {
    private final AsignacionService asignacionService;
    private final UsuarioService usuarioService;

    public AsignacionController(AsignacionService asignacionService, UsuarioService usuarioService) {
        this.asignacionService = asignacionService;
        this.usuarioService = usuarioService;
    }

    @ModelAttribute
    public void setResponseHeader(HttpServletResponse response) {
        response.setHeader("Version", Constants.VERSION);
        response.setHeader("Last-Modified", Constants.LAST_MODIFIED);
    }

    @GetMapping(path = "/all")
    public @ResponseBody ResponseEntity<Iterable<AsignacionModel>> getAll() {
        Iterable<AsignacionModel> asignacionModelIterable = this.asignacionService.findAll();

        if (!asignacionModelIterable.iterator().hasNext()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(asignacionModelIterable, HttpStatus.OK);
    }

    @GetMapping(path = "/{agencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<Iterable<AsignacionModel>> getByAgenciaAnioAndSemana(
            @PathVariable String agencia, @PathVariable int anio, @PathVariable int semana) {
        UsuarioModel agenteUsuarioModel = this.usuarioService.findByAgenciaTipoAndStatus(agencia, "Agente", true);

        Iterable<AsignacionModel> asignacionModelIterable = this.asignacionService
                .findByQuienEntregoUsuarioIdAnioAndSemana(agenteUsuarioModel.getUsuarioId(), anio, semana);

        if (!asignacionModelIterable.iterator().hasNext()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(asignacionModelIterable, HttpStatus.OK);
    }

    @GetMapping(path = "/by_usuario-id_anio_and_semana/{usuarioId}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<HashMap<String, Object>> getByUsuarioIdAnioAndSemana(
            @PathVariable Integer usuarioId, @PathVariable int anio, @PathVariable int semana) {
        HashMap<String, Object> responseHM = new HashMap<>();

        responseHM.put("ingresos",
                this.asignacionService
                        .findByQuienRecibioUsuarioIdAnioAndSemana(usuarioId, anio, semana));
        responseHM.put("egresos",
                this.asignacionService
                        .findByQuienEntregoUsuarioIdAnioAndSemana(usuarioId, anio, semana));

        responseHM = PWAUtil.asignacionModelPwa(responseHM);

        if (responseHM.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(responseHM, HttpStatus.OK);
    }

    @GetMapping(path = "/one/{id}")
    public @ResponseBody ResponseEntity<AsignacionModel> getOneById(@PathVariable String id) {
        AsignacionModel asignacionModel = this.asignacionService.findById(id);

        if (asignacionModel == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(asignacionModel, HttpStatus.OK);
    }

    @PostMapping(path = "/create-one")
    public @ResponseBody ResponseEntity<String> postCreateOne(@RequestBody AsignacionModel asignacionModel) {
        String strSession = "session_id=76d814874514726176f0615260848da2aab725ea";

        if (this.asignacionService.existById(asignacionModel.getAsignacionId())) {
            return new ResponseEntity<>("La Asignación Ya Existe", HttpStatus.CONFLICT);
        }

        UsuarioModel optusuarMod = this.usuarioService.findById(asignacionModel.getQuienRecibioUsuarioId());

        if (optusuarMod == null) {
            return new ResponseEntity<>("Debe ingresar un quienRecibioUsuarioId válido", HttpStatus.BAD_REQUEST);
        }

        optusuarMod = this.usuarioService.findById(asignacionModel.getQuienEntregoUsuarioId());

        if (optusuarMod == null) {
            return new ResponseEntity<>("Debe ingresar un quienEntregoUsuarioId válido", HttpStatus.BAD_REQUEST);
        }

        if (!asignacionModel.getLog().contains("{")) {
            return new ResponseEntity<>("Debe ingresar un log con formato json", HttpStatus.BAD_REQUEST);
        }

        if (!asignacionModel.getLog().contains("}")) {
            return new ResponseEntity<>("Debe ingresar un log con formato json", HttpStatus.BAD_REQUEST);
        }

        this.asignacionService.save(asignacionModel);

        Call<ResponseBodyXms> callrespBodyXms = RetrofitOdoo.getInstance().getApi().asignacionCreateOne(strSession,
                new AsignacionBody(new AsignacionList(asignacionModel)));
        RetrofitOdooUtil.sendCall(callrespBodyXms);

        return new ResponseEntity<>("Asignación Creada con Éxito", HttpStatus.CREATED);
    }

    @PostMapping(path = "/create-many")
    public @ResponseBody ResponseEntity<ArrayList<HashMap<String, Object>>> postCreateMany(
            @RequestBody ArrayList<AsignacionModel> asignacionModels) {
        String strSession = "session_id=76d814874514726176f0615260848da2aab725ea";

        ArrayList<HashMap<String, Object>> darrdicasignMod_O = new ArrayList<>();

        for (AsignacionModel asignMod : asignacionModels) {
            HashMap<String, Object> objeto = new HashMap<>();
            String strMsg = "OK";
            String strMsgAux = "";
            boolean boolIsOnline = true;

            AsignacionModel optasignMod = this.asignacionService.findById(asignMod.getAsignacionId());

            if (optasignMod != null) {
                strMsgAux += "La Asignación Ya Existe|";
                boolIsOnline = false;
            }

            UsuarioModel optusuarMod = this.usuarioService.findById(asignMod.getQuienRecibioUsuarioId());

            if (optusuarMod == null) {
                strMsgAux += "Debe ingresar un quienRecibioUsuarioId válido|";
                boolIsOnline = false;
            }

            optusuarMod = this.usuarioService.findById(asignMod.getQuienEntregoUsuarioId());

            if (optusuarMod == null) {
                strMsgAux += "Debe ingresar un quienEntregoUsuarioId válido|";
                boolIsOnline = false;
            }

            if (!asignMod.getLog().contains("{")) {
                strMsgAux += "Debe ingresar un log con formato json|";
                boolIsOnline = false;
            }

            if (!asignMod.getLog().contains("}")) {
                strMsgAux += "Debe ingresar un log con formato json|";
                boolIsOnline = false;
            }

            try {
                if (boolIsOnline) {
                    this.asignacionService.save(asignMod);

                    Call<ResponseBodyXms> callrespBodyXms = RetrofitOdoo.getInstance().getApi()
                            .asignacionCreateOne(strSession,
                                    new AsignacionBody(new AsignacionList(asignMod)));
                    RetrofitOdooUtil.sendCall(callrespBodyXms);
                } else {
                    strMsg = strMsgAux;
                }
            } catch (HttpClientErrorException e) {
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
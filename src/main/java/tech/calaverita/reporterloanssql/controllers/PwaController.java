package tech.calaverita.reporterloanssql.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.helpers.PwaUtil;
import tech.calaverita.reporterloanssql.models.CalendarioModel;
import tech.calaverita.reporterloanssql.models.UsuarioModel;
import tech.calaverita.reporterloanssql.pojos.CobranzaPwa;
import tech.calaverita.reporterloanssql.pojos.ObjectsContainer;
import tech.calaverita.reporterloanssql.security.AuthCredentials;
import tech.calaverita.reporterloanssql.services.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@CrossOrigin
@RestController
@RequestMapping("/xpress/v1/pwa")
public class PwaController {
    @Autowired
    private RepositoriesContainer repositoriesContainer;
    ObjectsContainer[] objectsContainers;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthCredentials login) {
        UsuarioModel usuarioModel = XpressService.login(login.getUsername(), login.getPassword());

        if (usuarioModel == null) {
            return new ResponseEntity<>("Usuario y/o contraseña incorrecto", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(usuarioModel, HttpStatus.OK);
    }

    @GetMapping("/agencias/{gerencia}")
    public ResponseEntity<ArrayList<String>> getAgenciasByGerencia(@PathVariable("gerencia") String gerencia) {
        ArrayList<String> agencias = AgenciaService.getAgenciasByGerencia(gerencia);

        if (agencias.isEmpty()) {
            return new ResponseEntity<>(agencias, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(agencias, HttpStatus.OK);
    }

    @GetMapping("/gerencias/{seguridad}")
    public ResponseEntity<ArrayList<String>> getGerenciasBySeguridad(@PathVariable("seguridad") String seguridad) {
        ArrayList<String> gerencias = GerenciaService.getGerenciasBySeguridad(seguridad);
        return new ResponseEntity<>(gerencias, HttpStatus.OK);
    }

    @GetMapping("/cobranza/{agencia}/{anio}/{semana}")
    public ResponseEntity<CobranzaPwa> getCobranzaByAgenciaAnioAndSemana(@PathVariable("agencia") String agencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana) {
        CobranzaPwa cobranzaPwa = new CobranzaPwa();

        cobranzaPwa.setCobranza(PwaUtil.getPrestamoCobranzaPwasFromPrestamoModelsAndPagoModels(agencia, anio, semana));

        return new ResponseEntity<>(cobranzaPwa, HttpStatus.OK);
    }

    @GetMapping("/semana_actual")
    public ResponseEntity<CalendarioModel> getSemanaActual() {
        String fechaActual = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        CalendarioModel calendarioModel = CalendarioService.getSemanaActualXpressByFechaActual(fechaActual);

        return new ResponseEntity<>(calendarioModel, HttpStatus.OK);
    }
}

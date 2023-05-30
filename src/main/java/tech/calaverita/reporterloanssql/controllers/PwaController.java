package tech.calaverita.reporterloanssql.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.calaverita.reporterloanssql.helpers.PwaUtil;
import tech.calaverita.reporterloanssql.pojos.CobranzaPwa;
import tech.calaverita.reporterloanssql.services.PrestamoService;
import tech.calaverita.reporterloanssql.services.XpressService;

import java.util.ArrayList;

@RestController
@RequestMapping("/xpress/v1/pwa")
public class PwaController {
    @GetMapping("/agencias/{gerencia}")
    public ResponseEntity<ArrayList<String>> getAgenciasByGerencia(@PathVariable("gerencia") String gerencia){
        ArrayList<String> agencias = XpressService.getAgenciasByGerencia(gerencia);

        if(agencias.isEmpty()){
            return new ResponseEntity<>(agencias, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(agencias, HttpStatus.OK);
    }

    @GetMapping("/cobranza/{agencia}/{anio}/{semana}")
    public ResponseEntity<CobranzaPwa> getCobranzaByAgenciaAnioAndSemana(@PathVariable("agencia") String agencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana){
        CobranzaPwa cobranzaPwa = new CobranzaPwa();

        cobranzaPwa.setCobranza(PwaUtil.getPrestamoCobranzaPwasFromPrestamoModelsAndPagoModels(agencia, anio, semana));

        return new ResponseEntity<>(cobranzaPwa, HttpStatus.OK);
    }
}

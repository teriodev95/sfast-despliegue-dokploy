package tech.calaverita.reporterloanssql.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.utils.VisitaUtil;
import tech.calaverita.reporterloanssql.models.VisitaModel;
import tech.calaverita.reporterloanssql.services.VisitaService;

import java.util.ArrayList;

@RestController
@RequestMapping(path = "/xpress/v1/visits")
public class VisitaController {
    private VisitaService visitaService;

    @Autowired
    public void setVisitaService(VisitaService visitaService) {
        this.visitaService = visitaService;
    }

    @GetMapping
    public ResponseEntity<ArrayList<VisitaModel>> getVisitaModels() {
        ArrayList<VisitaModel> visitaModels = visitaService.getVisitaModels();

        if (visitaModels.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(visitaModels, HttpStatus.OK);
    }

    @GetMapping(path = "/{visitaId}")
    public ResponseEntity<VisitaModel> getVisitaModelByVisitaId(@PathVariable(name = "visitaId") String visitaId) {
        VisitaModel visitaModel = visitaService.getVisitaModelByVisitaId(visitaId);

        if (visitaModel == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(visitaModel, HttpStatus.OK);
    }

    @GetMapping(path = "/prestamo-id/{prestamoId}")
    public ResponseEntity<ArrayList<VisitaModel>> getVisitaModelsByPrestamoId(@PathVariable(name = "prestamoId") String prestamoId) {
        ArrayList<VisitaModel> visitaModels = visitaService.getVisitaModelsByPrestamoId(prestamoId);

        if (visitaModels.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(visitaModels, HttpStatus.OK);
    }

    @PostMapping(path = "/create-one")
    public ResponseEntity<String> createVisitaModel(@RequestBody VisitaModel visitaModel) {

        ResponseEntity<String> responseEntity = VisitaUtil.checkVisit(visitaModel);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            visitaService.createVisitaModel(visitaModel);
        } else {
            return responseEntity;
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

package tech.calaverita.reporterloanssql.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.models.mariaDB.VisitaModel;
import tech.calaverita.reporterloanssql.services.VisitaService;
import tech.calaverita.reporterloanssql.utils.VisitaUtil;

import java.util.ArrayList;

@RestController
@RequestMapping(path = "/xpress/v1/visits")
public final class VisitaController {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private final VisitaService visServ;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    private VisitaController(
            VisitaService visServ_S
    ) {
        this.visServ = visServ_S;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @GetMapping
    public ResponseEntity<ArrayList<VisitaModel>> getVisitaModels() {
        ArrayList<VisitaModel> visEntVisitaEntities = visServ.darrvisModFindAll();

        if (visEntVisitaEntities.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(visEntVisitaEntities, HttpStatus.OK);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @GetMapping(path = "/{visitaId}")
    public ResponseEntity<VisitaModel> getVisitaModelByVisitaId(
            @PathVariable(name = "visitaId") String visitaId
    ) {
        VisitaModel visitaModel = visServ.visModFindByVisitaId(visitaId);

        if (visitaModel == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(visitaModel, HttpStatus.OK);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @GetMapping(path = "/prestamo-id/{prestamoId}")
    public ResponseEntity<ArrayList<VisitaModel>> getVisitaModelsByPrestamoId(
            @PathVariable(name = "prestamoId") String prestamoId
    ) {
        ArrayList<VisitaModel> visEntVisitaEntities = visServ.darrvisModFindByPrestamoId(prestamoId);

        if (visEntVisitaEntities.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(visEntVisitaEntities, HttpStatus.OK);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @PostMapping(path = "/create-one")
    public ResponseEntity<String> createVisitaModel(
            @RequestBody VisitaModel visitaModel
    ) {

        ResponseEntity<String> responseEntity = VisitaUtil.restrCheckVisit(visitaModel);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            visServ.save(visitaModel);
        } else {
            return responseEntity;
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

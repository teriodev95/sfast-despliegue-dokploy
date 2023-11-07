package tech.calaverita.reporterloanssql.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.persistence.entities.VisitaEntity;
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
    public ResponseEntity<ArrayList<VisitaEntity>> getVisitaModels() {
        ArrayList<VisitaEntity> visEntVisitaEntities = visServ.darrvisModFindAll();

        if (visEntVisitaEntities.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(visEntVisitaEntities, HttpStatus.OK);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @GetMapping(path = "/{visitaId}")
    public ResponseEntity<VisitaEntity> getVisitaModelByVisitaId(
            @PathVariable(name = "visitaId") String visitaId
    ) {
        VisitaEntity visitaEntity = visServ.visModFindByVisitaId(visitaId);

        if (visitaEntity == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(visitaEntity, HttpStatus.OK);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @GetMapping(path = "/prestamo-id/{prestamoId}")
    public ResponseEntity<ArrayList<VisitaEntity>> getVisitaModelsByPrestamoId(
            @PathVariable(name = "prestamoId") String prestamoId
    ) {
        ArrayList<VisitaEntity> visEntVisitaEntities = visServ.darrvisModFindByPrestamoId(prestamoId);

        if (visEntVisitaEntities.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(visEntVisitaEntities, HttpStatus.OK);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @PostMapping(path = "/create-one")
    public ResponseEntity<String> createVisitaModel(
            @RequestBody VisitaEntity visitaEntity
    ) {

        ResponseEntity<String> responseEntity = VisitaUtil.restrCheckVisit(visitaEntity);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            visServ.save(visitaEntity);
        } else {
            return responseEntity;
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

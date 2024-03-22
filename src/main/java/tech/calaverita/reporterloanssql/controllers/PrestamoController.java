package tech.calaverita.reporterloanssql.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.models.view.PrestamoModel;
import tech.calaverita.reporterloanssql.repositories.view.PrestRepoPrestamoRepository;
import tech.calaverita.reporterloanssql.utils.PrestamoUtil;

@RestController
@RequestMapping(path = "/xpress/v1/loans")
public final class PrestamoController {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private final PrestRepoPrestamoRepository prestRepo;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    private PrestamoController(
            PrestRepoPrestamoRepository prestRepo_S
    ) {
        this.prestRepo = prestRepo_S;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @CrossOrigin
    @GetMapping(path = "/{id}")
    public ResponseEntity<PrestamoModel> represtModGetByStrId(
            @PathVariable("id") String strId_I
    ) {
        PrestamoModel prestMod_O = prestRepo.prestEntFindByPrestamoId(strId_I);

        if (
                prestMod_O == null
        ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        PrestamoUtil.prestamoModelAsignarPorcentajeCobrado(prestMod_O);

        return new ResponseEntity<>(prestMod_O, HttpStatus.OK);
    }
}

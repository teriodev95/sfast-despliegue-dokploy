package tech.calaverita.reporterloanssql.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.helpers.PrestamoUtil;
import tech.calaverita.reporterloanssql.models.PrestamoModel;
import tech.calaverita.reporterloanssql.repositories.PrestamoRepository;

@RestController
@RequestMapping(path = "/xpress/v1/loans")
public class PrestamoController {
    @Autowired
    PrestamoRepository prestamoRepository;

    @CrossOrigin
    @GetMapping(path = "/{id}")
    public ResponseEntity<PrestamoModel> getPrestamoByPrestamoId(@PathVariable("id") String prestamoId) {
        PrestamoModel prestamoModel = prestamoRepository.getPrestamoByPrestamoId(prestamoId);

        if (prestamoModel == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        PrestamoUtil.asignarPorcentajeCobrado(prestamoModel);

        return new ResponseEntity<>(prestamoModel, HttpStatus.OK);
    }
}

package tech.calaverita.reporterloanssql.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.calaverita.reporterloanssql.models.PrestamoModel;
import tech.calaverita.reporterloanssql.repositories.PrestamoRepository;

@RestController
@RequestMapping(path = "/xpress/v1/loans")
public class PrestamoController {
    @Autowired
    PrestamoRepository prestamoRepository;

    @GetMapping(path = "/{id}")
    public PrestamoModel getPrestamoByPrestamoId(@PathVariable("id") String prestamoId){

        return prestamoRepository.getPrestamoByPrestamoId(prestamoId);
    }
}

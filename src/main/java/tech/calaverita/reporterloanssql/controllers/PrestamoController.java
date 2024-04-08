package tech.calaverita.reporterloanssql.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.Constants;
import tech.calaverita.reporterloanssql.models.mariaDB.views.PrestamoModel;
import tech.calaverita.reporterloanssql.repositories.views.PrestamoRepository;
import tech.calaverita.reporterloanssql.utils.PrestamoUtil;

@RestController
@RequestMapping(path = "/xpress/v1/loans")
public final class PrestamoController {
    private final PrestamoRepository prestamoRepository;

    public PrestamoController(
            PrestamoRepository prestamoRepository
    ) {
        this.prestamoRepository = prestamoRepository;
    }

    @ModelAttribute
    public void setResponseHeader(HttpServletResponse response) {
        response.setHeader("Version", Constants.VERSION);
        response.setHeader("Last-Modified", Constants.LAST_MODIFIED);
    }

    @CrossOrigin
    @GetMapping(path = "/{id}")
    public ResponseEntity<PrestamoModel> represtModGetByStrId(
            @PathVariable("id") String strId_I
    ) {
        PrestamoModel prestMod_O = prestamoRepository.prestEntFindByPrestamoId(strId_I);

        if (
                prestMod_O == null
        ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        PrestamoUtil.prestamoModelAsignarPorcentajeCobrado(prestMod_O);

        return new ResponseEntity<>(prestMod_O, HttpStatus.OK);
    }
}

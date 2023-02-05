package tech.calaverita.reporterloanssql.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.models.PagoModel;
import tech.calaverita.reporterloanssql.models.PrestamoModel;
import tech.calaverita.reporterloanssql.pojos.PrestamoPago;
import tech.calaverita.reporterloanssql.repositories.PrestamoRepository;

import java.util.ArrayList;

@Controller
@RequestMapping(path = "/xpress/v1/loans")
public class PrestamoController {
    @Autowired
    private PrestamoRepository prestamoRepository;

    @GetMapping(path = "/loans-by-pagos-and-agencia")
    public @ResponseBody ArrayList<PrestamoModel> getPrestamosByAgencia(@RequestBody ArrayList<PagoModel> pagos){
        ArrayList<PrestamoModel> prestamos = new ArrayList<>();

        for(PagoModel pago: pagos){
            prestamos.add(prestamoRepository.getPrestamoModelsByPrestamoIdAndAgencia(pago.getPrestamoId(), pago.getAgente()));
        }

        return prestamos;
    }

    @GetMapping(path = "/loans-by-pagos-and-gerencia/{gerencia}")
    public @ResponseBody ArrayList<PrestamoModel> getPrestamosByGerencia(@RequestBody ArrayList<PagoModel> pagos, @PathVariable("gerencia") String gerencia){
        ArrayList<PrestamoModel> prestamos = new ArrayList<>();

        for(PagoModel pago: pagos){
            prestamos.add(prestamoRepository.getPrestamoModelsByPrestamoIdAndGerencia(pago.getPrestamoId(), gerencia));
        }

        return prestamos;
    }
}

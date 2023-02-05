package tech.calaverita.reporterloanssql.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.calaverita.reporterloanssql.models.PagoModel;
import tech.calaverita.reporterloanssql.models.PrestamoModel;
import tech.calaverita.reporterloanssql.pojos.PrestamoPago;

import java.util.ArrayList;

@Controller
@RequestMapping(path = "/xpress/v1/loanPay")
public class PrestamoPagoController {
    @Autowired
    private PagoController pagoController;
    @Autowired PrestamoController prestamoController;

    @GetMapping(path = "/cobranza-agencia/{agencia}/{anio}/{semana}")
    public @ResponseBody ArrayList<PrestamoPago> getPrestamoPagoForCobranzaByAgencia(@PathVariable("agencia") String agencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana){
        ArrayList<PrestamoPago> prestamoPagos = new ArrayList<>();
        ArrayList<PagoModel> pagos = pagoController.getPagosForCobranzaByAgencia(agencia, anio, semana);
        ArrayList<PrestamoModel> prestamos = prestamoController.getPrestamosByAgencia(pagos);

        for(PrestamoModel prestamo: prestamos){
            for(PagoModel pago: pagos){
                PrestamoPago prestamoPago = new PrestamoPago();
                prestamoPago.setPrestamo(prestamo);
                if(prestamo.getPrestamoId().equalsIgnoreCase(pago.getPrestamoId())){
                    prestamoPago.setPago(pago);
                    prestamoPagos.add(prestamoPago);
                }
            }
        }
        return prestamoPagos;
    }

    @GetMapping(path = "/cobranza-gerencia/{gerencia}/{anio}/{semana}")
    public @ResponseBody ArrayList<PrestamoPago> getPrestamoPagoForCobranzaByGerencia(@PathVariable("gerencia") String gerencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana){
        ArrayList<PrestamoPago> prestamoPagos = new ArrayList<>();
        ArrayList<PagoModel> pagos = pagoController.getPagosForCobranzaByGerencia(anio, semana);
        ArrayList<PrestamoModel> prestamos = prestamoController.getPrestamosByGerencia(pagos, gerencia);

        for(PrestamoModel prestamo: prestamos){
            for(PagoModel pago: pagos){
                PrestamoPago prestamoPago = new PrestamoPago();
                prestamoPago.setPrestamo(prestamo);
                if(prestamo.getPrestamoId().equalsIgnoreCase(pago.getPrestamoId())){
                    prestamoPago.setPago(pago);
                    prestamoPagos.add(prestamoPago);
                }
            }
        }
        return prestamoPagos;
    }

    @GetMapping(path = "/dashboard-agencia/{agencia}/{anio}/{semana}")
    public @ResponseBody ArrayList<PrestamoPago> getPrestamoPagoForDashboardByAgencia(@PathVariable("agencia") String agencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana){
        ArrayList<PrestamoPago> prestamoPagos = new ArrayList<>();
        ArrayList<PagoModel> pagos = pagoController.getPagosForDashboard(agencia, anio, semana);
        ArrayList<PrestamoModel> prestamos = prestamoController.getPrestamosByAgencia(pagos);

        for(PrestamoModel prestamo: prestamos){
            for(PagoModel pago: pagos){
                PrestamoPago prestamoPago = new PrestamoPago();
                prestamoPago.setPrestamo(prestamo);
                if(prestamo.getPrestamoId().equalsIgnoreCase(pago.getPrestamoId())){
                    prestamoPago.setPago(pago);
                    prestamoPagos.add(prestamoPago);
                }
            }
        }
        return prestamoPagos;
    }

    @GetMapping(path = "/dashboard-gerencia/{gerencia}/{anio}/{semana}")
    public @ResponseBody ArrayList<PrestamoPago> getPrestamoPagoForDashboardByGerencia(@PathVariable("gerencia") String gerencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana){
        ArrayList<PrestamoPago> prestamoPagos = new ArrayList<>();
        ArrayList<PagoModel> pagos = pagoController.getPagosForDashboardByGerencia(anio, semana);
        ArrayList<PrestamoModel> prestamos = prestamoController.getPrestamosByGerencia(pagos, gerencia);

        for(PrestamoModel prestamo: prestamos){
            for(PagoModel pago: pagos){
                PrestamoPago prestamoPago = new PrestamoPago();
                prestamoPago.setPrestamo(prestamo);
                if(prestamo.getPrestamoId().equalsIgnoreCase(pago.getPrestamoId())){
                    prestamoPago.setPago(pago);
                    prestamoPagos.add(prestamoPago);
                }
            }
        }
        return prestamoPagos;
    }
}

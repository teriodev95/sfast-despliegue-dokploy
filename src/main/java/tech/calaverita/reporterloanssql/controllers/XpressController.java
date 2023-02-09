package tech.calaverita.reporterloanssql.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.models.PagoModel;
import tech.calaverita.reporterloanssql.models.PrestamoModel;
import tech.calaverita.reporterloanssql.pojos.Cobranza;
import tech.calaverita.reporterloanssql.pojos.Dashboard;
import tech.calaverita.reporterloanssql.pojos.Liquidacion;
import tech.calaverita.reporterloanssql.pojos.PrestamoPago;
import tech.calaverita.reporterloanssql.repositories.PagoRepository;
import tech.calaverita.reporterloanssql.repositories.PrestamoRepository;
import tech.calaverita.reporterloanssql.scripts.MyUtil;

import java.util.ArrayList;
import java.util.HashSet;

@RestController()
@RequestMapping(path = "/xpress/v1")
public class XpressController {
    @Autowired
    private PrestamoPagoController prestamoPagoController;
    @Autowired
    private PrestamoRepository prestamoRepository;
    @Autowired
    private PagoRepository pagoRepository;

    @GetMapping(path = "/cobranza-agencia/{agencia}/{anio}/{semana}")
    public @ResponseBody Cobranza getCobranzaByAgencia(@PathVariable("agencia") String agencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana){
        Cobranza cobranza = new Cobranza();
        ArrayList<PrestamoPago> prestamoPagos = prestamoPagoController.getPrestamoPagoForCobranzaByAgencia(agencia, anio, semana);
        ArrayList<PrestamoModel> prestamos = new ArrayList<>();

        for(int i = 0; i < prestamoPagos.size(); i++){
            double excedente = prestamoPagos.get(i).getPago().getAbreCon() - prestamoPagos.get(i).getPago().getTarifa();
            if(prestamoPagos.get(i).getPago().getCierraCon() == (excedente / prestamoPagos.get(i).getPrestamo().getPorcentaje())){
                prestamoPagos.remove(i);
            }
        }

        for(PrestamoPago prestamoPago: prestamoPagos){
            if(prestamoPago.getPago().getCierraCon() > 0){
                PrestamoModel prestamo = prestamoPago.getPrestamo();
                prestamos.add(prestamo);
                cobranza.setAgencia(agencia);
                cobranza.setClientes(cobranza.getClientes() + 1);

                if(prestamoPago.getPago().getCierraCon() < prestamoPago.getPrestamo().getTarifa())
                    prestamoPago.getPago().setTarifa((prestamoPago.getPago().getCierraCon()));

                switch (prestamo.getDiaDePago()){
                    case "MIERCOLES":
                        cobranza.setDebitoMiercoles(cobranza.getDebitoMiercoles() + prestamoPago.getPago().getTarifa());
                        break;
                    case "JUEVES":
                        cobranza.setDebitoJueves(cobranza.getDebitoJueves() + prestamoPago.getPago().getTarifa());
                        break;
                    case "VIERNES":
                        cobranza.setDebitoViernes(cobranza.getDebitoViernes() + prestamoPago.getPago().getTarifa());
                        break;
                }
            }
        }

        cobranza.setDebitoTotal(cobranza.getDebitoMiercoles() + cobranza.getDebitoJueves() + cobranza.getDebitoViernes());
        cobranza.setPrestamos(prestamos);

        return cobranza;
    }

    @GetMapping(path = "/cobranza-gerencia/{gerencia}/{anio}/{semana}")
    public @ResponseBody ArrayList<Cobranza> getCobranzaByGerencia(@PathVariable("gerencia") String gerencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana){
        ArrayList<Cobranza> cobranzas = new ArrayList<>();

        ArrayList<PrestamoPago> prestamoPagos = prestamoPagoController.getPrestamoPagoForCobranzaByGerencia(gerencia, anio, semana);
        HashSet<String> agencias = new HashSet<>();

        for(PrestamoPago prestamoPago: prestamoPagos){
            agencias.add(prestamoPago.getPrestamo().getAgente());
        }

        for(String agencia: agencias){
            Cobranza cobranza = getCobranzaByAgencia(agencia, anio, semana);
            cobranzas.add(cobranza);
        }

        return cobranzas;
    }

    @GetMapping(path = "/dashboard-agencia/{agencia}/{anio}/{semana}")
    public @ResponseBody Dashboard getDashboardByAgenciaAnioAndWeek(@PathVariable("agencia") String agencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana){
        MyUtil myUtil = new MyUtil();
        myUtil.prestamoPagos = prestamoPagoController.getPrestamoPagoForDashboardByAgencia(agencia, anio, semana);
        myUtil.verificarDatosSemanales();
        Dashboard dashboard = myUtil.dashboard;


        return dashboard;
    }

    @GetMapping(path = "/dashboard-gerencia/{gerencia}/{anio}/{semana}")
    public @ResponseBody ArrayList<Dashboard> getDashboardByGerencia(@PathVariable("gerencia") String gerencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana){
        ArrayList<Dashboard> dashboards = new ArrayList<>();
        ArrayList<PrestamoPago> prestamoPagos = prestamoPagoController.getPrestamoPagoForDashboardByGerencia(gerencia, anio, semana);
        HashSet<String> agencias = new HashSet<>();

        for(PrestamoPago prestamoPago: prestamoPagos){
            agencias.add(prestamoPago.getPrestamo().getAgente());
        }

        for(String agencia: agencias){
            Dashboard dashboard = getDashboardByAgenciaAnioAndWeek(agencia, anio, semana);
            dashboards.add(dashboard);
        }

        return dashboards;
    }

    @PutMapping(path = "/liquidacion-pago")
    public @ResponseBody String setLiquidacionPrestamo(@RequestBody Liquidacion liquidacion){

        PrestamoPago prestamoPago = prestamoPagoController.getPrestamoPagoForLiquidacion(liquidacion);

        PrestamoModel prestamo = prestamoPago.getPrestamo();
        prestamo.setWkDescu(liquidacion.getSemana() + "-" + liquidacion.getAnio());
        prestamo.setDescuento(liquidacion.getDescuento());
        prestamo.setSaldo(0);

        PagoModel pago = prestamoPago.getPago();
        pago.setCierraCon(0);

        prestamoRepository.save(prestamo);
        pagoRepository.save(pago);

        return "Liquidación Cargada con Éxito";
    }
}
package tech.calaverita.reporterloanssql.threads.pwa;

import tech.calaverita.reporterloanssql.helpers.pwa.PWAUtil;
import tech.calaverita.reporterloanssql.models.PagoModel;
import tech.calaverita.reporterloanssql.models.PagoVistaModel;
import tech.calaverita.reporterloanssql.models.VisitaModel;
import tech.calaverita.reporterloanssql.pojos.pwa.PagoHistoricoPWA;
import tech.calaverita.reporterloanssql.services.PagoService;
import tech.calaverita.reporterloanssql.services.VisitaService;

import java.util.ArrayList;

public class PagoHistoricoPWAThread implements Runnable {
    PagoVistaModel pagoVistaModel;
    PagoHistoricoPWA pagoHistoricoPWA;
    ArrayList<PagoModel> pagoModels;

    public PagoHistoricoPWAThread(PagoVistaModel pagoVistaModel, PagoHistoricoPWA pagoHistoricoPWA) {
        this.pagoVistaModel = pagoVistaModel;
        this.pagoHistoricoPWA = pagoHistoricoPWA;
    }

    @Override
    public void run() {
        getPagoHistoricoPWAFromPagoVistaModel();
    }

    public void getPagoHistoricoPWAFromPagoVistaModel() {
        pagoHistoricoPWA.setMonto(pagoVistaModel.getMonto());
        pagoHistoricoPWA.setTipo(pagoVistaModel.getTipo());
        pagoHistoricoPWA.setPrestamoId(pagoVistaModel.getPrestamoId());
        pagoHistoricoPWA.setPrestamo(pagoVistaModel.getPrestamo());
        pagoHistoricoPWA.setSemana(pagoVistaModel.getSemana());
        pagoHistoricoPWA.setAnio(pagoVistaModel.getAnio());
        pagoHistoricoPWA.setTarifa(pagoVistaModel.getTarifa());
        pagoHistoricoPWA.setCliente(pagoVistaModel.getCliente());
        pagoHistoricoPWA.setAgente(pagoVistaModel.getAgente());
        pagoHistoricoPWA.setIdentificador(pagoVistaModel.getIdentificador());
        pagoHistoricoPWA.setPagos(PWAUtil.getPagoPWAsFromPagoModels(PagoService.findPagoModelsByPrestamoIdAnioAndSemana(pagoHistoricoPWA.getPrestamoId(), pagoHistoricoPWA.getAnio(), pagoHistoricoPWA.getSemana())));
        pagoHistoricoPWA.setVisitas(VisitaService.findVisitaModelsByPrestamoIdAnioAndSemana(pagoHistoricoPWA.getPrestamoId(), pagoHistoricoPWA.getAnio(), pagoHistoricoPWA.getSemana()));
    }

}

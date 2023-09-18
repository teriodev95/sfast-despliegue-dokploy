package tech.calaverita.reporterloanssql.threads.pwa;

import tech.calaverita.reporterloanssql.utils.pwa.PWAUtil;
import tech.calaverita.reporterloanssql.models.PagoAgrupadoModel;
import tech.calaverita.reporterloanssql.models.PagoModel;
import tech.calaverita.reporterloanssql.pojos.pwa.PagoHistoricoPWA;
import tech.calaverita.reporterloanssql.services.PagoService;
import tech.calaverita.reporterloanssql.services.VisitaService;

import java.util.ArrayList;

public class PagoHistoricoPWAThread implements Runnable {
    PagoAgrupadoModel pagoAgrupadoModel;
    PagoHistoricoPWA pagoHistoricoPWA;
    ArrayList<PagoModel> pagoModels;

    public PagoHistoricoPWAThread(PagoAgrupadoModel pagoAgrupadoModel, PagoHistoricoPWA pagoHistoricoPWA) {
        this.pagoAgrupadoModel = pagoAgrupadoModel;
        this.pagoHistoricoPWA = pagoHistoricoPWA;
    }

    @Override
    public void run() {
        getPagoHistoricoPWAFromPagoVistaModel();
    }

    public void getPagoHistoricoPWAFromPagoVistaModel() {
        pagoHistoricoPWA.setMonto(pagoAgrupadoModel.getMonto());
        pagoHistoricoPWA.setTipo(pagoAgrupadoModel.getTipo());
        pagoHistoricoPWA.setPrestamoId(pagoAgrupadoModel.getPrestamoId());
        pagoHistoricoPWA.setPrestamo(pagoAgrupadoModel.getPrestamo());
        pagoHistoricoPWA.setSemana(pagoAgrupadoModel.getSemana());
        pagoHistoricoPWA.setAnio(pagoAgrupadoModel.getAnio());
        pagoHistoricoPWA.setTarifa(pagoAgrupadoModel.getTarifa());
        pagoHistoricoPWA.setCliente(pagoAgrupadoModel.getCliente());
        pagoHistoricoPWA.setAgente(pagoAgrupadoModel.getAgente());
        pagoHistoricoPWA.setIdentificador(pagoAgrupadoModel.getIdentificador());
        pagoHistoricoPWA.setPagos(PWAUtil.getPagoPWAsFromPagoModels(PagoService.findPagoModelsByPrestamoIdAnioAndSemana(pagoHistoricoPWA.getPrestamoId(), pagoHistoricoPWA.getAnio(), pagoHistoricoPWA.getSemana())));
        pagoHistoricoPWA.setVisitas(VisitaService.findVisitaModelsByPrestamoIdAnioAndSemana(pagoHistoricoPWA.getPrestamoId(), pagoHistoricoPWA.getAnio(), pagoHistoricoPWA.getSemana()));
    }

}

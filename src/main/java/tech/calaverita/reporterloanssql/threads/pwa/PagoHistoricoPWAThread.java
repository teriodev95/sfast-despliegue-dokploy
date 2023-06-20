package tech.calaverita.reporterloanssql.threads.pwa;

import tech.calaverita.reporterloanssql.helpers.pwa.PWAUtil;
import tech.calaverita.reporterloanssql.models.PagoModel;
import tech.calaverita.reporterloanssql.models.PagoVistaModel;
import tech.calaverita.reporterloanssql.models.VisitaModel;
import tech.calaverita.reporterloanssql.pojos.pwa.PagoHistoricoPWA;
import tech.calaverita.reporterloanssql.services.VisitaService;

import java.util.ArrayList;

public class PagoHistoricoPWAThread implements Runnable {
    PagoVistaModel pagoVistaModel;
    PagoHistoricoPWA pagoHistoricoPWA;
    ArrayList<PagoModel> pagoModels;
    ArrayList<VisitaModel> visitaModels;

    public PagoHistoricoPWAThread(PagoVistaModel pagoVistaModel, PagoHistoricoPWA pagoHistoricoPWA, ArrayList<PagoModel> pagoModels, ArrayList<VisitaModel> visitaModels) {
        this.pagoVistaModel = pagoVistaModel;
        this.pagoHistoricoPWA = pagoHistoricoPWA;
        this.pagoModels = pagoModels;
        this.visitaModels = visitaModels;
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

        ArrayList<PagoModel> pagoModelsAux = new ArrayList<>();
        ArrayList<VisitaModel> visitaModelsAux = new ArrayList<>();

        for (PagoModel pagoModel : pagoModels) {
            if (pagoModel.getPrestamoId().equals(pagoHistoricoPWA.getPrestamoId())) {
                pagoModelsAux.add(pagoModel);
            }
        }

        for (VisitaModel visitaModel : visitaModels) {
            if (visitaModel.getPrestamoId().equals(pagoHistoricoPWA.getPrestamoId())) {
                visitaModelsAux.add(visitaModel);
            }
        }

        pagoHistoricoPWA.setPagos(PWAUtil.getPagoPWAsFromPagoModels(pagoModelsAux));
        pagoHistoricoPWA.setVisitas(visitaModelsAux);
    }

}

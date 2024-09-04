package tech.calaverita.sfast_xpress.threads.pwa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.calaverita.sfast_xpress.models.mariaDB.PagoModel;
import tech.calaverita.sfast_xpress.pojos.PWA.PagoHistoricoPWA;
import tech.calaverita.sfast_xpress.services.PagoService;
import tech.calaverita.sfast_xpress.services.VisitaService;
import tech.calaverita.sfast_xpress.utils.pwa.PWAUtil;

@Component
public class PagoHistoricoPWAThread implements Runnable {
    private PagoModel pagoModel;
    private PagoHistoricoPWA pagoHistoricoPWA;
    private static PagoService pagoService;
    private static VisitaService visitaService;

    @Autowired
    private PagoHistoricoPWAThread(
            PagoService pagServ_S,
            VisitaService visServ_S) {
        PagoHistoricoPWAThread.pagoService = pagServ_S;
        PagoHistoricoPWAThread.visitaService = visServ_S;
    }

    public PagoHistoricoPWAThread(
            PagoModel pagoModel,
            PagoHistoricoPWA pagoHistoricoPWA) {
        this.pagoModel = pagoModel;
        this.pagoHistoricoPWA = pagoHistoricoPWA;
    }

    @Override
    public void run() {
        getPagoHistoricoPWAFromPagoVistaModel();
    }

    public void getPagoHistoricoPWAFromPagoVistaModel() {
        pagoHistoricoPWA.setMonto(pagoModel.getMonto());
        pagoHistoricoPWA.setTipo(pagoModel.getTipo());
        pagoHistoricoPWA.setPrestamoId(pagoModel.getPrestamoId());
        pagoHistoricoPWA.setPrestamo(pagoModel.getPrestamo());
        pagoHistoricoPWA.setSemana(pagoModel.getSemana());
        pagoHistoricoPWA.setAnio(pagoModel.getAnio());
        pagoHistoricoPWA.setTarifa(pagoModel.getTarifa());
        pagoHistoricoPWA.setCliente(pagoModel.getCliente());
        pagoHistoricoPWA.setAgente(pagoModel.getAgente());
        pagoHistoricoPWA.setIdentificador(pagoModel.getIdentificador());

        boolean esPrimerPago = false;
        pagoHistoricoPWA.setPagos(PWAUtil.darrpagoPwaFromPagoModels(PagoHistoricoPWAThread.pagoService
                .findByPrestamoIdAnioSemanaAndEsPrimerPago(pagoHistoricoPWA.getPrestamoId(),
                        pagoHistoricoPWA.getAnio(), pagoHistoricoPWA.getSemana(), esPrimerPago)));

        pagoHistoricoPWA.setVisitas(PagoHistoricoPWAThread.visitaService
                .findByPrestamoIdAnioAndSemana(pagoHistoricoPWA.getPrestamoId(), pagoHistoricoPWA.getAnio(),
                        pagoHistoricoPWA.getSemana()));
    }

}

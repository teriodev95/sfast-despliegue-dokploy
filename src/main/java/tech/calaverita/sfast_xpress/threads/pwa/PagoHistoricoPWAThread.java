package tech.calaverita.sfast_xpress.threads.pwa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PagoAgrupadoModel;
import tech.calaverita.sfast_xpress.pojos.PWA.PagoHistoricoPWA;
import tech.calaverita.sfast_xpress.services.PagoService;
import tech.calaverita.sfast_xpress.services.VisitaService;
import tech.calaverita.sfast_xpress.utils.pwa.PWAUtil;

@Component
public class PagoHistoricoPWAThread implements Runnable {
    private PagoAgrupadoModel pagoAgrupadoModel;
    private PagoHistoricoPWA pagoHistoricoPWA;
    private static PagoService pagoService;
    private static VisitaService visitaService;

    @Autowired
    private PagoHistoricoPWAThread(
            PagoService pagServ_S,
            VisitaService visServ_S
    ) {
        PagoHistoricoPWAThread.pagoService = pagServ_S;
        PagoHistoricoPWAThread.visitaService = visServ_S;
    }

    public PagoHistoricoPWAThread(
            PagoAgrupadoModel pagoAgrupadoModel,
            PagoHistoricoPWA pagoHistoricoPWA
    ) {
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

        boolean esPrimerPago = false;
        pagoHistoricoPWA.setPagos(PWAUtil.darrpagoPwaFromPagoModels(PagoHistoricoPWAThread
                .pagoService.findByPrestamoIdAnioSemanaAndEsPrimerPago(pagoHistoricoPWA.getPrestamoId(),
                        pagoHistoricoPWA.getAnio(), pagoHistoricoPWA.getSemana(), esPrimerPago)));

        pagoHistoricoPWA.setVisitas(PagoHistoricoPWAThread.visitaService
                .findByPrestamoIdAnioAndSemana(pagoHistoricoPWA.getPrestamoId(), pagoHistoricoPWA.getAnio(),
                        pagoHistoricoPWA.getSemana()));
    }

}

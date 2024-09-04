package tech.calaverita.sfast_xpress.threads.pwa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.calaverita.sfast_xpress.models.mariaDB.dynamic.PagoDynamicModel;
import tech.calaverita.sfast_xpress.pojos.PWA.PagoHistoricoPWA;
import tech.calaverita.sfast_xpress.services.PagoService;
import tech.calaverita.sfast_xpress.services.VisitaService;
import tech.calaverita.sfast_xpress.utils.pwa.PWAUtil;

@Component
public class PagoHistoricoPWAThread implements Runnable {
    private PagoDynamicModel pagoDynamicModel;
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
            PagoDynamicModel pagoDynamicModel,
            PagoHistoricoPWA pagoHistoricoPWA
    ) {
        this.pagoDynamicModel = pagoDynamicModel;
        this.pagoHistoricoPWA = pagoHistoricoPWA;
    }

    @Override
    public void run() {
        getPagoHistoricoPWAFromPagoVistaModel();
    }

    public void getPagoHistoricoPWAFromPagoVistaModel() {
        pagoHistoricoPWA.setMonto(pagoDynamicModel.getMonto());
        pagoHistoricoPWA.setTipo(pagoDynamicModel.getTipo());
        pagoHistoricoPWA.setPrestamoId(pagoDynamicModel.getPrestamoId());
        pagoHistoricoPWA.setPrestamo(pagoDynamicModel.getPrestamo());
        pagoHistoricoPWA.setSemana(pagoDynamicModel.getSemana());
        pagoHistoricoPWA.setAnio(pagoDynamicModel.getAnio());
        pagoHistoricoPWA.setTarifa(pagoDynamicModel.getTarifa());
        pagoHistoricoPWA.setCliente(pagoDynamicModel.getCliente());
        pagoHistoricoPWA.setAgente(pagoDynamicModel.getAgencia());
        pagoHistoricoPWA.setIdentificador(pagoDynamicModel.getIdentificador());

        boolean esPrimerPago = false;
        pagoHistoricoPWA.setPagos(PWAUtil.darrpagoPwaFromPagoModels(PagoHistoricoPWAThread
                .pagoService.findByPrestamoIdAnioSemanaAndEsPrimerPago(pagoHistoricoPWA.getPrestamoId(),
                        pagoHistoricoPWA.getAnio(), pagoHistoricoPWA.getSemana(), esPrimerPago)));

        pagoHistoricoPWA.setVisitas(PagoHistoricoPWAThread.visitaService
                .findByPrestamoIdAnioAndSemana(pagoHistoricoPWA.getPrestamoId(), pagoHistoricoPWA.getAnio(),
                        pagoHistoricoPWA.getSemana()));
    }

}

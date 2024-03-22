package tech.calaverita.reporterloanssql.threads.pwa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.calaverita.reporterloanssql.models.view.PagoAgrupadoModel;
import tech.calaverita.reporterloanssql.pojos.PWA.PagoHistoricoPWA;
import tech.calaverita.reporterloanssql.services.PagoService;
import tech.calaverita.reporterloanssql.services.VisitaService;
import tech.calaverita.reporterloanssql.utils.pwa.PWAUtil;

@Component
public class PagoHistoricoPWAThread implements Runnable {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private PagoAgrupadoModel pagoAgrupadoModel;
    private PagoHistoricoPWA pagoHistoricoPWA;
    private static PagoService pagServ;
    private static VisitaService visServ;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    @Autowired
    private PagoHistoricoPWAThread(
            PagoService pagServ_S,
            VisitaService visServ_S
    ) {
        PagoHistoricoPWAThread.pagServ = pagServ_S;
        PagoHistoricoPWAThread.visServ = visServ_S;
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public PagoHistoricoPWAThread(
            PagoAgrupadoModel pagoAgrupadoModel,
            PagoHistoricoPWA pagoHistoricoPWA
    ) {
        this.pagoAgrupadoModel = pagoAgrupadoModel;
        this.pagoHistoricoPWA = pagoHistoricoPWA;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public void run() {
        getPagoHistoricoPWAFromPagoVistaModel();
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
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
        pagoHistoricoPWA.setPagos(PWAUtil.darrpagoPwaFromPagoModels(PagoHistoricoPWAThread.pagServ.darrpagModFindByPrestamoIdAnioSemanaAndNoPrimerPago(pagoHistoricoPWA.getPrestamoId(), pagoHistoricoPWA.getAnio(), pagoHistoricoPWA.getSemana())));
        pagoHistoricoPWA.setVisitas(PagoHistoricoPWAThread.visServ.darrVisModFindByPrestamoIdAnioAndSemana(pagoHistoricoPWA.getPrestamoId(), pagoHistoricoPWA.getAnio(), pagoHistoricoPWA.getSemana()));
    }

}

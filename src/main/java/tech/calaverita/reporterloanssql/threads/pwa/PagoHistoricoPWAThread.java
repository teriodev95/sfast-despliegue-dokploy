package tech.calaverita.reporterloanssql.threads.pwa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.calaverita.reporterloanssql.persistence.entities.view.PagoAgrupadoEntity;
import tech.calaverita.reporterloanssql.pojos.pwa.PagoHistoricoPWA;
import tech.calaverita.reporterloanssql.services.PagoService;
import tech.calaverita.reporterloanssql.services.VisitaService;
import tech.calaverita.reporterloanssql.utils.pwa.PWAUtil;

@Component
public class PagoHistoricoPWAThread implements Runnable {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private PagoAgrupadoEntity pagoAgrupadoEntity;
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
            PagoAgrupadoEntity pagoAgrupadoEntity,
            PagoHistoricoPWA pagoHistoricoPWA
    ) {
        this.pagoAgrupadoEntity = pagoAgrupadoEntity;
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
        pagoHistoricoPWA.setMonto(pagoAgrupadoEntity.getMonto());
        pagoHistoricoPWA.setTipo(pagoAgrupadoEntity.getTipo());
        pagoHistoricoPWA.setPrestamoId(pagoAgrupadoEntity.getPrestamoId());
        pagoHistoricoPWA.setPrestamo(pagoAgrupadoEntity.getPrestamo());
        pagoHistoricoPWA.setSemana(pagoAgrupadoEntity.getSemana());
        pagoHistoricoPWA.setAnio(pagoAgrupadoEntity.getAnio());
        pagoHistoricoPWA.setTarifa(pagoAgrupadoEntity.getTarifa());
        pagoHistoricoPWA.setCliente(pagoAgrupadoEntity.getCliente());
        pagoHistoricoPWA.setAgente(pagoAgrupadoEntity.getAgente());
        pagoHistoricoPWA.setIdentificador(pagoAgrupadoEntity.getIdentificador());
        pagoHistoricoPWA.setPagos(PWAUtil.darrpagoPwaFromPagoModels(PagoHistoricoPWAThread.pagServ.darrpagModFindByPrestamoIdAnioSemanaAndNoPrimerPago(pagoHistoricoPWA.getPrestamoId(), pagoHistoricoPWA.getAnio(), pagoHistoricoPWA.getSemana())));
        pagoHistoricoPWA.setVisitas(PagoHistoricoPWAThread.visServ.darrVisModFindByPrestamoIdAnioAndSemana(pagoHistoricoPWA.getPrestamoId(), pagoHistoricoPWA.getAnio(), pagoHistoricoPWA.getSemana()));
    }

}

package tech.calaverita.reporterloanssql.threads.pwa;

import tech.calaverita.reporterloanssql.persistence.entities.PagoEntity;
import tech.calaverita.reporterloanssql.pojos.pwa.PagoPWA;

public class PagoPWAThread implements Runnable {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private final PagoEntity pagoEntity;
    private final PagoPWA pagoPWA;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    public PagoPWAThread(
            PagoEntity pagoEntity,
            PagoPWA pagoPWA
    ) {
        this.pagoEntity = pagoEntity;
        this.pagoPWA = pagoPWA;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public void run() {
        getPagoPWAFromPagoModel();
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public void getPagoPWAFromPagoModel() {
        pagoPWA.setPagoId(pagoEntity.getPagoId());
        pagoPWA.setMonto(pagoEntity.getMonto());
        pagoPWA.setEsPrimerPago(pagoEntity.getEsPrimerPago());
        pagoPWA.setAbreCon(pagoEntity.getAbreCon());
        pagoPWA.setCierraCon(pagoEntity.getCierraCon());
        pagoPWA.setTipo(pagoEntity.getTipo());
        pagoPWA.setCreadoDesde(pagoEntity.getCreadoDesde());
        pagoPWA.setFechaPago(pagoEntity.getFechaPago());
        pagoPWA.setLat(pagoEntity.getLat());
        pagoPWA.setLng(pagoEntity.getLng());
        pagoPWA.setComentario(pagoEntity.getComentario());
        pagoPWA.setDatosMigracion(pagoEntity.getDatosMigracion());
        pagoPWA.setCreatedAt(pagoEntity.getCreatedAt());
        pagoPWA.setUpdatedAt(pagoEntity.getUpdatedAt());
        pagoPWA.setLog(pagoEntity.getLog());
        pagoPWA.setQuienPago(pagoEntity.getQuienPago());
    }
}

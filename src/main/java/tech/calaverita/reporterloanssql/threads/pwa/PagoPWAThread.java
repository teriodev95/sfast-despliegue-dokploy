package tech.calaverita.reporterloanssql.threads.pwa;

import tech.calaverita.reporterloanssql.models.mariaDB.PagoModel;
import tech.calaverita.reporterloanssql.pojos.PWA.PagoPWA;

public class PagoPWAThread implements Runnable {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private final PagoModel pagoModel;
    private final PagoPWA pagoPWA;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    public PagoPWAThread(
            PagoModel pagoModel,
            PagoPWA pagoPWA
    ) {
        this.pagoModel = pagoModel;
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
        pagoPWA.setPagoId(pagoModel.getPagoId());
        pagoPWA.setMonto(pagoModel.getMonto());
        pagoPWA.setEsPrimerPago(pagoModel.getEsPrimerPago());
        pagoPWA.setAbreCon(pagoModel.getAbreCon());
        pagoPWA.setCierraCon(pagoModel.getCierraCon());
        pagoPWA.setTipo(pagoModel.getTipo());
        pagoPWA.setCreadoDesde(pagoModel.getCreadoDesde());
        pagoPWA.setFechaPago(pagoModel.getFechaPago());
        pagoPWA.setLat(pagoModel.getLat());
        pagoPWA.setLng(pagoModel.getLng());
        pagoPWA.setComentario(pagoModel.getComentario());
        pagoPWA.setDatosMigracion(pagoModel.getDatosMigracion());
        pagoPWA.setCreatedAt(pagoModel.getCreatedAt());
        pagoPWA.setUpdatedAt(pagoModel.getUpdatedAt());
        pagoPWA.setLog(pagoModel.getLog());
        pagoPWA.setQuienPago(pagoModel.getQuienPago());
    }
}

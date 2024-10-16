package tech.calaverita.sfast_xpress.DTOs.solicitud;

import lombok.Data;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class DatosCreditoDTO {
    private Object montoSolicitado;
    private String nivelCliente;
    private String plazoSemanas;
    private Object tarifaSemanal;
    private Object primerPago;
    private Object cargo;
    private Object totalPagar;
    private String tipoCredito;
    private String diaDeEntrega;
    private String horaDeEntrega;
    private String diaDePago;
    private String horaDePago;
    private String identificadorCredito;

    public DatosCreditoDTO(Object montoSolicitado, String nivelCliente, String plazoSemanas, Object tarifaSemanal,
            Object primerPago, Object cargo, Object totalPagar, String tipoCredito, String diaDeEntrega,
            String horaDeEntrega, String diaDePago, String horaDePago, String identificadorCredito) {
        this.montoSolicitado = montoSolicitado;
        this.nivelCliente = nivelCliente;
        this.plazoSemanas = plazoSemanas;
        this.tarifaSemanal = tarifaSemanal;
        this.primerPago = primerPago;
        this.cargo = cargo;
        this.totalPagar = totalPagar;
        this.tipoCredito = tipoCredito;
        this.diaDeEntrega = diaDeEntrega;
        this.horaDeEntrega = horaDeEntrega;
        this.diaDePago = diaDePago;
        this.horaDePago = horaDePago;
        this.identificadorCredito = identificadorCredito;

        monetaryToDouble();
    }

    public void monetaryToDouble() {
        this.montoSolicitado = MyUtil.monetaryToDouble(this.montoSolicitado);
        this.tarifaSemanal = MyUtil.monetaryToDouble(this.tarifaSemanal);
        this.primerPago = MyUtil.monetaryToDouble(this.primerPago);
        this.cargo = MyUtil.monetaryToDouble(this.cargo);
        this.totalPagar = MyUtil.monetaryToDouble(this.totalPagar);
    }
}

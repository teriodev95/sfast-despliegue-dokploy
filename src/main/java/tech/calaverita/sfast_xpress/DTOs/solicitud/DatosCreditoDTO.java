package tech.calaverita.sfast_xpress.DTOs.solicitud;

import lombok.Data;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class DatosCreditoDTO {
    private Double montoSolicitado;
    private String nivelCliente;
    private String plazoSemanas;
    private Double tarifaSemanal;
    private Double primerPago;
    private Double cargo;
    private Double totalPagar;
    private String tipoCredito;
    private String diaDeEntrega;
    private String horaDeEntrega;
    private String diaDePago;
    private String horaDePago;
    private String identificadorCredito;

    public DatosCreditoDTO(String montoSolicitado, String nivelCliente, String plazoSemanas, String tarifaSemanal,
            String primerPago, String cargo, String totalPagar, String tipoCredito, String diaDeEntrega,
            String horaDeEntrega, String diaDePago, String horaDePago, String identificadorCredito) {
        this.nivelCliente = nivelCliente;
        this.plazoSemanas = plazoSemanas;
        this.tipoCredito = tipoCredito;
        this.diaDeEntrega = diaDeEntrega;
        this.horaDeEntrega = horaDeEntrega;
        this.diaDePago = diaDePago;
        this.horaDePago = horaDePago;
        this.identificadorCredito = identificadorCredito;
        this.montoSolicitado = MyUtil.monetaryToDouble(montoSolicitado);
        this.tarifaSemanal = MyUtil.monetaryToDouble(tarifaSemanal);
        this.primerPago = MyUtil.monetaryToDouble(primerPago);
        this.cargo = MyUtil.monetaryToDouble(cargo);
        this.totalPagar = MyUtil.monetaryToDouble(totalPagar);
    }
}

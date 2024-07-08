package tech.calaverita.sfast_xpress.DTOs.solicitud;

import lombok.Data;

@Data
public class DatosCreditoDTO {
    private String montoSolicitado;
    private String nivelCliente;
    private String plazoSemanas;
    private String tarifaSemanal;
    private String primerPago;
    private String cargo;
    private String totalPagar;
    private String tipoCredito;
    private String diaDeEntrega;
    private String horaDeEntrega;
    private String diaDePago;
    private String horaDePago;
    private String identificadorCredito;
}

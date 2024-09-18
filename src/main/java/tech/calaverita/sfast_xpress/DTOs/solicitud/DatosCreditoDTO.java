package tech.calaverita.sfast_xpress.DTOs.solicitud;

import lombok.Data;

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
}

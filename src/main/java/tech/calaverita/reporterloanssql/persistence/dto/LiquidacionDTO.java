package tech.calaverita.reporterloanssql.persistence.dto;

import lombok.Data;

@Data
public class LiquidacionDTO {
    private String cliente;
    private String identificador;
    private String semEntrega;
    private Double entregado;
    private Double cargo;
    private Double montoTotal;
    private Double cobrado;
    private Double saldo;
    private Double descuentoDinero;
    private Integer descuentoPorcentaje;
    private Integer semanasTranscurridas;
    private Double liquidaCon;
    private String prestamoId;
}

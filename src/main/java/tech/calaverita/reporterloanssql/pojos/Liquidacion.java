package tech.calaverita.reporterloanssql.pojos;

import lombok.Data;

@Data
public class Liquidacion {
    private String prestamoId;
    private double monto;
    private double descuento;
    private int anio;
    private int semana;
}

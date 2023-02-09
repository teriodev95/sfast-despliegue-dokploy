package tech.calaverita.reporterloanssql.pojos;

import lombok.Data;

@Data
public class Dashboard {
    private String gerencia;
    private String agencia;
    private int anio;
    private int semana;
    private int clientes;
    private int noPagos;
    private int numeroLiquidaciones;
    private int pagosReducidos;
    private double debitoMiercoles;
    private double debitoJueves;
    private double debitoViernes;
    private double debitoTotal;
    private double rendimiento;
    private double totalDeDescuento;
    private double totalCobranzaPura;
    private double montoExcedente;
    private double multas;
    private double liquidaciones;
    private double cobranzaTotal;
    private double montoDeDebitoFaltante;
}

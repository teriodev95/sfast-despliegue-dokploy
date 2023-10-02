package tech.calaverita.reporterloanssql.pojos.pwa;

import lombok.Data;

@Data
public class CierreSemanalPWA {
    private String zona;
    private String gerente;
    private String agencia;
    private String agente;
    private Double rendimiento;
    private String nivel;
    private Double cobranzaPura;
    private Double montoExcedente;
    private Double liquidaciones;
    private Double multas;
    private Double otrosIngresosAgente;
    private Double todalIngresosAgente;
    private Integer porcentajeComisionPorCobranza;
    private Integer porcentajeBonoMensual;
    private Integer semana;
    private Integer anio;
    private Integer dia;
    private String mes;
    private Integer clientes;
    private Integer pagosReducidos;
    private Integer noPagos;
    private Integer numeroLiquidaciones;
    private Double asignacionesEgresosAgente;
    private Double otrosEgresosAgente;
    private Double efectivoEntregadoCierreEgresosAgente;
    private Double totalEgresos;
    private Double pagoComisionCobranzaEgresosGerente;
    private Double pagoComisionVentasEgresosGerente;
    private Double bonosEgresosGerente;
    private Double efectivoRestanteCierreEgresosGerente;
}

package tech.calaverita.reporterloanssql.dto.reporte_general_gerencia;

import lombok.Data;

@Data
public class DashboardReporteGeneralGerenciaDTO {
    private String concepto;
    private Double debitoTotal;
    private Integer clientesCobrados;
    private Double cobranzaPura;
    private Double diferenciaCobranzaPuraVsDebitoTotal;
    private Double porcentajeCobranzaPura;
    private Double diferenciaActualVsDiferenciaAnterior;
    private Double cobranzaTotal;
    private Double excedente;
    private Double totalVentas;
}

package tech.calaverita.reporterloanssql.persistence.dto.reporte_general_gerencia;

import lombok.Data;

@Data
public class DashboardReporteGeneralGerencia {
    String concepto;
    Double debitoTotal;
    Integer clientesCobrados;
    Double cobranzaPura;
    Double diferenciaCobranzaPuraVsDebitoTotal;
    Double porcentajeCobranzaPura;
    Double diferenciaActualVsDiferenciaAnterior;
    Double cobranzaTotal;
    Double excedente;
    Double totalVentas;
}

package tech.calaverita.reporterloanssql.dto.reporte_general_gerencia;

import lombok.Data;

@Data
public class DashboardReporteGeneralGerencia {
    String concepto;
    Double debitoTotal;
    Integer clientesTotalesCobrados;
    Integer clientesCobradosMiercoles;
    Integer clientesCobradosJueves;
    Integer clientesTotales;
    Double cobranzaPura;
    Double porcentajeCobranzaPura;
    Double diferenciaCobranzaPuraVsDebitoTotal;
    Double diferenciaActualVsDiferenciaAnterior;
    Double cobranzaTotal;
    Double excedente;
    Double totalVentas;
}

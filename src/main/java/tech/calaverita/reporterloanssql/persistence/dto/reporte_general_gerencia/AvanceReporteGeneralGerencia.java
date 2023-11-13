package tech.calaverita.reporterloanssql.persistence.dto.reporte_general_gerencia;

import lombok.Data;

@Data
public class AvanceReporteGeneralGerencia {
    String concepto;
    Double debitoTotal;
    Double porcentajeCobranzaPura;
}

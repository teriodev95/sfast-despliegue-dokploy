package tech.calaverita.sfast_xpress.DTOs.reporte_general_gerencia;

import lombok.Data;

@Data
public class AvanceReporteGeneralGerencia {
    String concepto;
    Double debitoTotal;
    Double porcentajeCobranzaPura;
}

package tech.calaverita.sfast_xpress.dto.reporte_general_gerencia;

import lombok.Data;

@Data
public class AvanceReporteGeneralGerenciaDTO {
    private String concepto;
    private Double debitoTotal;
    private Double cobranzaPura;
    private Double porcentajeCobranzaPura;
}

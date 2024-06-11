package tech.calaverita.sfast_xpress.dto.reporte_diario_agencias;

import lombok.Data;

@Data
public class AgenciaReporteDiarioAgenciasDTO {
    private String agencia;
    private String agente;
    private Double efectivoActualAgente;
    private Boolean isAgenciaCerrada;
    private DashboardSemanaActualReporteDiarioAgenciasDTO semanaActual;
    private DashboardSemanaAnteriorReporteDiarioAgenciasDTO semanaAnterior;
}

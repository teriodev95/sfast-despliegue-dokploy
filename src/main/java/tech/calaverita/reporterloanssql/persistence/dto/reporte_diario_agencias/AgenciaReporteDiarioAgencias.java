package tech.calaverita.reporterloanssql.persistence.dto.reporte_diario_agencias;

import lombok.Data;

@Data
public class AgenciaReporteDiarioAgencias {
    String agencia;
    String agente;
    DashboardSemanaActualReporteDiarioAgencias semanaActual;
    DashboardSemanaAnteriorReporteDiarioAgencias semanaAnterior;
}

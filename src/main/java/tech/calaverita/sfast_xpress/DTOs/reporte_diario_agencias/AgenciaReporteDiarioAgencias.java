package tech.calaverita.sfast_xpress.DTOs.reporte_diario_agencias;

import lombok.Data;

@Data
public class AgenciaReporteDiarioAgencias {
    String nombre;
    String agente;
    DashboardSemanaActualReporteDiarioAgencias semanaActual;
    DashboardSemanaAnteriorReporteDiarioAgencias semanaAnterior;
}

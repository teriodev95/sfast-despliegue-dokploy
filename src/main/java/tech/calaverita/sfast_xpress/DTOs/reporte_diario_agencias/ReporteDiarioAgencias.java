package tech.calaverita.sfast_xpress.DTOs.reporte_diario_agencias;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ReporteDiarioAgencias {
    EncabezadoReporteDiarioAgencias encabezado;
    ArrayList<AgenciaReporteDiarioAgencias> agencias;
}

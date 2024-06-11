package tech.calaverita.sfast_xpress.dto.reporte_general_gerencia;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ReporteGeneralGerencia {
    EncabezadoReporteGeneralGerencia encabezado;
    ArrayList<DashboardReporteGeneralGerencia> dashboards;
    ArrayList<AvanceReporteGeneralGerencia> avances;
    Double perdidaAcumulada;
    Double efectivoGerente;
    Double efectivoCampo;
    Double totalEfectivo;
}

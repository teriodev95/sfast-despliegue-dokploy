package tech.calaverita.sfast_xpress.models.mongoDB;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import tech.calaverita.sfast_xpress.DTOs.reporte_general_gerencia.ArrastreReporteGeneralGerenciaDTO;
import tech.calaverita.sfast_xpress.DTOs.reporte_general_gerencia.AvanceReporteGeneralGerenciaDTO;
import tech.calaverita.sfast_xpress.DTOs.reporte_general_gerencia.DashboardReporteGeneralGerenciaDTO;
import tech.calaverita.sfast_xpress.DTOs.reporte_general_gerencia.EncabezadoReporteGeneralGerenciaDTO;

import java.util.ArrayList;

@Data
@Document(collection = "generales_gerencia")
public class ReporteGeneralGerenciaDocument {
    @Id
    private String id;
    private Double perdidaAcumulada;
    private Double efectivoGerente;
    private Double efectivoCampo;
    private Double totalEfectivo;
    private EncabezadoReporteGeneralGerenciaDTO encabezado;
    private ArrayList<DashboardReporteGeneralGerenciaDTO> dashboards;
    private ArrayList<AvanceReporteGeneralGerenciaDTO> avances;
    private ArrayList<ArrastreReporteGeneralGerenciaDTO> arrastres;
}

package tech.calaverita.sfast_xpress.models.mongoDB;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import tech.calaverita.sfast_xpress.DTOs.reporte_diario_agencias.AgenciaReporteDiarioAgenciasDTO;
import tech.calaverita.sfast_xpress.DTOs.reporte_diario_agencias.EncabezadoReporteDiarioAgenciasDTO;

import java.util.ArrayList;

@Data
@Document(collection = "diarios_agencias")
public class ReporteDiarioAgenciasDocument {
    @Id
    private String id;
    private EncabezadoReporteDiarioAgenciasDTO encabezado;
    private ArrayList<AgenciaReporteDiarioAgenciasDTO> agencias;
}

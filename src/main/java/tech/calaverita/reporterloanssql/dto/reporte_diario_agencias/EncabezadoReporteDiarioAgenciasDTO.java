package tech.calaverita.reporterloanssql.dto.reporte_diario_agencias;

import lombok.Data;

@Data
public class EncabezadoReporteDiarioAgenciasDTO {
    private String sucursal;
    private String zona;
    private String gerente;
    private String seguridad;
    private String fecha;
    private String semana;
    private String hora;
}

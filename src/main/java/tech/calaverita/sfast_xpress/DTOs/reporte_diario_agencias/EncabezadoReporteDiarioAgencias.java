package tech.calaverita.sfast_xpress.DTOs.reporte_diario_agencias;

import lombok.Data;

@Data
public class EncabezadoReporteDiarioAgencias {
    String sucursal;
    String zona;
    String gerente;
    String seguridad;
    String fecha;
    String semana;
    String hora;
}

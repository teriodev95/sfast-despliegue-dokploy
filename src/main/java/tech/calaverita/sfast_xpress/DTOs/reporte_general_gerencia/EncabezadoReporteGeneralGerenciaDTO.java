package tech.calaverita.sfast_xpress.DTOs.reporte_general_gerencia;

import lombok.Data;

@Data
public class EncabezadoReporteGeneralGerenciaDTO {
    private String sucursal;
    private String zona;
    private String gerente;
    private String seguridad;
    private String fecha;
    private Integer semana;
    private String hora;
}

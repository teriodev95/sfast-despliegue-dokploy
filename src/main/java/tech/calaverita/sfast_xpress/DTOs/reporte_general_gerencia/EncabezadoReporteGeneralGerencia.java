package tech.calaverita.sfast_xpress.DTOs.reporte_general_gerencia;

import lombok.Data;

@Data
public class EncabezadoReporteGeneralGerencia {
    String sucursal;
    String zona;
    String gerente;
    String seguridad;
    String fecha;
    Integer semana;
    String hora;
}

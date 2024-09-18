package tech.calaverita.sfast_xpress.DTOs.solicitud;

import lombok.Data;

@Data
public class IngresosDTO {
    private String ocupacion;
    private String nombreEmpresa;
    private String domicilioEmpleo;
    private Double sueldo;
    private String periodoPago;
    private String comprueba;
    private String antiguedad;
    private String otrosIngresos;
    private String otrosIngresosConcepto;
    private Double otrosIngresosMonto;
    private Double otrosIngresosTotalMes;
}

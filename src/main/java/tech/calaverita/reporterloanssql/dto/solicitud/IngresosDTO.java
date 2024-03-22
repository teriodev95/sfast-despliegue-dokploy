package tech.calaverita.reporterloanssql.dto.solicitud;

import lombok.Data;

@Data
public class IngresosDTO {
    private String ocupacion;
    private String nombreEmpresa;
    private String domicilioEmpleo;
    private String sueldo;
    private String periodoPago;
    private String comprueba;
    private String antiguedad;
    private String otrosIngresos;
    private String otrosIngresosConcepto;
    private String otrosIngresosMonto;
    private String otrosIngresosTotalMes;
}

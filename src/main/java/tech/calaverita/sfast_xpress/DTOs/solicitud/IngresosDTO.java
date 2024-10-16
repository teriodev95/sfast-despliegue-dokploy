package tech.calaverita.sfast_xpress.DTOs.solicitud;

import lombok.Data;
import tech.calaverita.sfast_xpress.utils.MyUtil;

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

    public IngresosDTO(String ocupacion, String nombreEmpresa, String domicilioEmpleo, String sueldo,
            String periodoPago, String comprueba, String antiguedad, String otrosIngresos, String otrosIngresosConcepto,
            String otrosIngresosMonto, String otrosIngresosTotalMes) {
        this.ocupacion = ocupacion;
        this.nombreEmpresa = nombreEmpresa;
        this.domicilioEmpleo = domicilioEmpleo;
        this.periodoPago = periodoPago;
        this.comprueba = comprueba;
        this.antiguedad = antiguedad;
        this.otrosIngresos = otrosIngresos;
        this.otrosIngresosConcepto = otrosIngresosConcepto;
        this.sueldo = MyUtil.monetaryToDouble(sueldo);
        this.otrosIngresosMonto = MyUtil.monetaryToDouble(otrosIngresosMonto);
        this.otrosIngresosTotalMes = MyUtil.monetaryToDouble(otrosIngresosTotalMes);
    }
}

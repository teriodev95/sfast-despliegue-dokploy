package tech.calaverita.sfast_xpress.DTOs.solicitud;

import lombok.Data;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class IngresosDTO {
    private String ocupacion;
    private String nombreEmpresa;
    private String domicilioEmpleo;
    private Object sueldo;
    private String periodoPago;
    private String comprueba;
    private String antiguedad;
    private String otrosIngresos;
    private String otrosIngresosConcepto;
    private Object otrosIngresosMonto;
    private Object otrosIngresosTotalMes;

    public IngresosDTO(String ocupacion, String nombreEmpresa, String domicilioEmpleo, Object sueldo,
            String periodoPago, String comprueba, String antiguedad, String otrosIngresos, String otrosIngresosConcepto,
            Object otrosIngresosMonto, Object otrosIngresosTotalMes) {
        this.ocupacion = ocupacion;
        this.nombreEmpresa = nombreEmpresa;
        this.domicilioEmpleo = domicilioEmpleo;
        this.sueldo = sueldo;
        this.periodoPago = periodoPago;
        this.comprueba = comprueba;
        this.antiguedad = antiguedad;
        this.otrosIngresos = otrosIngresos;
        this.otrosIngresosConcepto = otrosIngresosConcepto;
        this.otrosIngresosMonto = otrosIngresosMonto;
        this.otrosIngresosTotalMes = otrosIngresosTotalMes;

        monetaryToDouble();
    }

    public void monetaryToDouble() {
        this.sueldo = MyUtil.monetaryToDouble(this.sueldo);
        this.otrosIngresosMonto = MyUtil.monetaryToDouble(this.otrosIngresosMonto);
        this.otrosIngresosTotalMes = MyUtil.monetaryToDouble(this.otrosIngresosTotalMes);
    }
}

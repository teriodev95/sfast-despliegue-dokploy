package tech.calaverita.sfast_xpress.DTOs.numeros_gerencia.metricas;

import lombok.Data;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class NumerosGerenciaMetricaDto {
    private Object semanaAnterior;
    private Object semanaActual;
    private Object diferencia;

    public NumerosGerenciaMetricaDto() {
        this.semanaAnterior = 0;
        this.semanaActual = 0;
        this.diferencia = 0;
    }

    public NumerosGerenciaMetricaDto(int semanaAnterior, int semanaActual) {
        this();
        this.semanaAnterior = semanaAnterior;
        this.semanaActual = semanaActual;
        this.diferencia = Math.abs(semanaActual - semanaAnterior);
    }

    public NumerosGerenciaMetricaDto(double semanaAnterior, double semanaActual) {
        this();
        this.semanaAnterior = semanaAnterior;
        this.semanaActual = semanaActual;
        this.diferencia = Math.abs(semanaActual - semanaAnterior);
        formatDoubles();
    }

    private void formatDoubles() {
        // To easy code
        boolean isSemanaAnteriorDouble = this.semanaAnterior instanceof Double;
        boolean isSemanaActualDouble = this.semanaActual instanceof Double;
        boolean isDiferenciaDouble = this.diferencia instanceof Double;

        if (isSemanaAnteriorDouble) {
            this.semanaAnterior = Double.isNaN((Double) this.semanaAnterior) ? 0.0 : this.semanaAnterior;
        }
        if (isSemanaActualDouble) {
            this.semanaActual = Double.isNaN((Double) this.semanaActual) ? 0.0 : this.semanaActual;
        }
        if (isDiferenciaDouble) {
            this.diferencia = Double.isNaN((Double) this.diferencia) ? 0.0 : this.diferencia;
        }

        this.semanaAnterior = MyUtil.getDouble((Double) this.semanaAnterior);
        this.semanaActual = MyUtil.getDouble((Double) this.semanaActual);
        this.diferencia = MyUtil.getDouble((Double) this.diferencia);
    }
}

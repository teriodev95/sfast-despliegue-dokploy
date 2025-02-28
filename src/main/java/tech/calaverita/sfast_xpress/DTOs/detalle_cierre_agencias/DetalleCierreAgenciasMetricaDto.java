package tech.calaverita.sfast_xpress.DTOs.detalle_cierre_agencias;

import lombok.Data;

@Data
public class DetalleCierreAgenciasMetricaDto {
    private Integer semanaAnterior;
    private Integer semanaActual;

    public DetalleCierreAgenciasMetricaDto() {
        this.semanaAnterior = 0;
        this.semanaActual = 0;
    }

    public DetalleCierreAgenciasMetricaDto(Integer semanaAnterior, Integer semanaActual) {
        this();
        this.semanaAnterior = semanaAnterior;
        this.semanaActual = semanaActual;
    }
}

package tech.calaverita.sfast_xpress.DTOs.numeros_gerencia.metricas;

import lombok.Data;

@Data
public class NumerosGerenciaMetricaValorDto {
    private Double semanaAnterior;
    private Double semanaActual;
    private Double diferencia;

    public NumerosGerenciaMetricaValorDto() {
        this.semanaAnterior = 0.0;
        this.semanaActual = 0.0;
        this.diferencia = 0.0;
    }

    public NumerosGerenciaMetricaValorDto(Double semanaAnterior, Double semanaActual) {
        this();
        this.semanaAnterior = semanaAnterior;
        this.semanaActual = semanaActual;
        this.diferencia = semanaActual - semanaAnterior;
    }
}

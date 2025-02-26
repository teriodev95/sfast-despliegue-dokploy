package tech.calaverita.sfast_xpress.DTOs.balance_general;

import lombok.Data;

@Data
public class BalanceGeneralMetricaDto {
    private Integer semanaAnterior;
    private Integer semanaActual;

    public BalanceGeneralMetricaDto() {
        this.semanaAnterior = 0;
        this.semanaActual = 0;
    }

    public BalanceGeneralMetricaDto(Integer semanaAnterior, Integer semanaActual) {
        this();
        this.semanaAnterior = semanaAnterior;
        this.semanaActual = semanaActual;
    }
}

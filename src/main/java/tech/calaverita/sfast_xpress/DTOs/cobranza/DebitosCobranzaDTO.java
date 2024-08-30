package tech.calaverita.sfast_xpress.DTOs.cobranza;

import java.util.ArrayList;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoModel;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class DebitosCobranzaDTO {
    private Double debitoMiercoles;
    private Double debitoJueves;
    private Double debitoViernes;
    private Double debitoTotal;

    public DebitosCobranzaDTO(ArrayList<PrestamoModel> prestamoModels) {
        this.debitoMiercoles = MyUtil.getDouble(getDebitoPorDia(prestamoModels, "MIERCOLES"));
        this.debitoJueves = MyUtil.getDouble(getDebitoPorDia(prestamoModels, "JUEVES"));
        this.debitoViernes = MyUtil.getDouble(getDebitoPorDia(prestamoModels, "VIERNES"));
        this.debitoTotal = MyUtil.getDouble(this.debitoMiercoles + this.debitoJueves + this.debitoViernes);
    }

    private Double getDebitoPorDia(ArrayList<PrestamoModel> prestamoModels, String diaDePago) {
        Double debitoPorDia = 0D;

        for (PrestamoModel prestamoModel : prestamoModels) {
            if (prestamoModel.getDiaDePago().equals(diaDePago)) {
                Double tarifa;

                if (prestamoModel.getTarifa() <= prestamoModel.getSaldoAlIniciarSemana()) {
                    tarifa = prestamoModel.getTarifa();
                } else {
                    tarifa = prestamoModel.getSaldoAlIniciarSemana();
                }

                debitoPorDia += tarifa;
            }
        }

        return debitoPorDia;
    }
}

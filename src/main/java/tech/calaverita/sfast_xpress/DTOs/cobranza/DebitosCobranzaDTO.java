package tech.calaverita.sfast_xpress.DTOs.cobranza;

import java.util.ArrayList;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoViewModel;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class DebitosCobranzaDTO {
    private Double debitoMiercoles;
    private Double debitoJueves;
    private Double debitoViernes;
    private Double debitoTotal;

    public DebitosCobranzaDTO(ArrayList<PrestamoViewModel> prestamoViewModels) {
        this.debitoMiercoles = MyUtil.getDouble(getDebitoPorDia(prestamoViewModels, "MIERCOLES"));
        this.debitoJueves = MyUtil.getDouble(getDebitoPorDia(prestamoViewModels, "JUEVES"));
        this.debitoViernes = MyUtil.getDouble(getDebitoPorDia(prestamoViewModels, "VIERNES"));
        this.debitoTotal = MyUtil.getDouble(this.debitoMiercoles + this.debitoJueves + this.debitoViernes);
    }

    private Double getDebitoPorDia(ArrayList<PrestamoViewModel> prestamoViewModels, String diaDePago) {
        Double debitoPorDia = 0D;

        for (PrestamoViewModel prestamoViewModel : prestamoViewModels) {
            if (prestamoViewModel.getDiaDePago().equals(diaDePago)) {
                Double tarifa;

                if (prestamoViewModel.getTarifa() <= prestamoViewModel.getSaldoAlIniciarSemana()) {
                    tarifa = prestamoViewModel.getTarifa();
                } else {
                    tarifa = prestamoViewModel.getSaldoAlIniciarSemana();
                }

                debitoPorDia += tarifa;
            }
        }

        return debitoPorDia;
    }
}

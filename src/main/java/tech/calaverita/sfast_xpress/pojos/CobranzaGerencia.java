package tech.calaverita.sfast_xpress.pojos;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.VentaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.dynamic.PagoDynamicModel;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class CobranzaGerencia {
    private Double cobranzaPura;
    private Double montoExcedente;
    private Double liquidaciones;
    private Double primerosPagos;
    private Double multas;
    private Double otros;

    public CobranzaGerencia() {
        this.cobranzaPura = 0D;
        this.montoExcedente = 0D;
        this.liquidaciones = 0D;
        this.primerosPagos = 0D;
        this.multas = 0D;
        this.otros = 0D;
    }

    public CobranzaGerencia(ArrayList<PagoDynamicModel> pagoDynamicModels, List<VentaModel> ventaModels) {
        this();

        double cobranzaTotal = 0D;

        for (PagoDynamicModel pagoDynamicModel : pagoDynamicModels) {
            // To easy code
            double tarifa = pagoDynamicModel.getAbreCon() < pagoDynamicModel.getTarifa() ? pagoDynamicModel.getAbreCon()
                    : pagoDynamicModel.getTarifa();
            double monto = pagoDynamicModel.getMonto();
            String tipo = pagoDynamicModel.getTipo();

            if (tipo.equals("Multa")) {
                this.multas += monto;
            } else if (tipo.equals("Liquidacion")) {
                this.liquidaciones += monto - tarifa;
            } else if (tipo.equals("Excedente")) {
                this.montoExcedente += monto - tarifa;
            }

            cobranzaTotal += monto;
        }

        this.cobranzaPura = cobranzaTotal - this.montoExcedente - this.liquidaciones;

        for (VentaModel ventaModel : ventaModels) {
            this.primerosPagos += ventaModel.getPrimerPago();
        }

        formatDoubles();
    }

    private void formatDoubles() {
        this.cobranzaPura = MyUtil.getDouble(this.cobranzaPura);
        this.montoExcedente = MyUtil.getDouble(this.montoExcedente);
        this.liquidaciones = MyUtil.getDouble(this.liquidaciones);
        this.primerosPagos = MyUtil.getDouble(this.primerosPagos);
        this.multas = MyUtil.getDouble(this.multas);
    }
}

package tech.calaverita.sfast_xpress.f_by_f_dashboard_gerencia.pojo;

import lombok.Data;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class CierreDashboardGerencia {
    private Double rendimiento;
    private Double efectivoEnCampo;

    public CierreDashboardGerencia(PagosDashboardGerencia pagosDashboardGerencia,
            DebitosCobranzaGerencia debitosCobranzaGerencia, Double asignaciones) {
        this.rendimiento = MyUtil
                .getDouble(
                        (pagosDashboardGerencia.getTotalCobranzaPura() / debitosCobranzaGerencia.getDebitoTotal())
                                * 100);
        this.efectivoEnCampo = MyUtil.getDouble(pagosDashboardGerencia.getCobranzaTotal() - asignaciones);
    }
}

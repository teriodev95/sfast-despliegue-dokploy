package tech.calaverita.sfast_xpress.f_by_f_dashboard_agencia.pojo;

import lombok.Data;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class CierreDashboardAgencia {
    private Double rendimiento;
    private Double efectivoEnCampo;
    private String statusAgencia;

    public CierreDashboardAgencia(PagosDashboardAgencia pagosDashboardAgencia,
            DebitosCobranzaAgencia debitosCobranzaAgencia, Double asignaciones) {
        this.rendimiento = MyUtil
                .getDouble(
                        (pagosDashboardAgencia.getTotalCobranzaPura() / debitosCobranzaAgencia.getDebitoTotal()) * 100);
        this.efectivoEnCampo = MyUtil.getDouble(pagosDashboardAgencia.getCobranzaTotal() - asignaciones);
    }

    public CierreDashboardAgencia setStatusAgencia(String statusAgencia) {
        this.statusAgencia = statusAgencia;
        return this;
    }
}

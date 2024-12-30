package tech.calaverita.sfast_xpress.DTOs.dashboard;

import lombok.Data;
import tech.calaverita.sfast_xpress.DTOs.cobranza.DebitosCobranzaDTO;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class CierreDashboardDTO {
    private Double rendimiento;
    private Double efectivoEnCampo;
    private String statusAgencia;

    public CierreDashboardDTO(PagosDashboardDTO pagosDashboardDTO, DebitosCobranzaDTO debitosCobranzaDTO,
            Double asignaciones, String statusAgencia) {
        this.rendimiento = MyUtil
                .getDouble((pagosDashboardDTO.getTotalCobranzaPura() / debitosCobranzaDTO.getDebitoTotal()) * 100);
        this.efectivoEnCampo = MyUtil.getDouble(pagosDashboardDTO.getCobranzaTotal() - asignaciones);
        this.statusAgencia = statusAgencia;
    }
}

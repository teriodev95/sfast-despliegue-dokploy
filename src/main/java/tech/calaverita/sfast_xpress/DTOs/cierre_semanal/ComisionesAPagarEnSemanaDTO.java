package tech.calaverita.sfast_xpress.DTOs.cierre_semanal;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.CierreSemanalConsolidadoV2Model;
import tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal.CierreSemanalModel;

@Data
public class ComisionesAPagarEnSemanaDTO {
    private Integer porcentajeComisionCobranza = 0;
    private Integer porcentajeBonoMensual = 0;
    private Double pagoComisionCobranza = 0.0;
    private Double pagoComisionVentas = 0.0;
    private Double bonos = 0.0;

    public ComisionesAPagarEnSemanaDTO() {

    }

    public ComisionesAPagarEnSemanaDTO(CierreSemanalModel cierreSemanalModel) {
        this.porcentajeComisionCobranza = cierreSemanalModel.getPorcentajeComisionCobranza();
        this.porcentajeBonoMensual = cierreSemanalModel.getPorcentajeBonoMensual();
        this.pagoComisionCobranza = cierreSemanalModel.getPagoComisionCobranza();
        this.pagoComisionVentas = cierreSemanalModel.getPagoComisionVentas();
        this.bonos = cierreSemanalModel.getBonos();
    }

    public ComisionesAPagarEnSemanaDTO(CierreSemanalConsolidadoV2Model cierreSemanalConsolidadoV2Model) {
        if (cierreSemanalConsolidadoV2Model != null) {
            this.porcentajeComisionCobranza = cierreSemanalConsolidadoV2Model.getPorcentajePorCobranzaPagadoEnSemana();
            this.porcentajeBonoMensual = cierreSemanalConsolidadoV2Model.getPorcentajePorBonoMensualPagadoEnSemana();
            this.pagoComisionCobranza = cierreSemanalConsolidadoV2Model.getComisionCobranzaPagadaEnSemana();
            this.pagoComisionVentas = cierreSemanalConsolidadoV2Model.getComisionVentasPagadaEnSemana();
            this.bonos = cierreSemanalConsolidadoV2Model.getBonosPagadosEnSemana();
        }
    }
}

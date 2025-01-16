package tech.calaverita.sfast_xpress.DTOs.cierre_semanal;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.CierreSemanalConsolidadoV2Model;
import tech.calaverita.sfast_xpress.models.mariaDB.ComisionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal.CierreSemanalModel;

@Data
public class IngresosAgenteDTO {
    private Double cobranzaPura;
    private Double montoExcedente;
    private Double liquidaciones;
    private Double multas;
    private Double otros = 0.0;
    private String motivoOtros = "";
    private Double total;

    public IngresosAgenteDTO() {

    }

    public IngresosAgenteDTO(CierreSemanalModel cierreSemanalModel) {
        this.cobranzaPura = cierreSemanalModel.getCobranzaPura();
        this.montoExcedente = cierreSemanalModel.getMontoExcedente();
        this.liquidaciones = cierreSemanalModel.getLiquidaciones();
        this.multas = cierreSemanalModel.getMultas();
        this.otros = cierreSemanalModel.getOtrosIngresos();
        this.motivoOtros = cierreSemanalModel.getMotivoOtrosIngresos();
    }

    public IngresosAgenteDTO(CierreSemanalConsolidadoV2Model cierreSemanalConsolidadoV2Model,
            ComisionModel comisionModel) {
        if (comisionModel != null) {
            this.cobranzaPura = comisionModel.getCobranzaPura();
            this.montoExcedente = comisionModel.getMontoExcedente();
        }
        this.liquidaciones = cierreSemanalConsolidadoV2Model.getLiquidaciones();
        this.multas = cierreSemanalConsolidadoV2Model.getMultas();
        this.otros = cierreSemanalConsolidadoV2Model.getOtrosIngresos();
        this.motivoOtros = cierreSemanalConsolidadoV2Model.getMotivoOtrosIngresos();
    }
}

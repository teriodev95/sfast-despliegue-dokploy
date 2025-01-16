package tech.calaverita.sfast_xpress.DTOs.cierre_semanal;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.CierreSemanalConsolidadoV2Model;
import tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal.CierreSemanalModel;

@Data
public class EgresosAgenteDTO {
    private Double asignaciones;
    private Double otros = 0.0;
    private String motivoOtros = "";
    private Double efectivoEntregadoCierre;
    private Double total;

    public EgresosAgenteDTO() {

    }

    public EgresosAgenteDTO(CierreSemanalModel cierreSemanalModel) {
        this.asignaciones = cierreSemanalModel.getAsignaciones();
        this.otros = cierreSemanalModel.getOtrosEgresos();
        this.motivoOtros = cierreSemanalModel.getMotivoOtrosEgresos();
        this.efectivoEntregadoCierre = cierreSemanalModel.getEfectivoEntregadoCierre();
        this.total = cierreSemanalModel.getTotalEgresosAgente();
    }

    public EgresosAgenteDTO(CierreSemanalConsolidadoV2Model cierreSemanalConsolidadoV2Model) {
        this.asignaciones = cierreSemanalConsolidadoV2Model.getAsignaciones();
        this.otros = cierreSemanalConsolidadoV2Model.getOtrosEgresos();
        this.motivoOtros = cierreSemanalConsolidadoV2Model.getMotivoOtrosEgresos();
        this.efectivoEntregadoCierre = cierreSemanalConsolidadoV2Model.getEfectivoEntregadoCierre();
        this.total = cierreSemanalConsolidadoV2Model.getTotalEgresosAgente();
    }
}

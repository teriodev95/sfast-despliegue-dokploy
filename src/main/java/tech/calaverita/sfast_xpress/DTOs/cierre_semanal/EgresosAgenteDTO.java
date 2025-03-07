package tech.calaverita.sfast_xpress.DTOs.cierre_semanal;

import java.util.HashMap;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.CierreSemanalConsolidadoV2Model;
import tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal.CierreSemanalModel;

@Data
public class EgresosAgenteDTO {
    private Double asignaciones;
    private Double otros;
    private String motivoOtros;
    private Double efectivoEntregadoCierre;
    private Double total;
    private HashMap<String, Integer> asignaciones_desglose;

    public EgresosAgenteDTO() {
        this.asignaciones = 0.0;
        this.otros = 0.0;
        this.motivoOtros = "";
        this.efectivoEntregadoCierre = 0.0;
        this.total = 0.0;

        HashMap<String, Integer> asignaciones_desglose = new HashMap<>();
        asignaciones_desglose.put("aSeguridad", 0);
        asignaciones_desglose.put("aGerente", 0);
        this.asignaciones_desglose = asignaciones_desglose;
    }

    public EgresosAgenteDTO(CierreSemanalModel cierreSemanalModel) {
        this();
        this.asignaciones = cierreSemanalModel.getAsignaciones();
        this.otros = cierreSemanalModel.getOtrosEgresos();
        this.motivoOtros = cierreSemanalModel.getMotivoOtrosEgresos();
        this.efectivoEntregadoCierre = cierreSemanalModel.getEfectivoEntregadoCierre();
        this.total = cierreSemanalModel.getTotalEgresosAgente();
    }

    public EgresosAgenteDTO(CierreSemanalConsolidadoV2Model cierreSemanalConsolidadoV2Model) {
        this();
        this.asignaciones = cierreSemanalConsolidadoV2Model.getAsignaciones();
        this.otros = cierreSemanalConsolidadoV2Model.getOtrosEgresos();
        this.motivoOtros = cierreSemanalConsolidadoV2Model.getMotivoOtrosEgresos();
        this.efectivoEntregadoCierre = cierreSemanalConsolidadoV2Model.getEfectivoEntregadoCierre();
        this.total = cierreSemanalConsolidadoV2Model.getTotalEgresosAgente();
    }
}

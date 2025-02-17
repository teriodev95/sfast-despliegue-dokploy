package tech.calaverita.sfast_xpress.DTOs.detalle_cierre_semanal;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.AsignacionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.CierreSemanalConsolidadoV2Model;
import tech.calaverita.sfast_xpress.models.mariaDB.ComisionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.IncidenteReposicionModel;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class DetalleCierreSemanalIngresosDto {
        @JsonProperty(value = "asignaciones")
        private DetalleCierreSemanalAsignacionesDto detalleCierreSemanalAsignacionesDto;
        @JsonProperty(value = "cobranza")
        private DetalleCierreSemanalCobranzaDto detalleCierreSemanalCobranzaDto;
        private Double total;

        public DetalleCierreSemanalIngresosDto() {
                this.total = 0D;
        }

        public DetalleCierreSemanalIngresosDto(List<AsignacionModel> asignacionModels,
                        List<CierreSemanalConsolidadoV2Model> cierreSemanalConsolidadoV2Models,
                        List<ComisionModel> comisionModels,
                        List<IncidenteReposicionModel> incidenteReposicionModels) {
                this();

                this.detalleCierreSemanalAsignacionesDto = new DetalleCierreSemanalAsignacionesDto(asignacionModels,
                                incidenteReposicionModels, "ingreso");
                this.detalleCierreSemanalCobranzaDto = new DetalleCierreSemanalCobranzaDto(
                                cierreSemanalConsolidadoV2Models,
                                comisionModels);

                this.total = MyUtil.getDouble(this.detalleCierreSemanalAsignacionesDto.getTotal()
                                + this.detalleCierreSemanalCobranzaDto.getTotal());
        }
}

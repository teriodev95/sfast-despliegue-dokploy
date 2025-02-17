package tech.calaverita.sfast_xpress.DTOs.detalle_cierre_semanal;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.AsignacionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.CierreSemanalConsolidadoV2Model;
import tech.calaverita.sfast_xpress.models.mariaDB.GastoModel;
import tech.calaverita.sfast_xpress.models.mariaDB.IncidenteReposicionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.VentaModel;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class DetalleCierreSemanalEgresosDto {
        @JsonProperty(value = "asignaciones")
        private DetalleCierreSemanalAsignacionesDto detalleCierreSemanalAsignacionesDto;
        @JsonProperty(value = "bonosYComisiones")
        private DetalleCierreSemanalBonosYComisionesDto detalleCierreSemanalBonosYComisionesDto;
        @JsonProperty(value = "gastos")
        private DetalleCierreSemanalGastosDto detalleCierreSemanalGastosDto;
        private Double total;

        public DetalleCierreSemanalEgresosDto() {
                this.total = 0D;
        }

        public DetalleCierreSemanalEgresosDto(List<AsignacionModel> asignacionModels,
                        List<CierreSemanalConsolidadoV2Model> cierreSemanalConsolidadoV2Models,
                        List<GastoModel> gastoModels, List<IncidenteReposicionModel> incidenteReposicionModels,
                        List<VentaModel> ventaModels) {
                this();

                this.detalleCierreSemanalAsignacionesDto = new DetalleCierreSemanalAsignacionesDto(asignacionModels,
                                incidenteReposicionModels, "egreso");
                this.detalleCierreSemanalBonosYComisionesDto = new DetalleCierreSemanalBonosYComisionesDto(
                                cierreSemanalConsolidadoV2Models,
                                ventaModels);
                this.detalleCierreSemanalGastosDto = new DetalleCierreSemanalGastosDto(gastoModels);

                this.total = MyUtil.getDouble(this.detalleCierreSemanalAsignacionesDto.getTotal()
                                + this.detalleCierreSemanalBonosYComisionesDto.getTotal()
                                + this.detalleCierreSemanalGastosDto.getTotal());
        }
}

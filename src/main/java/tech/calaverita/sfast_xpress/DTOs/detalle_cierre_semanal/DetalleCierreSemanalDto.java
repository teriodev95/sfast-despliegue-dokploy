package tech.calaverita.sfast_xpress.DTOs.detalle_cierre_semanal;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.AsignacionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.CierreSemanalConsolidadoV2Model;
import tech.calaverita.sfast_xpress.models.mariaDB.ComisionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.GastoModel;
import tech.calaverita.sfast_xpress.models.mariaDB.IncidenteReposicionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.VentaModel;
import tech.calaverita.sfast_xpress.pojos.AlmacenObjects;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class DetalleCierreSemanalDto {
        @JsonProperty(value = "ingresos")
        private DetalleCierreSemanalIngresosDto detalleCierreSemanalIngresosDto;
        @JsonProperty(value = "egresos")
        private DetalleCierreSemanalEgresosDto detalleCierreSemanalEgresosDto;
        private Double efectivoAEntregar;

        public DetalleCierreSemanalDto() {

        }

        @SuppressWarnings(value = "unchecked")
        public DetalleCierreSemanalDto(AlmacenObjects almacenObjects) {
                // To easy code
                List<AsignacionModel> ingresosAsignacionModel = (List<AsignacionModel>) almacenObjects
                                .getObject("ingresosAsignacionModel");
                List<AsignacionModel> egresosAsignacionModel = (List<AsignacionModel>) almacenObjects
                                .getObject("egresosAsignacionModel");
                List<CierreSemanalConsolidadoV2Model> cierreSemanalConsolidadoV2Models = (List<CierreSemanalConsolidadoV2Model>) almacenObjects
                                .getObject("cierreSemanalConsolidadoV2Models");
                List<ComisionModel> comisionModels = (List<ComisionModel>) almacenObjects.getObject("comisionModels");
                List<GastoModel> gastoModels = (List<GastoModel>) almacenObjects.getObject("gastoModels");
                List<IncidenteReposicionModel> incidenteReposicionModels = (List<IncidenteReposicionModel>) almacenObjects
                                .getObject("incidenteReposicionModels");
                List<VentaModel> ventaModels = (List<VentaModel>) almacenObjects.getObject("ventaModels");

                this.detalleCierreSemanalIngresosDto = new DetalleCierreSemanalIngresosDto(ingresosAsignacionModel,
                                cierreSemanalConsolidadoV2Models, comisionModels, incidenteReposicionModels);
                this.detalleCierreSemanalEgresosDto = new DetalleCierreSemanalEgresosDto(egresosAsignacionModel,
                                cierreSemanalConsolidadoV2Models, gastoModels, incidenteReposicionModels, ventaModels);

                this.efectivoAEntregar = MyUtil.getDouble(this.detalleCierreSemanalIngresosDto.getTotal()
                                - this.detalleCierreSemanalEgresosDto.getTotal());
        }
}

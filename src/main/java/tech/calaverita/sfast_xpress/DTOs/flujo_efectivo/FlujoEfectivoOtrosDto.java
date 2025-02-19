package tech.calaverita.sfast_xpress.DTOs.flujo_efectivo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.IncidenteReposicionModel;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class FlujoEfectivoOtrosDto {
    @JsonProperty(value = "subtotalIngresos")
    private Double subTotalIngresos;
    @JsonProperty(value = "ingresos")
    private List<FlujoEfectivoOtroIngresoOEgresoDto> otroIngresoDtos;
    @JsonProperty(value = "subtotalEgresos")
    private Double subTotalEgresos;
    @JsonProperty(value = "egresos")
    private List<FlujoEfectivoOtroIngresoOEgresoDto> otroEgresoDtos;

    public FlujoEfectivoOtrosDto() {
        this.subTotalIngresos = 0D;
        this.otroIngresoDtos = new ArrayList<>();
        this.subTotalEgresos = 0D;
        this.otroEgresoDtos = new ArrayList<>();
    }

    public FlujoEfectivoOtrosDto(List<IncidenteReposicionModel> incidenteReposicionModels) {
        this();

        for (IncidenteReposicionModel incidenteReposicionModel : incidenteReposicionModels) {
            if (incidenteReposicionModel.getTipo().equals("ingreso")) {
                this.subTotalIngresos += incidenteReposicionModel.getMonto();
                this.otroIngresoDtos.add(new FlujoEfectivoOtroIngresoOEgresoDto(incidenteReposicionModel));
            } else {
                this.subTotalEgresos += incidenteReposicionModel.getMonto();
                this.otroEgresoDtos.add(new FlujoEfectivoOtroIngresoOEgresoDto(incidenteReposicionModel));
            }
        }

        this.subTotalIngresos = MyUtil.getDouble(this.subTotalIngresos);
        this.subTotalEgresos = MyUtil.getDouble(this.subTotalEgresos);
    }

    public FlujoEfectivoOtrosDto(Double subTotalIngresos, List<FlujoEfectivoOtroIngresoOEgresoDto> otroIngresoDtos,
            List<FlujoEfectivoOtroIngresoOEgresoDto> otroEgresoDtos) {
        this.subTotalIngresos = subTotalIngresos;
        this.otroIngresoDtos = otroIngresoDtos;
        this.otroEgresoDtos = otroEgresoDtos;
    }
}

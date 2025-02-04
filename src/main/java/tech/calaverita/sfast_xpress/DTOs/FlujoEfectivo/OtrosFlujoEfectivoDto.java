package tech.calaverita.sfast_xpress.DTOs.FlujoEfectivo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.IncidenteReposicionModel;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class OtrosFlujoEfectivoDto {
    @JsonProperty(value = "subtotalIngresos")
    private Double subTotalIngresos;
    @JsonProperty(value = "ingresos")
    private List<OtroIngresoOEgresoDto> otroIngresoDtos;
    @JsonProperty(value = "subtotalEgresos")
    private Double subTotalEgresos;
    @JsonProperty(value = "egresos")
    private List<OtroIngresoOEgresoDto> otroEgresoDtos;

    public OtrosFlujoEfectivoDto() {
        this.subTotalIngresos = 0D;
        this.otroIngresoDtos = new ArrayList<>();
        this.subTotalEgresos = 0D;
        this.otroEgresoDtos = new ArrayList<>();
    }

    public OtrosFlujoEfectivoDto(List<IncidenteReposicionModel> incidenteReposicionModels) {
        this();

        for (IncidenteReposicionModel incidenteReposicionModel : incidenteReposicionModels) {
            if (incidenteReposicionModel.getTipo().equals("ingreso")) {
                this.subTotalIngresos += incidenteReposicionModel.getMonto();
                this.otroIngresoDtos.add(new OtroIngresoOEgresoDto(incidenteReposicionModel));
            } else {
                this.subTotalEgresos += incidenteReposicionModel.getMonto();
                this.otroEgresoDtos.add(new OtroIngresoOEgresoDto(incidenteReposicionModel));
            }
        }

        this.subTotalIngresos = MyUtil.getDouble(this.subTotalIngresos);
        this.subTotalEgresos = MyUtil.getDouble(this.subTotalEgresos);
    }

    public OtrosFlujoEfectivoDto(Double subTotalIngresos, List<OtroIngresoOEgresoDto> otroIngresoDtos,
            List<OtroIngresoOEgresoDto> otroEgresoDtos) {
        this.subTotalIngresos = subTotalIngresos;
        this.otroIngresoDtos = otroIngresoDtos;
        this.otroEgresoDtos = otroEgresoDtos;
    }
}

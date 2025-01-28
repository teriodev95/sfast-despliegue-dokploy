package tech.calaverita.sfast_xpress.DTOs.FlujoEfectivo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.AsignacionModel;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class AsignacionFlujoEfectivoDto {
    @JsonProperty(value = "subtotalIngresos")
    private Double subTotalIngresos;
    @JsonProperty(value = "ingresos")
    private List<AsignacionIngresoDto> asignacionIngresoDtos;
    @JsonProperty(value = "subtotalEgresos")
    private Double subTotalEgresos;
    @JsonProperty(value = "egresos")
    private List<AsignacionEgresoDto> asignacionEgresoDtos;

    public AsignacionFlujoEfectivoDto() {
        this.subTotalIngresos = 0D;
        this.asignacionIngresoDtos = new ArrayList<>();
        this.subTotalEgresos = 0D;
        this.asignacionEgresoDtos = new ArrayList<>();
    }

    public AsignacionFlujoEfectivoDto(List<AsignacionModel> asignacionModels, String gerencia) {
        this();

        for (AsignacionModel asignacionModel : asignacionModels) {
            // To easy code
            String gerenciaAsignacionModel = asignacionModel.getRecibioUsuarioModel().getGerencia();

            if (gerenciaAsignacionModel != null && gerenciaAsignacionModel.equals(gerencia)) {
                this.subTotalIngresos += asignacionModel.getMonto();
                this.asignacionIngresoDtos.add(new AsignacionIngresoDto(asignacionModel));
            } else {
                this.subTotalEgresos += asignacionModel.getMonto();
                this.asignacionEgresoDtos.add(new AsignacionEgresoDto(asignacionModel));
            }
        }

        this.subTotalIngresos = MyUtil.getDouble(this.subTotalIngresos);
        this.subTotalEgresos = MyUtil.getDouble(this.subTotalEgresos);
    }

    public AsignacionFlujoEfectivoDto(Double subTotalIngresos, List<AsignacionIngresoDto> asignacionIngresoDtos,
            Double subTotalEgresos, List<AsignacionEgresoDto> asignacionEgresoDtos) {
        this.subTotalIngresos = subTotalIngresos;
        this.asignacionIngresoDtos = asignacionIngresoDtos;
        this.subTotalEgresos = subTotalEgresos;
        this.asignacionEgresoDtos = asignacionEgresoDtos;
    }
}

package tech.calaverita.sfast_xpress.DTOs.flujo_efectivo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.AsignacionModel;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class FlujoEfectivoAsignacionDto {
    @JsonProperty(value = "subtotalIngresos")
    private Double subTotalIngresos;
    @JsonProperty(value = "ingresos")
    private List<FlujoEfectivoAsignacionIngresoDto> asignacionIngresoDtos;
    @JsonProperty(value = "subtotalEgresos")
    private Double subTotalEgresos;
    @JsonProperty(value = "egresos")
    private List<FlujoEfectivoAsignacionEgresoDto> asignacionEgresoDtos;

    public FlujoEfectivoAsignacionDto() {
        this.subTotalIngresos = 0D;
        this.asignacionIngresoDtos = new ArrayList<>();
        this.subTotalEgresos = 0D;
        this.asignacionEgresoDtos = new ArrayList<>();
    }

    public FlujoEfectivoAsignacionDto(List<AsignacionModel> asignacionModels, String gerencia) {
        this();

        for (AsignacionModel asignacionModel : asignacionModels) {
            // To easy code
            String gerenciaAsignacionModel = asignacionModel.getRecibioUsuarioModel().getGerencia();

            if (gerenciaAsignacionModel != null && gerenciaAsignacionModel.equals(gerencia)) {
                this.subTotalIngresos += asignacionModel.getMonto();
                this.asignacionIngresoDtos.add(new FlujoEfectivoAsignacionIngresoDto(asignacionModel));
            } else {
                this.subTotalEgresos += asignacionModel.getMonto();
                this.asignacionEgresoDtos.add(new FlujoEfectivoAsignacionEgresoDto(asignacionModel));
            }
        }

        this.subTotalIngresos = MyUtil.getDouble(this.subTotalIngresos);
        this.subTotalEgresos = MyUtil.getDouble(this.subTotalEgresos);
    }

    public FlujoEfectivoAsignacionDto(List<AsignacionModel> asignacionModels, int usuarioId) {
        this();

        for (AsignacionModel asignacionModel : asignacionModels) {
            // To easy code
            int quienRecibioUsuarioId = asignacionModel.getQuienRecibioUsuarioId();

            if (quienRecibioUsuarioId == usuarioId) {
                this.subTotalIngresos += asignacionModel.getMonto();
                this.asignacionIngresoDtos.add(new FlujoEfectivoAsignacionIngresoDto(asignacionModel));
            } else {
                this.subTotalEgresos += asignacionModel.getMonto();
                this.asignacionEgresoDtos.add(new FlujoEfectivoAsignacionEgresoDto(asignacionModel));
            }
        }

        this.subTotalIngresos = MyUtil.getDouble(this.subTotalIngresos);
        this.subTotalEgresos = MyUtil.getDouble(this.subTotalEgresos);
    }

    public FlujoEfectivoAsignacionDto(Double subTotalIngresos,
            List<FlujoEfectivoAsignacionIngresoDto> asignacionIngresoDtos,
            Double subTotalEgresos, List<FlujoEfectivoAsignacionEgresoDto> asignacionEgresoDtos) {
        this.subTotalIngresos = subTotalIngresos;
        this.asignacionIngresoDtos = asignacionIngresoDtos;
        this.subTotalEgresos = subTotalEgresos;
        this.asignacionEgresoDtos = asignacionEgresoDtos;
    }
}

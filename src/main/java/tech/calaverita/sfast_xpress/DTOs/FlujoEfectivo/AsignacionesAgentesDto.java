package tech.calaverita.sfast_xpress.DTOs.FlujoEfectivo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.AsignacionModel;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class AsignacionesAgentesDto {
    @JsonProperty(value = "subtotalIngresos")
    private Double subTotalIngresos;
    @JsonProperty(value = "ingresos")
    private List<AsignacionIngresoAgenteDto> ingresoAsignacionAgenteDtos;

    public AsignacionesAgentesDto() {
        this.subTotalIngresos = 0D;
        this.ingresoAsignacionAgenteDtos = new ArrayList<>();
    }

    public AsignacionesAgentesDto(List<AsignacionModel> asignacionesAgenteAsignacionModel) {
        this();

        for (AsignacionModel asignacionModel : asignacionesAgenteAsignacionModel) {
            boolean existsPrevious = false;
            for (AsignacionIngresoAgenteDto asignacionIngresoAgenteDto : this.ingresoAsignacionAgenteDtos) {
                if (asignacionModel.getEntregoUsuarioModel().getAgencia()
                        .equals(asignacionIngresoAgenteDto.getAgencia())) {
                    asignacionIngresoAgenteDto
                            .setMonto(asignacionIngresoAgenteDto.getMonto() + asignacionModel.getMonto());
                    asignacionIngresoAgenteDto.setCantidad(asignacionIngresoAgenteDto.getCantidad() + 1);

                    this.subTotalIngresos += asignacionModel.getMonto();
                    existsPrevious = true;
                    break;
                }
            }

            if (!existsPrevious) {
                this.subTotalIngresos += asignacionModel.getMonto();
                this.ingresoAsignacionAgenteDtos.add(new AsignacionIngresoAgenteDto(asignacionModel));
            }
        }

        this.subTotalIngresos = MyUtil.getDouble(this.subTotalIngresos);
    }

    public AsignacionesAgentesDto(Double subTotalIngresos,
            List<AsignacionIngresoAgenteDto> ingresoAsignacionAgenteDtos) {
        this.subTotalIngresos = subTotalIngresos;
        this.ingresoAsignacionAgenteDtos = ingresoAsignacionAgenteDtos;
    }
}

package tech.calaverita.sfast_xpress.DTOs.flujo_efectivo;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.AsignacionModel;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class FlujoEfectivoAsignacionEgresoDto {
    private String id;
    private String fecha;
    private String asignadoA;
    private Double monto;

    public FlujoEfectivoAsignacionEgresoDto() {

    }

    public FlujoEfectivoAsignacionEgresoDto(AsignacionModel asignacionModel) {
        this.id = asignacionModel.getAsignacionId();
        this.fecha = MyUtil.getLocalDateFromStringLocalDateTime(asignacionModel.getCreatedAt()).toString();
        this.asignadoA = asignacionModel.getRecibioUsuarioModel().getNombre();
        this.monto = asignacionModel.getMonto();
    }
}

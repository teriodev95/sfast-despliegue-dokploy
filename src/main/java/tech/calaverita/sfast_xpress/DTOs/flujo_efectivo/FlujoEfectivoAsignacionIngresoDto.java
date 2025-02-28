package tech.calaverita.sfast_xpress.DTOs.flujo_efectivo;

import java.time.LocalDate;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.AsignacionModel;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class FlujoEfectivoAsignacionIngresoDto {
    private String id;
    private LocalDate fecha;
    private String recibidoDe;
    private Double monto;

    public FlujoEfectivoAsignacionIngresoDto() {

    }

    public FlujoEfectivoAsignacionIngresoDto(AsignacionModel asignacionModel) {
        this.id = asignacionModel.getAsignacionId();
        this.fecha = MyUtil.getLocalDateFromStringLocalDateTime(asignacionModel.getCreatedAt());
        this.recibidoDe = asignacionModel.getEntregoUsuarioModel().getNombre();
        this.monto = asignacionModel.getMonto();
    }

    public FlujoEfectivoAsignacionIngresoDto(LocalDate fecha, String recibidoDe, Double monto) {
        this.fecha = fecha;
        this.recibidoDe = recibidoDe;
        this.monto = monto;
    }
}

package tech.calaverita.sfast_xpress.DTOs.flujo_efectivo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.AsignacionModel;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class FlujoEfectivoAsignacionEgresoDto {
    private LocalDate fecha;
    private String asignadoA;
    private Double monto;

    public FlujoEfectivoAsignacionEgresoDto() {

    }

    public FlujoEfectivoAsignacionEgresoDto(AsignacionModel asignacionModel) {
        // To easy code
        LocalDateTime fechaConHora = MyUtil.getLocalDateTimeFromString(asignacionModel.getCreatedAt()).minusHours(6);

        this.fecha = fechaConHora.toLocalDate();
        this.asignadoA = asignacionModel.getRecibioUsuarioModel().getNombre();
        this.monto = asignacionModel.getMonto();
    }

    public FlujoEfectivoAsignacionEgresoDto(LocalDate fecha, String recibidoDe, Double monto) {
        this.fecha = fecha;
        this.asignadoA = recibidoDe;
        this.monto = monto;
    }
}

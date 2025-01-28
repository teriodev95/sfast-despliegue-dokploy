package tech.calaverita.sfast_xpress.DTOs.FlujoEfectivo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.AsignacionModel;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class AsignacionEgresoDto {
    private LocalDate fecha;
    private String asignadoA;
    private Double monto;

    public AsignacionEgresoDto() {

    }

    public AsignacionEgresoDto(AsignacionModel asignacionModel) {
        // To easy code
        LocalDateTime fechaConHora = MyUtil.getLocalDateTimeFromString(asignacionModel.getCreatedAt()).minusHours(6);

        this.fecha = fechaConHora.toLocalDate();
        this.asignadoA = asignacionModel.getRecibioUsuarioModel().getNombre();
        this.monto = asignacionModel.getMonto();
    }

    public AsignacionEgresoDto(LocalDate fecha, String recibidoDe, Double monto) {
        this.fecha = fecha;
        this.asignadoA = recibidoDe;
        this.monto = monto;
    }
}

package tech.calaverita.sfast_xpress.DTOs.FlujoEfectivo;

import java.time.LocalDate;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.IncidenteReposicionModel;

@Data
public class OtroIngresoOEgresoDto {
    private LocalDate fecha;
    private String descripcion;
    private Double monto;

    public OtroIngresoOEgresoDto() {
        this.monto = 0D;
    }

    public OtroIngresoOEgresoDto(IncidenteReposicionModel incidenteReposicionModel) {
        this.fecha = incidenteReposicionModel.getFecha();
        this.descripcion = incidenteReposicionModel.getComentario();
        this.monto = incidenteReposicionModel.getMonto();
    }

    public OtroIngresoOEgresoDto(LocalDate fecha, String descripcion, Double monto) {
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.monto = monto;
    }
}

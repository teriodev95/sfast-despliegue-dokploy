package tech.calaverita.sfast_xpress.DTOs.flujo_efectivo;

import java.time.LocalDate;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.IncidenteReposicionModel;

@Data
public class FlujoEfectivoOtroIngresoOEgresoDto {
    private LocalDate fecha;
    private String descripcion;
    private Double monto;

    public FlujoEfectivoOtroIngresoOEgresoDto() {
        this.monto = 0D;
    }

    public FlujoEfectivoOtroIngresoOEgresoDto(IncidenteReposicionModel incidenteReposicionModel) {
        this.fecha = incidenteReposicionModel.getFecha();
        this.descripcion = incidenteReposicionModel.getCategoria() + " · " + incidenteReposicionModel.getComentario();
        this.monto = incidenteReposicionModel.getMonto();
    }

    public FlujoEfectivoOtroIngresoOEgresoDto(LocalDate fecha, String descripcion, Double monto) {
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.monto = monto;
    }
}

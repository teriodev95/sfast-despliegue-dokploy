package tech.calaverita.sfast_xpress.DTOs.flujo_efectivo;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.IncidenteReposicionModel;

@Data
public class FlujoEfectivoOtroIngresoOEgresoDto {
    private String fecha;
    private String descripcion;
    private Double monto;

    public FlujoEfectivoOtroIngresoOEgresoDto() {
        this.monto = 0D;
    }

    public FlujoEfectivoOtroIngresoOEgresoDto(IncidenteReposicionModel incidenteReposicionModel) {
        this.fecha = incidenteReposicionModel.getFecha().toString();
        this.descripcion = incidenteReposicionModel.getCategoria() + " · " + incidenteReposicionModel.getComentario();
        this.monto = incidenteReposicionModel.getMonto();
    }
}

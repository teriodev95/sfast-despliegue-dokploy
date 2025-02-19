package tech.calaverita.sfast_xpress.DTOs.flujo_efectivo;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.AsignacionModel;

@Data
public class FlujoEfectivoAsignacionIngresoAgenteDto {
    private String agencia;
    private String agente;
    private Double monto;
    private Integer cantidad;

    public FlujoEfectivoAsignacionIngresoAgenteDto() {
        this.monto = 0D;
        this.cantidad = 0;
    }

    public FlujoEfectivoAsignacionIngresoAgenteDto(AsignacionModel asignacionModel) {
        this.agencia = asignacionModel.getEntregoUsuarioModel().getAgencia();
        this.agente = asignacionModel.getEntregoUsuarioModel().getNombre();
        this.monto = asignacionModel.getMonto();
        this.cantidad = 1;
    }
}

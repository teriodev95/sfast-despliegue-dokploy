package tech.calaverita.sfast_xpress.DTOs.FlujoEfectivo;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.AsignacionModel;

@Data
public class AsignacionIngresoAgenteDto {
    private String agencia;
    private String agente;
    private Double monto;
    private Integer cantidad;

    public AsignacionIngresoAgenteDto() {
        this.monto = 0D;
        this.cantidad = 0;
    }

    public AsignacionIngresoAgenteDto(AsignacionModel asignacionModel) {
        this.agencia = asignacionModel.getEntregoUsuarioModel().getAgencia();
        this.agente = asignacionModel.getEntregoUsuarioModel().getNombre();
        this.monto = asignacionModel.getMonto();
        this.cantidad = 1;
    }
}

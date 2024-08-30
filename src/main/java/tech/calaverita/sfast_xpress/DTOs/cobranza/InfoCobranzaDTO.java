package tech.calaverita.sfast_xpress.DTOs.cobranza;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoModel;

@Data
public class InfoCobranzaDTO {
    private String gerencia;
    private String agencia;
    private Integer anio;
    private Integer semana;
    private Integer clientes;

    public InfoCobranzaDTO(PrestamoModel prestamoModel, Integer anio, Integer semana, Integer clientes) {
        this.gerencia = prestamoModel.getGerencia();
        this.agencia = prestamoModel.getAgente();
        this.anio = anio;
        this.semana = semana;
        this.clientes = clientes;
    }
}

package tech.calaverita.sfast_xpress.DTOs.cobranza;

import lombok.Data;

@Data
public class InfoCobranzaDTO {
    private String gerencia;
    private String agencia;
    private Integer anio;
    private Integer semana;
    private Integer clientes;

    public InfoCobranzaDTO(String gerencia, String agencia, Integer anio, Integer semana,
            Integer clientes) {
        this.gerencia = gerencia;
        this.agencia = agencia;
        this.anio = anio;
        this.semana = semana;
        this.clientes = clientes;
    }
}

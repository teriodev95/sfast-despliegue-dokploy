package tech.calaverita.sfast_xpress.dashboard_gerencia.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InfoCobranzaGerencia {
    private String gerencia;
    private String agencia;
    private Integer anio;
    private Integer semana;
    private Integer clientes;

    public InfoCobranzaGerencia setGerencia(String gerencia) {
        this.gerencia = gerencia;
        return this;
    }

    public InfoCobranzaGerencia setAgencia(String agencia) {
        this.agencia = agencia;
        return this;
    }

    public InfoCobranzaGerencia setAnio(Integer anio) {
        this.anio = anio;
        return this;
    }

    public InfoCobranzaGerencia setSemana(Integer semana) {
        this.semana = semana;
        return this;
    }

    public InfoCobranzaGerencia setClientes(Integer clientes) {
        this.clientes = clientes;
        return this;
    }
}

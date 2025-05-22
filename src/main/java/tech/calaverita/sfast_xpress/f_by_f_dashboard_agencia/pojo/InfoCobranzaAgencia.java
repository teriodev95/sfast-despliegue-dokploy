package tech.calaverita.sfast_xpress.f_by_f_dashboard_agencia.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InfoCobranzaAgencia {
    private String gerencia;
    private String agencia;
    private Integer anio;
    private Integer semana;
    private Integer clientes;

    public InfoCobranzaAgencia setGerencia(String gerencia) {
        this.gerencia = gerencia;
        return this;
    }

    public InfoCobranzaAgencia setAgencia(String agencia) {
        this.agencia = agencia;
        return this;
    }

    public InfoCobranzaAgencia setAnio(Integer anio) {
        this.anio = anio;
        return this;
    }

    public InfoCobranzaAgencia setSemana(Integer semana) {
        this.semana = semana;
        return this;
    }

    public InfoCobranzaAgencia setClientes(Integer clientes) {
        this.clientes = clientes;
        return this;
    }
}

package tech.calaverita.reporterloanssql.persistence.dto.cierre_semanal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
public class BalanceAgenciaDTO {
    private String zona;
    private String gerente;
    private String agencia;
    private String agente;
    private Double rendimiento;
    private String nivel;
    private String nivelCalculado;
    private Integer clientes;
    private Integer pagosReducidos;
    private Integer noPagos;
    private Integer liquidaciones;
}

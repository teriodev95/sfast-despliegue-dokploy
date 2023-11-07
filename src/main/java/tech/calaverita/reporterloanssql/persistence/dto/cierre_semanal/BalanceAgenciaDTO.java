package tech.calaverita.reporterloanssql.persistence.dto.cierre_semanal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
public class BalanceAgenciaDTO {
    String zona;
    String gerente;
    String agencia;
    String agente;
    Double rendimiento;
    String nivel;
    Integer clientes;
    Integer pagosReducidos;
    Integer noPagos;
    Integer liquidaciones;
}

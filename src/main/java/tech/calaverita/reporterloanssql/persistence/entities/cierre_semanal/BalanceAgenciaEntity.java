package tech.calaverita.reporterloanssql.persistence.entities.cierre_semanal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "balance_agencia")
public class BalanceAgenciaEntity {
    @Id
    String id;
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

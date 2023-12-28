package tech.calaverita.reporterloanssql.persistence.entities.cierre_semanal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "balances_agencias")
public class BalanceAgenciaEntity {
    @Id
    private String id;
    private String zona;
    private String gerente;
    private String agencia;
    private String agente;
    private Double rendimiento;
    private String nivel;
    private Integer clientes;
    private Integer pagosReducidos;
    private Integer noPagos;
    private Integer liquidaciones;
}

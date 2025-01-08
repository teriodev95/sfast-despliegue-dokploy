package tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "cierres_semanales_consolidados")
public class CierreSemanalModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private Integer semana;
    private Integer anio;
    private String gerencia;
    private String agencia;
    private Double rendimiento;
    private String nivel;
    private Integer clientes;
    private Integer pagosReducidos;
    private Integer noPagos;
    private Integer clientesLiquidados;
    private Double cobranzaPura;
    private Double montoExcedente;
    private Double liquidaciones;
    private Double multas;
    private Double otrosIngresos;
    private String motivoOtrosIngresos;
    private Double asignaciones;
    private Double otrosEgresos;
    private String motivoOtrosEgresos;
    private Double efectivoEntregadoCierre;
    private Double totalEgresosAgente;
    private Integer porcentajeComisionCobranza;
    private Integer porcentajeBonoMensual;
    private Double pagoComisionCobranza;
    private Double pagoComisionVentas;
    private Double bonos;
    private Double efectivoRestanteCierre;
    private String uidVerificacionAgente;
    private String uidVerificacionGerente;
}

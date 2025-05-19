package tech.calaverita.sfast_xpress.cierre_agencia;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "cierres_semanales_consolidados_v2")
public class CierreAgenciaModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Integer semana;
	private Integer anio;
	private String agencia;
	private String agente;
	private String gerencia;
	private String gerente;
	private Integer clientes;
	private Integer pagosReducidos;
	private Integer noPagos;
	private Integer clientesLiquidados;
	private Double liquidaciones;
	private Double multas;
	private Double otrosIngresos;
	private String motivoOtrosIngresos;
	private Double asignaciones;
	private Double otrosEgresos;
	private String motivoOtrosEgresos;
	private Double totalEgresosAgente;
	private Double comisionCobranzaPagadaEnSemana;
	private Integer porcentajePorCobranzaPagadoEnSemana;
	private Double bonosPagadosEnSemana;
	private Integer porcentajePorBonoMensualPagadoEnSemana;
	private Double comisionVentasPagadaEnSemana;
	private Double efectivoRestanteCierre;
	private Double efectivoEntregadoCierre;
	private String uidVerificacionAgente;
	private String uidVerificacionGerente;
}

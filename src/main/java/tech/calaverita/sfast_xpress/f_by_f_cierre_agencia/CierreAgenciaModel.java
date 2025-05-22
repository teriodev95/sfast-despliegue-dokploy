package tech.calaverita.sfast_xpress.f_by_f_cierre_agencia;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
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

	public CierreAgenciaModel(CierreAgenciaDto cierreAgenciaDto) {
		this.id = cierreAgenciaDto.getId();
		this.semana = cierreAgenciaDto.getSemana();
		this.anio = cierreAgenciaDto.getAnio();
		this.agencia = cierreAgenciaDto.getBalanceAgencia().getAgencia();
		this.agente = cierreAgenciaDto.getBalanceAgencia().getAgente();
		this.gerencia = cierreAgenciaDto.getBalanceAgencia().getZona();
		this.gerente = cierreAgenciaDto.getBalanceAgencia().getGerente();
		this.clientes = cierreAgenciaDto.getBalanceAgencia().getClientes();
		this.pagosReducidos = cierreAgenciaDto.getBalanceAgencia().getPagosReducidos();
		this.noPagos = cierreAgenciaDto.getBalanceAgencia().getNoPagos();
		this.clientesLiquidados = cierreAgenciaDto.getBalanceAgencia().getLiquidaciones();
		this.liquidaciones = cierreAgenciaDto.getIngresosAgente().getLiquidaciones();
		this.multas = cierreAgenciaDto.getIngresosAgente().getMultas();
		this.otrosIngresos = cierreAgenciaDto.getIngresosAgente().getOtros();
		this.motivoOtrosIngresos = cierreAgenciaDto.getIngresosAgente().getMotivoOtros();
		this.asignaciones = cierreAgenciaDto.getEgresosAgente().getAsignaciones();
		this.otrosEgresos = cierreAgenciaDto.getEgresosAgente().getOtros();
		this.motivoOtrosEgresos = cierreAgenciaDto.getEgresosAgente().getMotivoOtros();
		this.totalEgresosAgente = cierreAgenciaDto.getEgresosAgente().getTotal();
		this.comisionCobranzaPagadaEnSemana = cierreAgenciaDto.getComisionesAPagarEnSemana().getPagoComisionCobranza();
		this.porcentajePorCobranzaPagadoEnSemana = cierreAgenciaDto.getComisionesAPagarEnSemana()
				.getPorcentajeComisionCobranza();
		this.bonosPagadosEnSemana = cierreAgenciaDto.getComisionesAPagarEnSemana().getBonos();
		this.porcentajePorBonoMensualPagadoEnSemana = cierreAgenciaDto.getComisionesAPagarEnSemana()
				.getPorcentajeBonoMensual();
		this.comisionVentasPagadaEnSemana = cierreAgenciaDto.getComisionesAPagarEnSemana().getPagoComisionVentas();
		this.efectivoRestanteCierre = cierreAgenciaDto.getEfectivoRestanteCierre();
		this.efectivoEntregadoCierre = cierreAgenciaDto.getEgresosAgente().getEfectivoEntregadoCierre();
		this.uidVerificacionAgente = cierreAgenciaDto.getUidVerificacionAgente();
		this.uidVerificacionGerente = cierreAgenciaDto.getUidVerificacionGerente();
	}
}

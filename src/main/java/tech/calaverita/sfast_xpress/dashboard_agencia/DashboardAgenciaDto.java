package tech.calaverita.sfast_xpress.dashboard_agencia;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;
import tech.calaverita.sfast_xpress.dashboard_agencia.pojo.CierreDashboardAgencia;
import tech.calaverita.sfast_xpress.dashboard_agencia.pojo.DebitosCobranzaAgencia;
import tech.calaverita.sfast_xpress.dashboard_agencia.pojo.InfoCobranzaAgencia;
import tech.calaverita.sfast_xpress.dashboard_agencia.pojo.LiquidacionesDashboardAgencia;
import tech.calaverita.sfast_xpress.dashboard_agencia.pojo.PagosDashboardAgencia;
import tech.calaverita.sfast_xpress.dashboard_agencia.pojo.VentasDashboardAgencia;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashboardAgenciaDto {
	private String gerencia;
	private String agencia;
	private Integer anio;
	private Integer semana;
	private Integer clientes;
	private Integer clientesCobrados;
	private Integer noPagos;
	private Integer numeroLiquidaciones;
	private Integer pagosReducidos;
	private Double debitoMiercoles;
	private Double debitoJueves;
	private Double debitoViernes;
	private Double debitoTotal;
	private Double rendimiento;
	private Double totalDeDescuento;
	private Double totalCobranzaPura;
	private Double montoExcedente;
	private Double multas;
	private Double liquidaciones;
	private Double cobranzaTotal;
	private Double montoDeDebitoFaltante;
	private Double efectivoEnCampo;
	private String statusAgencia;
	private Boolean isCerrada;
	private Integer numeroVentas;
	private Double ventas;

	public DashboardAgenciaDto cierreDashboardAgencia(CierreDashboardAgencia cierreDashboardAgencia) {
		this.rendimiento = cierreDashboardAgencia.getRendimiento();
		this.efectivoEnCampo = cierreDashboardAgencia.getEfectivoEnCampo();
		this.statusAgencia = cierreDashboardAgencia.getStatusAgencia();
		return this;
	}

	public DashboardAgenciaDto debitosCobranzaAgencia(DebitosCobranzaAgencia debitosCobranzaAgencia) {
		this.debitoMiercoles = debitosCobranzaAgencia.getDebitoMiercoles();
		this.debitoJueves = debitosCobranzaAgencia.getDebitoJueves();
		this.debitoViernes = debitosCobranzaAgencia.getDebitoViernes();
		this.debitoTotal = debitosCobranzaAgencia.getDebitoTotal();
		return this;
	}

	public DashboardAgenciaDto infoCobranzaAgencia(InfoCobranzaAgencia infoCobranzaAgencia) {
		this.gerencia = infoCobranzaAgencia.getGerencia();
		this.agencia = infoCobranzaAgencia.getAgencia();
		this.anio = infoCobranzaAgencia.getAnio();
		this.semana = infoCobranzaAgencia.getSemana();
		this.clientes = infoCobranzaAgencia.getClientes();
		return this;
	}

	public DashboardAgenciaDto liquidacionesDashboardAgencia(
			LiquidacionesDashboardAgencia liquidacionesDashboardAgencia) {
		this.totalDeDescuento = liquidacionesDashboardAgencia.getTotalDeDescuento();
		this.liquidaciones = liquidacionesDashboardAgencia.getLiquidaciones();
		return this;
	}

	public DashboardAgenciaDto pagosDashboardAgencia(PagosDashboardAgencia pagosDashboardAgencia) {
		this.clientesCobrados = pagosDashboardAgencia.getClientesCobrados();
		this.noPagos = pagosDashboardAgencia.getNoPagos();
		this.numeroLiquidaciones = pagosDashboardAgencia.getNumeroLiquidaciones();
		this.pagosReducidos = pagosDashboardAgencia.getPagosReducidos();
		this.totalCobranzaPura = pagosDashboardAgencia.getTotalCobranzaPura();
		this.montoExcedente = pagosDashboardAgencia.getMontoExcedente();
		this.multas = pagosDashboardAgencia.getMultas();
		this.cobranzaTotal = pagosDashboardAgencia.getCobranzaTotal();
		this.montoDeDebitoFaltante = pagosDashboardAgencia.getMontoDeDebitoFaltante();
		return this;
	}

	public DashboardAgenciaDto ventasDashboardAgencia(VentasDashboardAgencia ventasDashboardAgencia) {
		this.numeroVentas = ventasDashboardAgencia.getNumeroVentas();
		this.ventas = ventasDashboardAgencia.getVentas();
		return this;
	}
}

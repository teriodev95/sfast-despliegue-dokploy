package tech.calaverita.sfast_xpress.dashboard_gerencia.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import tech.calaverita.sfast_xpress.dashboard_agencia.DashboardAgenciaDto;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashboardGerencia {
	private String gerencia;
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
	private Integer numeroVentas;
	private Double ventas;

	public DashboardGerencia() {
		this.clientes = 0;
		this.clientesCobrados = 0;
		this.noPagos = 0;
		this.numeroLiquidaciones = 0;
		this.pagosReducidos = 0;
		this.debitoMiercoles = 0.0;
		this.debitoJueves = 0.0;
		this.debitoViernes = 0.0;
		this.debitoTotal = 0.0;
		this.rendimiento = 0.0;
		this.totalDeDescuento = 0.0;
		this.totalCobranzaPura = 0.0;
		this.montoExcedente = 0.0;
		this.multas = 0.0;
		this.liquidaciones = 0.0;
		this.cobranzaTotal = 0.0;
		this.montoDeDebitoFaltante = 0.0;
		this.efectivoEnCampo = 0.0;
		this.numeroVentas = 0;
		this.ventas = 0.0;
	}

	public DashboardGerencia(List<DashboardAgenciaDto> dashboardAgenciaDtos) {
		this();

		this.gerencia = dashboardAgenciaDtos.get(0).getGerencia();
		this.anio = dashboardAgenciaDtos.get(0).getAnio();
		this.semana = dashboardAgenciaDtos.get(0).getSemana();

		for (DashboardAgenciaDto dashboardAgenciaDto : dashboardAgenciaDtos) {
			this.clientes = this.clientes += dashboardAgenciaDto.getClientes();
			this.clientesCobrados = this.clientesCobrados += dashboardAgenciaDto.getClientesCobrados();
			this.noPagos = this.noPagos += dashboardAgenciaDto.getNoPagos();
			this.numeroLiquidaciones = this.numeroLiquidaciones += dashboardAgenciaDto.getNumeroLiquidaciones();
			this.pagosReducidos = this.pagosReducidos += dashboardAgenciaDto.getPagosReducidos();
			this.debitoMiercoles = this.debitoMiercoles += dashboardAgenciaDto.getDebitoMiercoles();
			this.debitoJueves = this.debitoJueves += dashboardAgenciaDto.getDebitoJueves();
			this.debitoViernes = this.debitoViernes += dashboardAgenciaDto.getDebitoViernes();
			this.debitoTotal = this.debitoTotal += dashboardAgenciaDto.getDebitoTotal();
			this.totalDeDescuento = this.totalDeDescuento += dashboardAgenciaDto.getTotalDeDescuento();
			this.totalCobranzaPura = this.totalCobranzaPura += dashboardAgenciaDto.getTotalCobranzaPura();
			this.montoExcedente = this.montoExcedente += dashboardAgenciaDto.getMontoExcedente();
			this.multas = this.multas += dashboardAgenciaDto.getMultas();
			this.liquidaciones = this.liquidaciones += dashboardAgenciaDto.getLiquidaciones();
			this.cobranzaTotal = this.cobranzaTotal += dashboardAgenciaDto.getCobranzaTotal();
			this.montoDeDebitoFaltante = this.montoDeDebitoFaltante += dashboardAgenciaDto.getMontoDeDebitoFaltante();
			this.efectivoEnCampo = this.efectivoEnCampo += dashboardAgenciaDto.getEfectivoEnCampo();
			this.numeroVentas = this.numeroVentas += dashboardAgenciaDto.getNumeroVentas();
			this.ventas = this.ventas += dashboardAgenciaDto.getVentas();
		}
		this.rendimiento = this.totalCobranzaPura / this.debitoTotal * 100;
	}

	public DashboardGerencia cierreDashboardAgencia(CierreDashboardGerencia cierreDashboardGerencia) {
		this.rendimiento = cierreDashboardGerencia.getRendimiento();
		this.efectivoEnCampo = cierreDashboardGerencia.getEfectivoEnCampo();
		return this;
	}

	public DashboardGerencia debitosCobranzaAgencia(DebitosCobranzaGerencia debitosCobranzaGerencia) {
		this.debitoMiercoles = debitosCobranzaGerencia.getDebitoMiercoles();
		this.debitoJueves = debitosCobranzaGerencia.getDebitoJueves();
		this.debitoViernes = debitosCobranzaGerencia.getDebitoViernes();
		this.debitoTotal = debitosCobranzaGerencia.getDebitoTotal();
		return this;
	}

	public DashboardGerencia infoCobranzaAgencia(InfoCobranzaGerencia infoCobranzaGerencia) {
		this.gerencia = infoCobranzaGerencia.getGerencia();
		this.anio = infoCobranzaGerencia.getAnio();
		this.semana = infoCobranzaGerencia.getSemana();
		this.clientes = infoCobranzaGerencia.getClientes();
		return this;
	}

	public DashboardGerencia liquidacionesDashboardAgencia(
			LiquidacionesDashboardGerencia liquidacionesDashboardGerencia) {
		this.totalDeDescuento = liquidacionesDashboardGerencia.getTotalDeDescuento();
		this.liquidaciones = liquidacionesDashboardGerencia.getLiquidaciones();
		return this;
	}

	public DashboardGerencia pagosDashboardAgencia(PagosDashboardGerencia pagosDashboardGerencia) {
		this.clientesCobrados = pagosDashboardGerencia.getClientesCobrados();
		this.noPagos = pagosDashboardGerencia.getNoPagos();
		this.numeroLiquidaciones = pagosDashboardGerencia.getNumeroLiquidaciones();
		this.pagosReducidos = pagosDashboardGerencia.getPagosReducidos();
		this.totalCobranzaPura = pagosDashboardGerencia.getTotalCobranzaPura();
		this.montoExcedente = pagosDashboardGerencia.getMontoExcedente();
		this.multas = pagosDashboardGerencia.getMultas();
		this.cobranzaTotal = pagosDashboardGerencia.getCobranzaTotal();
		this.montoDeDebitoFaltante = pagosDashboardGerencia.getMontoDeDebitoFaltante();
		return this;
	}

	public DashboardGerencia ventasDashboardAgencia(VentasDashboardGerencia ventasDashboardGerencia) {
		this.numeroVentas = ventasDashboardGerencia.getNumeroVentas();
		this.ventas = ventasDashboardGerencia.getVentas();
		return this;
	}
}

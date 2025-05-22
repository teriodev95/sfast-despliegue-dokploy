package tech.calaverita.sfast_xpress.f_by_f_dashboard_gerencia;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;
import tech.calaverita.sfast_xpress.f_by_f_dashboard_gerencia.pojo.CierreDashboardGerencia;
import tech.calaverita.sfast_xpress.f_by_f_dashboard_gerencia.pojo.DashboardGerencia;
import tech.calaverita.sfast_xpress.f_by_f_dashboard_gerencia.pojo.DebitosCobranzaGerencia;
import tech.calaverita.sfast_xpress.f_by_f_dashboard_gerencia.pojo.InfoCobranzaGerencia;
import tech.calaverita.sfast_xpress.f_by_f_dashboard_gerencia.pojo.LiquidacionesDashboardGerencia;
import tech.calaverita.sfast_xpress.f_by_f_dashboard_gerencia.pojo.PagosDashboardGerencia;
import tech.calaverita.sfast_xpress.f_by_f_dashboard_gerencia.pojo.VentasDashboardGerencia;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashboardGerenciaDto {
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

	public DashboardGerenciaDto(DashboardGerencia dashboardGerencia) {
		this.gerencia = dashboardGerencia.getGerencia();
		this.anio = dashboardGerencia.getAnio();
		this.semana = dashboardGerencia.getSemana();
		this.clientes = dashboardGerencia.getClientes();
		this.clientesCobrados = dashboardGerencia.getClientesCobrados();
		this.noPagos = dashboardGerencia.getNoPagos();
		this.numeroLiquidaciones = dashboardGerencia.getNumeroLiquidaciones();
		this.pagosReducidos = dashboardGerencia.getPagosReducidos();
		this.debitoMiercoles = MyUtil.getDouble(dashboardGerencia.getDebitoMiercoles());
		this.debitoJueves = MyUtil.getDouble(dashboardGerencia.getDebitoJueves());
		this.debitoViernes = MyUtil.getDouble(dashboardGerencia.getDebitoViernes());
		this.debitoTotal = MyUtil.getDouble(dashboardGerencia.getDebitoTotal());
		this.rendimiento = MyUtil.getDouble(dashboardGerencia.getRendimiento());
		this.totalDeDescuento = MyUtil.getDouble(dashboardGerencia.getTotalDeDescuento());
		this.totalCobranzaPura = MyUtil.getDouble(dashboardGerencia.getTotalCobranzaPura());
		this.montoExcedente = MyUtil.getDouble(dashboardGerencia.getMontoExcedente());
		this.multas = MyUtil.getDouble(dashboardGerencia.getMultas());
		this.liquidaciones = MyUtil.getDouble(dashboardGerencia.getLiquidaciones());
		this.cobranzaTotal = MyUtil.getDouble(dashboardGerencia.getCobranzaTotal());
		this.montoDeDebitoFaltante = MyUtil.getDouble(dashboardGerencia.getMontoDeDebitoFaltante());
		this.efectivoEnCampo = MyUtil.getDouble(dashboardGerencia.getEfectivoEnCampo());
		this.numeroVentas = dashboardGerencia.getNumeroVentas();
		this.ventas = MyUtil.getDouble(dashboardGerencia.getVentas());
	}

	public DashboardGerenciaDto cierreDashboardAgencia(CierreDashboardGerencia cierreDashboardGerencia) {
		this.rendimiento = cierreDashboardGerencia.getRendimiento();
		this.efectivoEnCampo = cierreDashboardGerencia.getEfectivoEnCampo();
		return this;
	}

	public DashboardGerenciaDto debitosCobranzaAgencia(DebitosCobranzaGerencia debitosCobranzaGerencia) {
		this.debitoMiercoles = debitosCobranzaGerencia.getDebitoMiercoles();
		this.debitoJueves = debitosCobranzaGerencia.getDebitoJueves();
		this.debitoViernes = debitosCobranzaGerencia.getDebitoViernes();
		this.debitoTotal = debitosCobranzaGerencia.getDebitoTotal();
		return this;
	}

	public DashboardGerenciaDto infoCobranzaAgencia(InfoCobranzaGerencia infoCobranzaGerencia) {
		this.gerencia = infoCobranzaGerencia.getGerencia();
		this.anio = infoCobranzaGerencia.getAnio();
		this.semana = infoCobranzaGerencia.getSemana();
		this.clientes = infoCobranzaGerencia.getClientes();
		return this;
	}

	public DashboardGerenciaDto liquidacionesDashboardAgencia(
			LiquidacionesDashboardGerencia liquidacionesDashboardGerencia) {
		this.totalDeDescuento = liquidacionesDashboardGerencia.getTotalDeDescuento();
		this.liquidaciones = liquidacionesDashboardGerencia.getLiquidaciones();
		return this;
	}

	public DashboardGerenciaDto pagosDashboardAgencia(PagosDashboardGerencia pagosDashboardGerencia) {
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

	public DashboardGerenciaDto ventasDashboardAgencia(VentasDashboardGerencia ventasDashboardGerencia) {
		this.numeroVentas = ventasDashboardGerencia.getNumeroVentas();
		this.ventas = ventasDashboardGerencia.getVentas();
		return this;
	}
}

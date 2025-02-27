package tech.calaverita.sfast_xpress.DTOs.balance_general;

import lombok.Data;
import tech.calaverita.sfast_xpress.pojos.CobranzaAgencia;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class BalanceGeneralAgenciaDto {
	private Double debito;
	private Double cobranzaPura;
	private Double faltante;
	private Double eficiencia;
	private Double ventas;
	private String agencia;
	private String agente;
	private BalanceGeneralMetricaDto clientes;
	private BalanceGeneralMetricaDto noPagos;
	private BalanceGeneralMetricaDto pagosReducidos;
	private BalanceGeneralMetricaDto clientesLiquidados;

	public BalanceGeneralAgenciaDto() {
		this.debito = 0.0;
		this.cobranzaPura = 0.0;
		this.faltante = 0.0;
		this.eficiencia = 0.0;
		this.ventas = 0.0;
		this.agencia = "";
		this.agente = "";
		this.clientes = new BalanceGeneralMetricaDto();
		this.noPagos = new BalanceGeneralMetricaDto();
		this.pagosReducidos = new BalanceGeneralMetricaDto();
		this.clientesLiquidados = new BalanceGeneralMetricaDto();
	}

	public BalanceGeneralAgenciaDto(CobranzaAgencia cobranzaAgenciaSemanaAnterior,
			CobranzaAgencia cobranzaAgenciaSemanaActual) {
		this();
		this.debito = cobranzaAgenciaSemanaActual.getDebito();
		this.cobranzaPura = cobranzaAgenciaSemanaActual.getCobranzaPura();
		this.faltante = cobranzaAgenciaSemanaActual.getFaltante();
		this.eficiencia = cobranzaAgenciaSemanaActual.getEficiencia();
		this.ventas = cobranzaAgenciaSemanaActual.getVentas();
		this.agencia = cobranzaAgenciaSemanaActual.getAgencia();
		this.agente = cobranzaAgenciaSemanaActual.getAgente();
		this.clientes = new BalanceGeneralMetricaDto(
				cobranzaAgenciaSemanaAnterior.getClientes(),
				cobranzaAgenciaSemanaActual.getClientes());
		this.noPagos = new BalanceGeneralMetricaDto(
				cobranzaAgenciaSemanaAnterior.getNoPagos(),
				cobranzaAgenciaSemanaActual.getNoPagos());
		this.pagosReducidos = new BalanceGeneralMetricaDto(
				cobranzaAgenciaSemanaAnterior.getPagosReducidos(),
				cobranzaAgenciaSemanaActual.getPagosReducidos());
		this.clientesLiquidados = new BalanceGeneralMetricaDto(
				cobranzaAgenciaSemanaAnterior.getClientesLiquidados(),
				cobranzaAgenciaSemanaActual.getClientesLiquidados());
	}
}
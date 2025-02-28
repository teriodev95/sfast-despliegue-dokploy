package tech.calaverita.sfast_xpress.DTOs.detalle_cierre_agencias;

import lombok.Data;
import tech.calaverita.sfast_xpress.pojos.CobranzaAgencia;

@Data
public class DetalleCierreAgenciasAgenciaDto {
	private Double debito;
	private Double cobranzaPura;
	private Double faltante;
	private Double eficiencia;
	private Double ventas;
	private String agencia;
	private String agente;
	private DetalleCierreAgenciasMetricaDto clientes;
	private DetalleCierreAgenciasMetricaDto noPagos;
	private DetalleCierreAgenciasMetricaDto pagosReducidos;
	private DetalleCierreAgenciasMetricaDto clientesLiquidados;

	public DetalleCierreAgenciasAgenciaDto() {
		this.debito = 0.0;
		this.cobranzaPura = 0.0;
		this.faltante = 0.0;
		this.eficiencia = 0.0;
		this.ventas = 0.0;
		this.agencia = "";
		this.agente = "";
		this.clientes = new DetalleCierreAgenciasMetricaDto();
		this.noPagos = new DetalleCierreAgenciasMetricaDto();
		this.pagosReducidos = new DetalleCierreAgenciasMetricaDto();
		this.clientesLiquidados = new DetalleCierreAgenciasMetricaDto();
	}

	public DetalleCierreAgenciasAgenciaDto(CobranzaAgencia cobranzaAgenciaSemanaAnterior,
			CobranzaAgencia cobranzaAgenciaSemanaActual) {
		this();
		this.debito = cobranzaAgenciaSemanaActual.getDebito();
		this.cobranzaPura = cobranzaAgenciaSemanaActual.getCobranzaPura();
		this.faltante = cobranzaAgenciaSemanaActual.getFaltante();
		this.eficiencia = cobranzaAgenciaSemanaActual.getEficiencia();
		this.ventas = cobranzaAgenciaSemanaActual.getVentas();
		this.agencia = cobranzaAgenciaSemanaActual.getAgencia();
		this.agente = cobranzaAgenciaSemanaActual.getAgente();
		this.clientes = new DetalleCierreAgenciasMetricaDto(
				cobranzaAgenciaSemanaAnterior.getClientes(),
				cobranzaAgenciaSemanaActual.getClientes());
		this.noPagos = new DetalleCierreAgenciasMetricaDto(
				cobranzaAgenciaSemanaAnterior.getNoPagos(),
				cobranzaAgenciaSemanaActual.getNoPagos());
		this.pagosReducidos = new DetalleCierreAgenciasMetricaDto(
				cobranzaAgenciaSemanaAnterior.getPagosReducidos(),
				cobranzaAgenciaSemanaActual.getPagosReducidos());
		this.clientesLiquidados = new DetalleCierreAgenciasMetricaDto(
				cobranzaAgenciaSemanaAnterior.getClientesLiquidados(),
				cobranzaAgenciaSemanaActual.getClientesLiquidados());
	}
}
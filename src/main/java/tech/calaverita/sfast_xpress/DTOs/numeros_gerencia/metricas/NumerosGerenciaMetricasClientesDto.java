package tech.calaverita.sfast_xpress.DTOs.numeros_gerencia.metricas;

import java.util.List;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.CierreGerenciaModel;
import tech.calaverita.sfast_xpress.pojos.CobranzaAgencia;

@Data
public class NumerosGerenciaMetricasClientesDto {
	private Double descuentoPorLiquidacion;
	private NumerosGerenciaMetricaDto clientes;
	private NumerosGerenciaMetricaDto noPagos;
	private NumerosGerenciaMetricaDto pagosReducidos;
	private NumerosGerenciaMetricaDto clientesLiquidados;

	public NumerosGerenciaMetricasClientesDto() {
		this.descuentoPorLiquidacion = 0.0;
		this.clientes = new NumerosGerenciaMetricaDto();
		this.noPagos = new NumerosGerenciaMetricaDto();
		this.pagosReducidos = new NumerosGerenciaMetricaDto();
		this.clientesLiquidados = new NumerosGerenciaMetricaDto();
	}

	public NumerosGerenciaMetricasClientesDto(CierreGerenciaModel cierreGerenciaModel,
			List<CobranzaAgencia> cobranzaAgenciaSemanaActual) {
		this();

		int clientesSemanaAnterior = 0;
		int noPagosSemanaAnterior = 0;
		int pagosReducidosSemanaAnterior = 0;
		int clientesLiquidadosSemanaAnterior = 0;
		if (cierreGerenciaModel != null) {
			clientesSemanaAnterior = cierreGerenciaModel.getNumClientes();
			noPagosSemanaAnterior = cierreGerenciaModel.getNumNoPagos();
			pagosReducidosSemanaAnterior = cierreGerenciaModel.getNumReducidos();
			clientesLiquidadosSemanaAnterior = cierreGerenciaModel.getNumClientesLiquidacion();
		}

		int clientesSemanaActual = cobranzaAgenciaSemanaActual.stream().mapToInt(CobranzaAgencia::getClientes)
				.sum();
		int noPagosSemanaActual = cobranzaAgenciaSemanaActual.stream().mapToInt(CobranzaAgencia::getNoPagos)
				.sum();
		int pagosReducidosSemanaActual = cobranzaAgenciaSemanaActual.stream()
				.mapToInt(CobranzaAgencia::getPagosReducidos).sum();
		int clientesLiquidadosSemanaActual = cobranzaAgenciaSemanaActual.stream()
				.mapToInt(CobranzaAgencia::getClientesLiquidados).sum();

		this.clientes = new NumerosGerenciaMetricaDto(clientesSemanaAnterior, clientesSemanaActual);
		this.noPagos = new NumerosGerenciaMetricaDto(noPagosSemanaAnterior, noPagosSemanaActual);
		this.pagosReducidos = new NumerosGerenciaMetricaDto(pagosReducidosSemanaAnterior,
				pagosReducidosSemanaActual);
		this.clientesLiquidados = new NumerosGerenciaMetricaDto(clientesLiquidadosSemanaAnterior,
				clientesLiquidadosSemanaActual);
		this.descuentoPorLiquidacion = cobranzaAgenciaSemanaActual.stream()
				.mapToDouble(CobranzaAgencia::getDescuentoPorLiquidacion).sum();
	}
}

package tech.calaverita.sfast_xpress.DTOs.numeros_gerencia.metricas;

import java.util.List;

import lombok.Data;
import tech.calaverita.sfast_xpress.pojos.CobranzaAgencia;

@Data
public class NumerosGerenciaMetricasFinancierasDto {
	private NumerosGerenciaMetricaDto debito;
	private NumerosGerenciaMetricaDto cobranzaPura;
	private NumerosGerenciaMetricaDto faltante;
	private NumerosGerenciaMetricaDto eficiencia;

	public NumerosGerenciaMetricasFinancierasDto() {
		this.debito = new NumerosGerenciaMetricaDto();
		this.cobranzaPura = new NumerosGerenciaMetricaDto();
		this.faltante = new NumerosGerenciaMetricaDto();
		this.eficiencia = new NumerosGerenciaMetricaDto();
	}

	public NumerosGerenciaMetricasFinancierasDto(List<CobranzaAgencia> cobranzaAgenciasSemanaAnterior,
			List<CobranzaAgencia> cobranzaAgenciasSemanaActual) {
		this();

		int agenciasActivasSemanaAnterior = cobranzaAgenciasSemanaAnterior.stream()
				.filter(cobranzaAgencia -> cobranzaAgencia.getDebito() > 0).toArray().length;
		int agenciasActivasSemanaActual = cobranzaAgenciasSemanaActual.stream()
				.filter(cobranzaAgencia -> cobranzaAgencia.getDebito() > 0).toArray().length;

		double debitoSemanaAnterior = cobranzaAgenciasSemanaAnterior.stream()
				.mapToDouble(CobranzaAgencia::getDebito)
				.sum();
		double cobranzaPuraSemanaAnterior = cobranzaAgenciasSemanaAnterior.stream()
				.mapToDouble(CobranzaAgencia::getCobranzaPura)
				.sum();
		double faltanteSemanaAnterior = cobranzaAgenciasSemanaAnterior.stream()
				.mapToDouble(CobranzaAgencia::getFaltante)
				.sum();
		double eficienciaSemanaAnterior = cobranzaAgenciasSemanaAnterior.stream()
				.mapToDouble(CobranzaAgencia::getEficiencia)
				.sum() / agenciasActivasSemanaAnterior;
		double debitoSemanaActual = cobranzaAgenciasSemanaActual.stream().mapToDouble(CobranzaAgencia::getDebito)
				.sum();
		double cobranzaPuraSemanaActual = cobranzaAgenciasSemanaActual.stream()
				.mapToDouble(CobranzaAgencia::getCobranzaPura)
				.sum();
		double faltanteSemanaActual = cobranzaAgenciasSemanaActual.stream()
				.mapToDouble(CobranzaAgencia::getFaltante)
				.sum();
		double eficienciaSemanaActual = cobranzaAgenciasSemanaActual.stream()
				.mapToDouble(CobranzaAgencia::getEficiencia)
				.sum() / agenciasActivasSemanaActual;

		this.debito = new NumerosGerenciaMetricaDto(debitoSemanaAnterior, debitoSemanaActual);
		this.cobranzaPura = new NumerosGerenciaMetricaDto(cobranzaPuraSemanaAnterior,
				cobranzaPuraSemanaActual);
		this.faltante = new NumerosGerenciaMetricaDto(faltanteSemanaAnterior, faltanteSemanaActual);
		this.eficiencia = new NumerosGerenciaMetricaDto(eficienciaSemanaAnterior, eficienciaSemanaActual);
	}
}

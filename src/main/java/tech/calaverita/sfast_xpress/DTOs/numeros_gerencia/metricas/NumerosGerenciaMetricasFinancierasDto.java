package tech.calaverita.sfast_xpress.DTOs.numeros_gerencia.metricas;

import java.util.List;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.CierreGerenciaModel;
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

	public NumerosGerenciaMetricasFinancierasDto(CierreGerenciaModel cierreGerenciaModel,
			List<CobranzaAgencia> cobranzaAgenciasSemanaActual) {
		this();

		int agenciasActivasSemanaActual = cobranzaAgenciasSemanaActual.stream()
				.filter(cobranzaAgencia -> cobranzaAgencia.getDebito() > 0).toArray().length;

		double debitoSemanaAnterior = 0.0;
		double cobranzaPuraSemanaAnterior = 0.0;
		double faltanteSemanaAnterior = 0.0;
		double eficienciaSemanaAnterior = 0.0;
		if (cierreGerenciaModel != null) {
			debitoSemanaAnterior = cierreGerenciaModel.getDebito();
			cobranzaPuraSemanaAnterior = cierreGerenciaModel.getPura();
			faltanteSemanaAnterior = cierreGerenciaModel.getFaltante();
			eficienciaSemanaAnterior = cierreGerenciaModel.getEficiencia();
		}

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

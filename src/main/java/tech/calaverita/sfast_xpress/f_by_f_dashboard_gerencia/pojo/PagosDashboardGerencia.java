package tech.calaverita.sfast_xpress.f_by_f_dashboard_gerencia.pojo;

import java.util.ArrayList;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.dynamic.PagoDynamicModel;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class PagosDashboardGerencia {
	private Integer clientesCobrados;
	private Integer noPagos;
	private Integer numeroLiquidaciones;
	private Integer pagosReducidos;
	private Double totalCobranzaPura;
	private Double montoExcedente;
	private Double multas;
	private Double cobranzaTotal;
	private Double montoDeDebitoFaltante;

	public PagosDashboardGerencia(ArrayList<PagoDynamicModel> pagoDynamicModels, double liquidaciones,
			double debitoTotal) {
		this.clientesCobrados = (int) pagoDynamicModels.stream()
				.filter(pagoModel -> !pagoModel.getTipo().equals("Multa")
						&& !pagoModel.getTipo().equals("No_pago")
						&& !pagoModel.getTipo().equals(
								"Visita"))
				.count();
		this.noPagos = (int) pagoDynamicModels.stream()
				.filter(pagoModel -> pagoModel.getTipo().equals("No_pago"))
				.count();
		this.numeroLiquidaciones = (int) pagoDynamicModels.stream()
				.filter(pagoModel -> pagoModel.getTipo().equals("Liquidacion")).count();
		this.pagosReducidos = (int) pagoDynamicModels.stream()
				.filter(pagoModel -> pagoModel.getTipo().equals("Reducido"))
				.count();
		this.cobranzaTotal = MyUtil
				.getDouble(pagoDynamicModels.stream()
						.filter(pagoModel -> !pagoModel.getTipo().equals("Multa"))
						.mapToDouble(PagoDynamicModel::getMonto).sum());
		this.montoExcedente = MyUtil.getDouble(pagoDynamicModels.stream()
				.filter(pagoModel -> pagoModel.getTipo().equals("Excedente"))
				.mapToDouble(pagoModel -> pagoModel.getAbreCon() < pagoModel.getTarifa()
						? pagoModel.getMonto() - pagoModel.getAbreCon()
						: pagoModel.getMonto() - pagoModel.getTarifa())
				.sum());
		this.multas = MyUtil
				.getDouble(pagoDynamicModels.stream()
						.filter(pagoModel -> pagoModel.getTipo().equals("Multa"))
						.mapToDouble(PagoDynamicModel::getMonto)
						.sum());
		this.totalCobranzaPura = this.cobranzaTotal - this.montoExcedente - liquidaciones;
		this.montoDeDebitoFaltante = MyUtil.getDouble(debitoTotal - this.totalCobranzaPura);
	}
}

package tech.calaverita.sfast_xpress.DTOs.numeros_gerencia;

import java.util.List;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.VentaModel;

@Data
public class NumerosGerenciaObjetivosVentasDto {
	private Double objetivo;
	private Double porcentajeAlcanzado;
	private Double ventasNuevas;
	private Double renovaciones;
	private Double totalVentas;
	private Integer cantidadVentasNuevas;
	private Integer cantidadRenovaciones;
	private Integer cantidadTotalVentas;

	public NumerosGerenciaObjetivosVentasDto() {
		this.objetivo = 0.0;
		this.porcentajeAlcanzado = 0.0;
		this.ventasNuevas = 0.0;
		this.renovaciones = 0.0;
		this.totalVentas = 0.0;
		this.cantidadVentasNuevas = 0;
		this.cantidadRenovaciones = 0;
		this.cantidadTotalVentas = 0;
	}

	public NumerosGerenciaObjetivosVentasDto(List<VentaModel> ventaModels) {
		this();
		this.ventasNuevas = ventaModels.stream().filter(ventaModel -> ventaModel.getTipo().equals("Nuevo"))
				.mapToDouble(VentaModel::getMonto).sum();
		this.renovaciones = ventaModels.stream().filter(ventaModel -> ventaModel.getTipo().equals("Renovación"))
				.mapToDouble(VentaModel::getMonto).sum();
		this.totalVentas = this.ventasNuevas + this.renovaciones;
		this.cantidadVentasNuevas = (int) ventaModels.stream()
				.filter(ventaModel -> ventaModel.getTipo().equals("Nuevo"))
				.count();
		this.cantidadRenovaciones = (int) ventaModels.stream()
				.filter(ventaModel -> ventaModel.getTipo().equals("Renovación"))
				.count();
		this.cantidadTotalVentas = this.cantidadVentasNuevas + this.cantidadRenovaciones;
	}
}

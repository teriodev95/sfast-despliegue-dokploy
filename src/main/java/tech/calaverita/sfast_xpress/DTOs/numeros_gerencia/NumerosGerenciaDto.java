package tech.calaverita.sfast_xpress.DTOs.numeros_gerencia;

import java.util.List;

import lombok.Data;
import tech.calaverita.sfast_xpress.DTOs.numeros_gerencia.metricas.NumerosGerenciaMetricasClientesDto;
import tech.calaverita.sfast_xpress.DTOs.numeros_gerencia.metricas.NumerosGerenciaMetricasFinancierasDto;
import tech.calaverita.sfast_xpress.models.mariaDB.CierreGerenciaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.VentaModel;
import tech.calaverita.sfast_xpress.pojos.CobranzaAgencia;

@Data
public class NumerosGerenciaDto {
        private String titulo;
        private NumerosGerenciaMetricasFinancierasDto metricasFinancieras;
        private NumerosGerenciaMetricasClientesDto metricasClientes;
        private NumerosGerenciaObjetivosVentasDto objetivosVentas;

        public NumerosGerenciaDto() {
                this.titulo = "NUMEROS DE LA GERENCIA";
                this.metricasFinancieras = new NumerosGerenciaMetricasFinancierasDto();
                this.metricasClientes = new NumerosGerenciaMetricasClientesDto();
                this.objetivosVentas = new NumerosGerenciaObjetivosVentasDto();
        }

        public NumerosGerenciaDto(CierreGerenciaModel cierreGerenciaModel,
                        List<CobranzaAgencia> cobranzaAgenciaSemanaActual, List<VentaModel> ventaModels) {
                this();

                this.metricasFinancieras = new NumerosGerenciaMetricasFinancierasDto(
                                cierreGerenciaModel, cobranzaAgenciaSemanaActual);
                this.metricasClientes = new NumerosGerenciaMetricasClientesDto(
                                cierreGerenciaModel, cobranzaAgenciaSemanaActual);
                this.objetivosVentas = new NumerosGerenciaObjetivosVentasDto(ventaModels);
        }
}

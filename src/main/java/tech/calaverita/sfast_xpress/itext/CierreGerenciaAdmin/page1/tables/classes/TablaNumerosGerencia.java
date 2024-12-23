package tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes;

import lombok.Data;

@Data
public class TablaNumerosGerencia {
    private String titulo;
    private MetricasFinancieras metricasFinancieras;
    private MetricasClientes metricasClientes;
    private ObjetivosVentas objetivosVentas;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public MetricasFinancieras getMetricasFinancieras() {
        return metricasFinancieras;
    }

    public void setMetricasFinancieras(MetricasFinancieras metricasFinancieras) {
        this.metricasFinancieras = metricasFinancieras;
    }

    public MetricasClientes getMetricasClientes() {
        return metricasClientes;
    }

    public void setMetricasClientes(MetricasClientes metricasClientes) {
        this.metricasClientes = metricasClientes;
    }

    public ObjetivosVentas getObjetivosVentas() {
        return objetivosVentas;
    }

    public void setObjetivosVentas(ObjetivosVentas objetivosVentas) {
        this.objetivosVentas = objetivosVentas;
    }
}

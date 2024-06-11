package tech.calaverita.sfast_xpress.DTOs.reporte_general_gerencia;

import lombok.Data;

@Data
public class ArrastreReporteGeneralGerenciaDTO {
    private Integer semana;
    private Double debitoTotal;
    private Double cobranzaPura;
    private Double porcentajeCobranzaPura;
    private Double ventas;
}

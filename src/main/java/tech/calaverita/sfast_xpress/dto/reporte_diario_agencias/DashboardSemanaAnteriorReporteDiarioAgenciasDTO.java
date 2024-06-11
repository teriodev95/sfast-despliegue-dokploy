package tech.calaverita.sfast_xpress.dto.reporte_diario_agencias;

import lombok.Data;

@Data
public class DashboardSemanaAnteriorReporteDiarioAgenciasDTO {
    private Double debitoTotal;
    private Double cobranzaPura;
    private Double porcentajeCobranzaPura;
    private Double faltante;
    private Double diferenciaAcumuladaFaltantes;
    private Integer clientesCobradosMiercoles;
    private Integer clientesCobradosJueves;
    private Integer clientesTotalesCobrados;
    private Integer clientesTotales;
    private Double cobranzaTotal;
    private Double excedente;
    private Double totalVentas;
}

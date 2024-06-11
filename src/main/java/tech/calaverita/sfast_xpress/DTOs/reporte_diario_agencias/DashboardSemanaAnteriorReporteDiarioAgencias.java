package tech.calaverita.sfast_xpress.DTOs.reporte_diario_agencias;

import lombok.Data;

@Data
public class DashboardSemanaAnteriorReporteDiarioAgencias {
    Double debitoTotal;
    Double cobranzaPura;
    Double porcentajeCobranzaPura;
    Double faltante;
    Double diferenciaAcumuladaFaltantes;
    Integer clientesCobradosMiercoles;
    Integer clientesCobradosJueves;
    Integer clientesTotalesCobrados;
    Integer clientesTotales;
    Double cobranzaTotal;
    Double excedente;
    Double totalVentas;
}

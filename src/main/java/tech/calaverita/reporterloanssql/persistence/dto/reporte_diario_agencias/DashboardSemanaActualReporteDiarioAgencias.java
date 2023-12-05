package tech.calaverita.reporterloanssql.persistence.dto.reporte_diario_agencias;

import lombok.Data;

@Data
public class DashboardSemanaActualReporteDiarioAgencias {
    Double debitoTotal;
    Double cobranzaPura;
    Double porcentajeCobranzaPura;
    Double faltante;
    Double diferenciaFaltanteActualVsFaltanteAnterior;
    Double diferenciaAcumuladaFaltantes;
    Integer clientesCobradosMiercoles;
    Integer clientesCobradosJueves;
    Integer clientesTotalesCobrados;
    Integer clientesTotales;
    Double cobranzaTotal;
    Double excedente;
    Integer ventasTotales;
    Double totalVentas;
    Double efectivoActualAgente;
    Boolean isAgenciaCerrada;
}

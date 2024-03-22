package tech.calaverita.reporterloanssql.dto.solicitud;

import lombok.Data;

@Data
public class EgresosDTO {
    private String pagoMensualRenta;
    private String gastoMensualAlimentos;
    private String pagoServicios;
    private String gastoMensualTransporte;
    private String pagoEstudios;
    private String otrosGastos;
    private String totalGastos;
}

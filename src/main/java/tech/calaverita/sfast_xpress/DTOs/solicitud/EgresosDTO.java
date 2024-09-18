package tech.calaverita.sfast_xpress.DTOs.solicitud;

import lombok.Data;

@Data
public class EgresosDTO {
    private Double pagoMensualRenta;
    private Double gastoMensualAlimentos;
    private Double pagoServicios;
    private Double gastoMensualTransporte;
    private Double pagoEstudios;
    private Double otrosGastos;
    private Double totalGastos;
}

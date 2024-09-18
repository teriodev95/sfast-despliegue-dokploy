package tech.calaverita.sfast_xpress.DTOs.solicitud;

import lombok.Data;

@Data
public class ActivoDTO {
    private String activo;
    private String marca;
    private String antiguedad;
    private String noSerie;
    private Double valorAprox;
}

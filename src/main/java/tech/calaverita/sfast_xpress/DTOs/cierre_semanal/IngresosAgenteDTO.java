package tech.calaverita.sfast_xpress.DTOs.cierre_semanal;

import lombok.Data;

@Data
public class IngresosAgenteDTO {
    private Double cobranzaPura;
    private Double montoExcedente;
    private Double liquidaciones;
    private Double multas;
    private Double otros = 0.0;
    private String motivoOtros = "";
    private Double total;
}

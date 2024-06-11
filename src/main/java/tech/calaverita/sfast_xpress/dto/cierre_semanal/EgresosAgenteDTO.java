package tech.calaverita.sfast_xpress.dto.cierre_semanal;

import lombok.Data;

@Data
public class EgresosAgenteDTO {
    private Double asignaciones;
    private Double otros = 0.0;
    private String motivoOtros = "";
    private Double efectivoEntregadoCierre;
    private Double total;
}

package tech.calaverita.reporterloanssql.dto.cierre_semanal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
public class EgresosAgenteDTO {
    private Double asignaciones;
    private Double otros = 0.0;
    private String motivoOtros = "";
    private Double efectivoEntregadoCierre;
    private Double total;
}

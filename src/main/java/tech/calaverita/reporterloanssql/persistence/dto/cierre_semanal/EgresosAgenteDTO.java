package tech.calaverita.reporterloanssql.persistence.dto.cierre_semanal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
public class EgresosAgenteDTO {
    Double asignaciones;
    Double otros;
    Double efectivoEntregadoCierre;
    Double total;
}

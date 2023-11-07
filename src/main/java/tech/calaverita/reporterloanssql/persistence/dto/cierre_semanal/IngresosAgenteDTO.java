package tech.calaverita.reporterloanssql.persistence.dto.cierre_semanal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
public class IngresosAgenteDTO {
    Double cobranzaPura;
    Double montoExcedente;
    Double liquidaciones;
    Double multas;
    Double otros;
    Double total;
}

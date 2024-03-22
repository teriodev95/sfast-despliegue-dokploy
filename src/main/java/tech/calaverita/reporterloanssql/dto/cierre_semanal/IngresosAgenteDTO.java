package tech.calaverita.reporterloanssql.dto.cierre_semanal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

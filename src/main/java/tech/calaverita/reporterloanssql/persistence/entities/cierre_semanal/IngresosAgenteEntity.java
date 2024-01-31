package tech.calaverita.reporterloanssql.persistence.entities.cierre_semanal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "ingresos_agentes")
public class IngresosAgenteEntity {
    @Id
    private String id;
    private Double cobranzaPura;
    private Double montoExcedente;
    private Double liquidaciones;
    private Double multas;
    private Double otros;
    private String motivoOtros;

    private Double total;
}

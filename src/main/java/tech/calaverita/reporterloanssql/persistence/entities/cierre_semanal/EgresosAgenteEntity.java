package tech.calaverita.reporterloanssql.persistence.entities.cierre_semanal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "egresos_agente")
public class EgresosAgenteEntity {
    @Id
    private String id;
    private Double asignaciones;
    private Double otros;
    private Double efectivoEntregadoCierre;
    private Double total;
}

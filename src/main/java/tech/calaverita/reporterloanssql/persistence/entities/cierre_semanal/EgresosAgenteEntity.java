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
    String id;
    Double asignaciones;
    Double otros;
    Double efectivoEntregadoCierre;
    Double total;
}

package tech.calaverita.reporterloanssql.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CollectionIdJdbcTypeCode;

@Data
@Entity
@Table(name = "asignaciones")
public class AsignacionModel {
    @Id
    @Column(name = "asignacionid")
    private String asignacionId;
    private Double monto;
    private String agencia;
    private String gerencia;
    private int semana;
    private int anio;
    private int quienRecibioUsuarioId;
    private int quienEntregoUsuarioId;
    private String log;
    private String createdAt;
    private String updatedAt;
}

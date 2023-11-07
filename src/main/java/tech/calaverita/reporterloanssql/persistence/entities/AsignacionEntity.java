package tech.calaverita.reporterloanssql.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "asignaciones")
public class AsignacionEntity {
    @Id
    @Column(name = "asignacionid")
    private String asignacionId;
    private Double monto;
    private String agencia;
    private String gerencia;
    private Integer semana;
    private Integer anio;
    private Integer quienRecibioUsuarioId;
    private Integer quienEntregoUsuarioId;
    private String log;
    private String createdAt;
    private String updatedAt;
}

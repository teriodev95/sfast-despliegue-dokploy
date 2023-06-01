package tech.calaverita.reporterloanssql.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "visitas")
public class VisitaModel {
    @Id
    @Column(name = "visitaid")
    private String visitaId;
    @Column(name = "prestamoid")
    private String prestamoId;
    private Integer semana;
    private Integer anio;
    private String cliente;
    private String agente;
    private String fecha;
    private Double lat;
    private Double lng;
    private String createdAt;
    private String updatedAt;
}

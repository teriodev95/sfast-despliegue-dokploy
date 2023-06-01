package tech.calaverita.reporterloanssql.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "calendario")
public class CalendarioModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer semana;
    private Integer anio;
    private String desde;
    private String hasta;
}

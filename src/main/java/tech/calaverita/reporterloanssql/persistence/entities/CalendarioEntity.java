package tech.calaverita.reporterloanssql.persistence.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "calendario")
public class CalendarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer semana;
    private Integer anio;
    private String desde;
    private String hasta;
}

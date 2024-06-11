package tech.calaverita.sfast_xpress.models.mariaDB;

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
    private String mes;
    private String desde;
    private String hasta;
    private boolean pagoBono;
}

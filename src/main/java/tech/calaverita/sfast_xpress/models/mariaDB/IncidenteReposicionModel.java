package tech.calaverita.sfast_xpress.models.mariaDB;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "incidentes_reposiciones")
public class IncidenteReposicionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String categoria;
    private String tipo;
    private LocalDate fecha;
    private String comentario;
    private Double monto;
    private Integer usuarioId;
    private String gerencia;
    private Integer semana;
    private Integer anio;
}

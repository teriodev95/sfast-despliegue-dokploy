package tech.calaverita.sfast_xpress.models.mariaDB;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "asignaciones")
public class AsignacionModel {
    @Id
    @Column(name = "asignacionid")
    private String asignacionId;
    private Double monto;
    private Integer semana;
    private Integer anio;
    private String agencia;
    private String gerencia;
    private Integer quienRecibioUsuarioId;
    private Integer quienEntregoUsuarioId;
    private String tipo;
    private String log;
    private String createdAt;
    private String updatedAt;
    @ManyToOne
    @JoinColumn(name = "quienRecibioUsuarioId", insertable = false, updatable = false)
    @JsonIgnore
    private UsuarioModel recibioUsuarioModel;
    @ManyToOne
    @JoinColumn(name = "quienEntregoUsuarioId", insertable = false, updatable = false)
    @JsonIgnore
    private UsuarioModel entregoUsuarioModel;

    @PostLoad
    private void ajustarZonaHoraria() {
        if (createdAt != null) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                ZoneId zonaMexico = ZoneId.of("America/Mexico_City");

                // Convertir de String a LocalDateTime (asumiendo que está en UTC)
                LocalDateTime fechaUTC = LocalDateTime.parse(createdAt, formatter);
                ZonedDateTime fechaMexico = fechaUTC.atZone(ZoneId.of("UTC")).withZoneSameInstant(zonaMexico);

                // Guardar el ajuste en el mismo campo
                createdAt = fechaMexico.format(formatter);
            } catch (Exception e) {
                System.err.println("Error al ajustar zona horaria: " + e.getMessage());
            }
        }
    }
}

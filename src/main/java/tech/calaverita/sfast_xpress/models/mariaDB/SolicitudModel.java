package tech.calaverita.sfast_xpress.models.mariaDB;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "solicitudes")
public class SolicitudModel {
    @Id
    private String id;
    private String agencia;
    private String gerencia;
    private Integer semana;
    private Integer anio;
    private String fechaSolicitud;
    private String status;
    private String solicitud;
    private String createdAt;
    private String updatedAt;
}

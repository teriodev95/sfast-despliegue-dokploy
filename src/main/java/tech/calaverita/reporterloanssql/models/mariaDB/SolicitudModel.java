package tech.calaverita.reporterloanssql.models.mariaDB;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import tech.calaverita.reporterloanssql.dto.solicitud.SolicitudDTO;
import tech.calaverita.reporterloanssql.enums.StatusSolicitudEnum;

@Data
@Entity
@Table(name = "solicitudes")
public class SolicitudModel {
    @Id
    private String id;
    private String agente;
    private String gerente;
    private Integer semana;
    private Integer anio;
    private String fechaSolicitud;
    private String status;
    private String solicitud;
    private String createdAt;
    private String updatedAt;
}

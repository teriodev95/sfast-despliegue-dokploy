package tech.calaverita.reporterloanssql.dto.solicitud;

import jakarta.persistence.Transient;
import lombok.Data;
import tech.calaverita.reporterloanssql.enums.StatusSolicitudEnum;
import tech.calaverita.reporterloanssql.models.mariaDB.SolicitudModel;

@Data
public class SolicitudDTO {
    private String solicitudId;
    private ClienteDTO cliente;
    private AvalDTO aval;
    private CreditoDTO credito;
    private String fechaSolicitud;
    private String agente;
    private String gerente;
    private int semana;
    private int anio;
    private String status;
}

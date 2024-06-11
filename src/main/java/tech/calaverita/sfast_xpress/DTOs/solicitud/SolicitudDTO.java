package tech.calaverita.sfast_xpress.DTOs.solicitud;

import lombok.Data;

@Data
public class SolicitudDTO {
    private String solicitudId;
    private ClienteDTO cliente;
    private AvalDTO aval;
    private CreditoDTO credito;
    private String fechaSolicitud;
    private String agencia;
    private String gerencia;
    private int semana;
    private int anio;
    private String status;
}

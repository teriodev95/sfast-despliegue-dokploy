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
    private StatusSolicitudDTO[] historial;
    private CoordenadasDTO coordenadas;

    public void monetaryToDouble() {
        this.cliente.getIngresos().monetaryToDouble();
        this.cliente.getEgresos().monetaryToDouble();
        this.cliente.getActivos().monetaryToDouble();
        for (ActivoDTO activo : this.cliente.getActivos().getActivosElectr()) {
            activo.monetaryToDouble();
        }
        for (ActivoDTO activo : this.cliente.getActivos().getActivosLB()) {
            activo.monetaryToDouble();
        }
        this.aval.getIngresos().monetaryToDouble();
        this.aval.getActivos().monetaryToDouble();
        for (ActivoDTO activo : this.aval.getActivos().getActivosElectr()) {
            activo.monetaryToDouble();
        }
        for (ActivoDTO activo : this.aval.getActivos().getActivosLB()) {
            activo.monetaryToDouble();
        }
        this.credito.getDatos().monetaryToDouble();
    }
}

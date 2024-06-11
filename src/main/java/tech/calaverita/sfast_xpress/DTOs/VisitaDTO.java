package tech.calaverita.sfast_xpress.DTOs;

import lombok.Data;

@Data
public class VisitaDTO {
    private String visitaId;
    private String prestamoId;
    private Integer semana;
    private Integer anio;
    private String cliente;
    private String agente;
    private String fecha;
    private Double lat;
    private Double lng;
    private String createdAt;
    private String updatedAt;
    private Log log;

    @Data
    public static class Log {
        private String status;
        private String observaciones;
        private String reporteId;
        private String creadaPor;
    }
}

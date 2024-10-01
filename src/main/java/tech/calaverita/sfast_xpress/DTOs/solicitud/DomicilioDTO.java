package tech.calaverita.sfast_xpress.DTOs.solicitud;

import lombok.Data;

@Data
public class DomicilioDTO {
    private String cp;
    private String municipio;
    private String estado;
    private String calle;
    private String colonia;
    private String numExt;
    private String numInt;
    private String referencia;
    private String tipoPropiedad;
    private String tiempoViviendo;
    private String noServicio;
    private String tipoComprobante;
}

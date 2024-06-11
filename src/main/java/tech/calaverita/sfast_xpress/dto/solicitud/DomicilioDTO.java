package tech.calaverita.sfast_xpress.dto.solicitud;

import lombok.Data;

@Data
public class DomicilioDTO {
    private String cp;
    private String municipio;
    private String estado;
    private String calle;
    private String numExt;
    private String numInt;
    private String referencia;
    private String tipoPropiedad;
    private String tiempoViviendo;
}

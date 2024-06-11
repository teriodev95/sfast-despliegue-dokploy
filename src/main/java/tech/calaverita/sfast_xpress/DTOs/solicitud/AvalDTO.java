package tech.calaverita.sfast_xpress.DTOs.solicitud;

import lombok.Data;

@Data
public class AvalDTO {
    private DatosPersonaDTO datos;
    private DomicilioDTO domicilio;
    private ReferenciaDTO referencia;
    private IngresosDTO ingresos;
    private ActivosDTO activos;
}

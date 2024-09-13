package tech.calaverita.sfast_xpress.DTOs.solicitud;

import lombok.Data;

@Data
public class ClienteDTO {
    private DatosPersonaDTO datos;
    private DomicilioDTO domicilio;
    private ReferenciaDTO referencia;
    private IngresosDTO ingresos;
    private EgresosDTO egresos;
    private ActivosDTO activos;
    private DocumentosDTO documentos;
}

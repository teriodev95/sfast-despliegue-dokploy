package tech.calaverita.sfast_xpress.DTOs.solicitud;

import lombok.Data;

@Data
public class CoordenadasDTO {
    private UbicacionDTO ubicacionLevantamiento;
    private UbicacionDTO ubicacionDesembolso;
}

package tech.calaverita.sfast_xpress.dto.cierre_semanal;

import lombok.Data;

@Data
public class EgresosGerenteDTO {
    private Integer porcentajeComisionCobranza;
    private Integer porcentajeBonoMensual;
    private Double pagoComisionCobranza;
    private Double pagoComisionVentas = 0.0;
    private Double bonos;
    private Double efectivoRestanteCierre;
}

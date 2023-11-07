package tech.calaverita.reporterloanssql.persistence.dto.cierre_semanal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
public class EgresosGerenteDTO {
    Integer porcentajeComisionCobranza;
    Integer porcentajeBonoMensual;
    Double pagoComisionCobranza;
    Double pagoComisionVentas;
    Double bonos;
    Double efectivoRestanteCierre;
}

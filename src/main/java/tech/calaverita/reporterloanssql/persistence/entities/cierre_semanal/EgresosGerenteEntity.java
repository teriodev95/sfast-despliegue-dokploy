package tech.calaverita.reporterloanssql.persistence.entities.cierre_semanal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "egresos_gerente")
public class EgresosGerenteEntity {
    @Id
    String id;
    Integer porcentajeComisionCobranza;
    Integer porcentajeBonoMensual;
    Double pagoComisionCobranza;
    Double pagoComisionVentas;
    Double bonos;
    Double efectivoRestanteCierre;
}

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
    private String id;
    private Integer porcentajeComisionCobranza;
    private Integer porcentajeBonoMensual;
    private Double pagoComisionCobranza;
    private Double pagoComisionVentas;
    private Double bonos;
    private Double efectivoRestanteCierre;
}

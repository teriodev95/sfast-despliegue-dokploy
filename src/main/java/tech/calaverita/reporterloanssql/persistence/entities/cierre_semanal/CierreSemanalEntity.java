package tech.calaverita.reporterloanssql.persistence.entities.cierre_semanal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "cierres_semanales")
public class CierreSemanalEntity {
    @Id
    private String id;
    private String balanceAgenciaId;
    private String egresosAgenteId;
    private String egresosGerenteId;
    private String ingresosAgenteId;
    private Integer semana;
    private Integer anio;
    private Integer dia;
    private String mes;
    private String pdf;
    private String selfieAgente;
    private String selfieGerente;
}

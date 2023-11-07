package tech.calaverita.reporterloanssql.persistence.entities.cierre_semanal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "cierre_semanal")
public class CierreSemanalEntity {
    @Id
    String id;
    String balanceAgenciaId;
    String egresosAgenteId;
    String egresosGerenteId;
    String ingresosAgenteId;
    Integer semana;
    Integer anio;
    Integer dia;
    Integer mes;
    String selfieAgente;
    String selfieGerente;
}

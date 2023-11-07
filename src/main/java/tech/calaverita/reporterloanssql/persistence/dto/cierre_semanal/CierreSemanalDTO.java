package tech.calaverita.reporterloanssql.persistence.dto.cierre_semanal;

import lombok.Data;

@Data
public class CierreSemanalDTO {
    Integer semana;
    Integer anio;
    Integer dia;
    Integer mes;
    String selfieAgente;
    String selfieGerente;
    BalanceAgenciaDTO balanceAgencia;
    EgresosAgenteDTO egresosAgente;
    EgresosGerenteDTO egresosGerente;
    IngresosAgenteDTO ingresosAgente;
}

package tech.calaverita.reporterloanssql.persistence.dto.cierre_semanal;

import lombok.Data;

@Data
public class CierreSemanalDTO {
    private Integer semana;
    private Integer anio;
    private Integer dia;
    private Integer mes;
    private String selfieAgente;
    private String selfieGerente;
    private BalanceAgenciaDTO balanceAgencia;
    private EgresosAgenteDTO egresosAgente;
    private EgresosGerenteDTO egresosGerente;
    private IngresosAgenteDTO ingresosAgente;
    private Integer pinAgente;
}

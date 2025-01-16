package tech.calaverita.sfast_xpress.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import tech.calaverita.sfast_xpress.DTOs.cierre_semanal.BalanceAgenciaDTO;
import tech.calaverita.sfast_xpress.DTOs.cierre_semanal.ComisionesAPagarEnSemanaDTO;
import tech.calaverita.sfast_xpress.DTOs.cierre_semanal.EgresosAgenteDTO;
import tech.calaverita.sfast_xpress.DTOs.cierre_semanal.IngresosAgenteDTO;

@Data
public class CierreSemanalConsolidadoV2DTO {
    private Integer semana;
    private Integer anio;
    private String sucursal;
    private String mes;
    private Boolean isAgenciaCerrada;
    @JsonProperty(value = "balanceAgencia")
    private BalanceAgenciaDTO balanceAgenciaDTO;
    @JsonProperty(value = "egresosAgente")
    private EgresosAgenteDTO egresosAgenteDTO;
    @JsonProperty(value = "comisionesApagarEnSemana")
    private ComisionesAPagarEnSemanaDTO comisionesAPagarEnSemanaDTO;
    @JsonProperty(value = "ingresosAgente")
    private IngresosAgenteDTO ingresosAgenteDTO;
    private Double efectivoRestanteCierre;
    private Integer pinAgente;
    private String statusAgencia;
    private String uidVerificacionAgente;
    private String uidVerificacionGerente;
}

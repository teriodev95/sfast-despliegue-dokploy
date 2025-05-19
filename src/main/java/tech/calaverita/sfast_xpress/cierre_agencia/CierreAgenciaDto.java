package tech.calaverita.sfast_xpress.cierre_agencia;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import tech.calaverita.sfast_xpress.cierre_agencia.pojo.BalanceAgencia;
import tech.calaverita.sfast_xpress.cierre_agencia.pojo.ComisionesAPagarEnSemana;
import tech.calaverita.sfast_xpress.cierre_agencia.pojo.EgresosAgente;
import tech.calaverita.sfast_xpress.cierre_agencia.pojo.IngresosAgente;
import tech.calaverita.sfast_xpress.dashboard_agencia.DashboardAgenciaDto;

@Data
public class CierreAgenciaDto {
    private Long id;
    private Integer semana;
    private Integer anio;
    private String sucursal;
    private String mes;
    private Boolean isAgenciaCerrada;
    @JsonProperty(value = "balanceAgencia")
    private BalanceAgencia balanceAgencia;
    @JsonProperty(value = "egresosAgente")
    private EgresosAgente egresosAgente;
    @JsonProperty(value = "comisionesApagarEnSemana")
    private ComisionesAPagarEnSemana comisionesAPagarEnSemana;
    @JsonProperty(value = "ingresosAgente")
    private IngresosAgente ingresosAgente;
    private Double efectivoRestanteCierre;
    private Integer pinAgente;
    private String statusAgencia;
    private String uidVerificacionAgente;
    private String uidVerificacionGerente;

    public CierreAgenciaDto() {
        this.id = null;
        this.semana = 0;
        this.anio = 0;
        this.sucursal = "";
        this.mes = "";
        this.isAgenciaCerrada = false;
        this.efectivoRestanteCierre = 0.0;
        this.pinAgente = 0;
        this.statusAgencia = "ACTIVA";
        this.uidVerificacionAgente = null;
        this.uidVerificacionGerente = null;
        this.balanceAgencia = new BalanceAgencia();
        this.egresosAgente = new EgresosAgente();
        this.comisionesAPagarEnSemana = new ComisionesAPagarEnSemana();
        this.ingresosAgente = new IngresosAgente();
    }

    public CierreAgenciaDto(CierreAgenciaModel cierreAgenciaModel) {
        this();
        this.id = cierreAgenciaModel.getId();
        this.semana = cierreAgenciaModel.getSemana();
        this.anio = cierreAgenciaModel.getAnio();
        this.efectivoRestanteCierre = cierreAgenciaModel.getEfectivoRestanteCierre();
        this.isAgenciaCerrada = true;
        this.uidVerificacionAgente = cierreAgenciaModel.getUidVerificacionAgente();
        this.uidVerificacionGerente = cierreAgenciaModel.getUidVerificacionGerente();
    }

    public CierreAgenciaDto(String sucursal, String mes, int pinAgente) {
        this();
        this.mes = mes;
        this.sucursal = sucursal;
        this.pinAgente = pinAgente;
    }

    public CierreAgenciaDto dashboardAgenciaDto(DashboardAgenciaDto dashboardAgenciaDto) {
        this.semana = dashboardAgenciaDto.getSemana();
        this.anio = dashboardAgenciaDto.getAnio();
        this.efectivoRestanteCierre = dashboardAgenciaDto.getEfectivoEnCampo();
        return this;
    }

    public CierreAgenciaDto setBalanceAgencia(BalanceAgencia balanceAgencia) {
        this.balanceAgencia = balanceAgencia;
        return this;
    }

    public CierreAgenciaDto setComisionesAPagarEnSemana(ComisionesAPagarEnSemana comisionesAPagarEnSemanaDTO) {
        this.comisionesAPagarEnSemana = comisionesAPagarEnSemanaDTO;
        return this;
    }

    public CierreAgenciaDto setEgresosAgente(EgresosAgente egresosAgenteDTO) {
        this.egresosAgente = egresosAgenteDTO;
        return this;
    }

    public CierreAgenciaDto setIngresosAgente(IngresosAgente ingresosAgenteDTO) {
        this.ingresosAgente = ingresosAgenteDTO;
        return this;
    }
}

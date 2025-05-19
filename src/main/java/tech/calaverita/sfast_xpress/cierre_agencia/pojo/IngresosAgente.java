package tech.calaverita.sfast_xpress.cierre_agencia.pojo;

import lombok.Data;
import tech.calaverita.sfast_xpress.cierre_agencia.CierreAgenciaModel;
import tech.calaverita.sfast_xpress.dashboard_agencia.DashboardAgenciaDto;
import tech.calaverita.sfast_xpress.models.mariaDB.ComisionModel;

@Data
public class IngresosAgente {
    private Double cobranzaPura;
    private Double montoExcedente;
    private Double liquidaciones;
    private Double multas;
    private Double otros;
    private String motivoOtros;
    private Double total;

    public IngresosAgente() {
        this.cobranzaPura = 0.0;
        this.montoExcedente = 0.0;
        this.liquidaciones = 0.0;
        this.multas = 0.0;
        this.otros = 0.0;
        this.motivoOtros = "";
        this.total = 0.0;
    }

    public IngresosAgente(CierreAgenciaModel cierreAgenciaModel, ComisionModel comisionModel) {
        this();
        if (comisionModel != null) {
            this.cobranzaPura = comisionModel.getCobranzaPura();
            this.montoExcedente = comisionModel.getMontoExcedente();
        }
        this.liquidaciones = cierreAgenciaModel.getLiquidaciones();
        this.multas = cierreAgenciaModel.getMultas();
        this.otros = cierreAgenciaModel.getOtrosIngresos();
        this.motivoOtros = cierreAgenciaModel.getMotivoOtrosIngresos();
    }

    public IngresosAgente(DashboardAgenciaDto dashboardAgenciaDto) {
        this();
        this.cobranzaPura = dashboardAgenciaDto.getTotalCobranzaPura();
        this.montoExcedente = dashboardAgenciaDto.getMontoExcedente();
        this.liquidaciones = dashboardAgenciaDto.getLiquidaciones();
        this.multas = dashboardAgenciaDto.getMultas();
    }
}

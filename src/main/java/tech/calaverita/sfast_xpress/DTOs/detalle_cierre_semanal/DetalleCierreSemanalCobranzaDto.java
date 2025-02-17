package tech.calaverita.sfast_xpress.DTOs.detalle_cierre_semanal;

import java.util.List;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.CierreSemanalConsolidadoV2Model;
import tech.calaverita.sfast_xpress.models.mariaDB.ComisionModel;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class DetalleCierreSemanalCobranzaDto {
    private Double cobranzaPura;
    private Double montoExcedente;
    private Double liquidaciones;
    private Double primerosPagos;
    private Double multas;
    private Double otros;
    private Double total;

    public DetalleCierreSemanalCobranzaDto() {
        this.cobranzaPura = 0D;
        this.montoExcedente = 0D;
        this.liquidaciones = 0D;
        this.primerosPagos = 0D;
        this.multas = 0D;
        this.otros = 0D;
        this.total = 0D;
    }

    public DetalleCierreSemanalCobranzaDto(List<CierreSemanalConsolidadoV2Model> cierreSemanalConsolidadoV2Models,
            List<ComisionModel> comisionModels) {
        this();

        for (CierreSemanalConsolidadoV2Model cierreSemanalConsolidadoV2Model : cierreSemanalConsolidadoV2Models) {
            this.liquidaciones += cierreSemanalConsolidadoV2Model.getLiquidaciones();
            this.multas += cierreSemanalConsolidadoV2Model.getMultas();
            this.otros += cierreSemanalConsolidadoV2Model.getOtrosIngresos();
        }

        for (ComisionModel comisionModel : comisionModels) {
            this.primerosPagos += comisionModel.getPrimerosPagos();
            this.cobranzaPura += comisionModel.getCobranzaPura();
            this.montoExcedente += comisionModel.getMontoExcedente();
        }

        this.total = this.cobranzaPura + this.montoExcedente + this.liquidaciones + this.primerosPagos + this.multas
                + this.otros;

        formatDoubles();
    }

    private void formatDoubles() {
        this.cobranzaPura = MyUtil.getDouble(this.cobranzaPura);
        this.montoExcedente = MyUtil.getDouble(this.montoExcedente);
        this.liquidaciones = MyUtil.getDouble(this.liquidaciones);
        this.primerosPagos = MyUtil.getDouble(this.primerosPagos);
        this.multas = MyUtil.getDouble(this.multas);
        this.otros = MyUtil.getDouble(this.otros);
        this.total = MyUtil.getDouble(this.total);
    }
}

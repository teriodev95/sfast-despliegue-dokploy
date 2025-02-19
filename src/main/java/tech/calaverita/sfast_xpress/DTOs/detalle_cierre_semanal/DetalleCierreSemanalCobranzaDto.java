package tech.calaverita.sfast_xpress.DTOs.detalle_cierre_semanal;

import lombok.Data;
import tech.calaverita.sfast_xpress.pojos.CobranzaGerencia;
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

    public DetalleCierreSemanalCobranzaDto(CobranzaGerencia cobranzaGerencia) {
        this();

        this.liquidaciones += cobranzaGerencia.getLiquidaciones();
        this.multas += cobranzaGerencia.getMultas();
        this.otros += cobranzaGerencia.getOtros();
        this.primerosPagos += cobranzaGerencia.getPrimerosPagos();
        this.cobranzaPura += cobranzaGerencia.getCobranzaPura();
        this.montoExcedente += cobranzaGerencia.getMontoExcedente();

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

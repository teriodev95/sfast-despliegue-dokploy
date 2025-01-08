package tech.calaverita.sfast_xpress.DTOs.cierre_semanal;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal.CierreSemanalModel;

@Data
public class IngresosAgenteDTO {
    private Double cobranzaPura;
    private Double montoExcedente;
    private Double liquidaciones;
    private Double multas;
    private Double otros = 0.0;
    private String motivoOtros = "";
    private Double total;

    public IngresosAgenteDTO() {

    }

    public IngresosAgenteDTO(CierreSemanalModel cierreSemanalModel) {
        this.cobranzaPura = cierreSemanalModel.getCobranzaPura();
        this.montoExcedente = cierreSemanalModel.getMontoExcedente();
        this.liquidaciones = cierreSemanalModel.getLiquidaciones();
        this.multas = cierreSemanalModel.getMultas();
        this.otros = cierreSemanalModel.getOtrosIngresos();
        this.motivoOtros = cierreSemanalModel.getMotivoOtrosIngresos();
    }
}

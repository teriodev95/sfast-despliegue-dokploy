package tech.calaverita.sfast_xpress.DTOs.cierre_semanal;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.CierreSemanalConsolidadoV2Model;
import tech.calaverita.sfast_xpress.models.mariaDB.ComisionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal.CierreSemanalModel;

@Data
public class EgresosGerenteDTO {
    private Integer porcentajeComisionCobranza;
    private Integer porcentajeBonoMensual;
    private Double pagoComisionCobranza;
    private Double pagoComisionVentas = 0.0;
    private Double bonos;
    private Double efectivoRestanteCierre;

    public EgresosGerenteDTO() {

    }

    public EgresosGerenteDTO(CierreSemanalModel cierreSemanalModel) {
        this.porcentajeComisionCobranza = cierreSemanalModel.getPorcentajeComisionCobranza();
        this.porcentajeBonoMensual = cierreSemanalModel.getPorcentajeBonoMensual();
        this.pagoComisionCobranza = cierreSemanalModel.getPagoComisionCobranza();
        this.pagoComisionVentas = cierreSemanalModel.getPagoComisionVentas();
        this.bonos = cierreSemanalModel.getBonos();
        this.efectivoRestanteCierre = cierreSemanalModel.getEfectivoRestanteCierre();
    }
}

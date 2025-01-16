package tech.calaverita.sfast_xpress.DTOs.cierre_semanal;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.ComisionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal.CierreSemanalModel;

@Data
public class ComisionesAPagarEnSemanaDTO {
    private Integer porcentajeComisionCobranza = 0;
    private Integer porcentajeBonoMensual = 0;
    private Double pagoComisionCobranza = 0.0;
    private Double pagoComisionVentas = 0.0;
    private Double bonos = 0.0;

    public ComisionesAPagarEnSemanaDTO() {

    }

    public ComisionesAPagarEnSemanaDTO(CierreSemanalModel cierreSemanalModel) {
        this.porcentajeComisionCobranza = cierreSemanalModel.getPorcentajeComisionCobranza();
        this.porcentajeBonoMensual = cierreSemanalModel.getPorcentajeBonoMensual();
        this.pagoComisionCobranza = cierreSemanalModel.getPagoComisionCobranza();
        this.pagoComisionVentas = cierreSemanalModel.getPagoComisionVentas();
        this.bonos = cierreSemanalModel.getBonos();
    }

    public ComisionesAPagarEnSemanaDTO(ComisionModel comisionModel) {
        if (comisionModel != null) {
            this.porcentajeComisionCobranza = comisionModel.getPorcentajeComisionCobranza();
            this.porcentajeBonoMensual = comisionModel.getPorcentajeBonoMensual();
            this.pagoComisionCobranza = comisionModel.getComisionCobranza();
            this.pagoComisionVentas = comisionModel.getComisionVentas();
            this.bonos = comisionModel.getBonos();
        }
    }
}

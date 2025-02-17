package tech.calaverita.sfast_xpress.DTOs.detalle_cierre_semanal;

import java.util.List;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.CierreSemanalConsolidadoV2Model;
import tech.calaverita.sfast_xpress.models.mariaDB.VentaModel;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class DetalleCierreSemanalBonosYComisionesDto {
    private Double ventasClientesNuevos;
    private Double ventasRenovaciones;
    private Double totalVentas;
    private Double comisionesCobranza;
    private Double comisionesVentas;
    private Double bonosAgentes;
    private Double total;

    public DetalleCierreSemanalBonosYComisionesDto() {
        this.ventasClientesNuevos = 0D;
        this.ventasRenovaciones = 0D;
        this.totalVentas = 0D;
        this.comisionesCobranza = 0D;
        this.comisionesVentas = 0D;
        this.bonosAgentes = 0D;
        this.total = 0D;
    }

    public DetalleCierreSemanalBonosYComisionesDto(
            List<CierreSemanalConsolidadoV2Model> cierreSemanalConsolidadoV2Models, List<VentaModel> ventaModels) {
        this();

        for (CierreSemanalConsolidadoV2Model cierreSemanalConsolidadoV2 : cierreSemanalConsolidadoV2Models) {
            this.comisionesCobranza += cierreSemanalConsolidadoV2.getComisionCobranzaPagadaEnSemana();
            this.comisionesVentas += cierreSemanalConsolidadoV2.getComisionVentasPagadaEnSemana();
            this.bonosAgentes += cierreSemanalConsolidadoV2.getBonosPagadosEnSemana();
        }

        for (VentaModel ventaModel : ventaModels) {
            Double monto = ventaModel.getMonto();

            if (ventaModel.getTipo().equals("Nuevo")) {
                this.ventasClientesNuevos += monto;
            } else {
                this.ventasRenovaciones += monto;
            }

            this.totalVentas += monto;
        }

        this.total = this.totalVentas + this.comisionesCobranza + this.comisionesVentas + this.bonosAgentes;

        formatDoubles();
    }

    private void formatDoubles() {
        this.ventasClientesNuevos = MyUtil.getDouble(this.ventasClientesNuevos);
        this.ventasRenovaciones = MyUtil.getDouble(this.ventasRenovaciones);
        this.totalVentas = MyUtil.getDouble(this.totalVentas);
        this.comisionesCobranza = MyUtil.getDouble(this.comisionesCobranza);
        this.comisionesVentas = MyUtil.getDouble(this.comisionesVentas);
        this.bonosAgentes = MyUtil.getDouble(this.bonosAgentes);
        this.total = MyUtil.getDouble(this.total);
    }
}

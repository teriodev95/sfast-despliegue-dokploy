package tech.calaverita.sfast_xpress.DTOs.dashboard;

import java.util.ArrayList;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.LiquidacionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PagoAgrupadoModel;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class LiquidacionesDashboardDTO {
    private Double totalDeDescuento;
    private Double liquidaciones;

    public LiquidacionesDashboardDTO(ArrayList<LiquidacionModel> liquidacionModels,
            ArrayList<tech.calaverita.sfast_xpress.models.mariaDB.views.PagoAgrupadoModel> pagoAgrupadoModel) {
        this.totalDeDescuento = MyUtil
                .getDouble(liquidacionModels.stream().mapToDouble(LiquidacionModel::getDescuentoEnDinero).sum());
        this.liquidaciones = MyUtil.getDouble(getLiquidaciones(liquidacionModels, pagoAgrupadoModel));
    }

    private Double getLiquidaciones(ArrayList<LiquidacionModel> liquidacionModels,
            ArrayList<PagoAgrupadoModel> pagoAgrupadoModel) {
        Double liquidaciones = 0D;

        for (LiquidacionModel liquidacionModel : liquidacionModels) {
            for (PagoAgrupadoModel pagoModel : pagoAgrupadoModel) {
                if (liquidacionModel.getPrestamoId().equals(pagoModel.getPrestamoId())) {
                    liquidaciones += liquidacionModel.getLiquidoCon() - pagoModel.getTarifa();
                    break;
                }
            }
        }

        return liquidaciones;
    }
}

package tech.calaverita.sfast_xpress.DTOs.dashboard;

import java.util.ArrayList;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.LiquidacionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.dynamic.PagoDynamicModel;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class LiquidacionesDashboardDTO {
    private Double totalDeDescuento;
    private Double liquidaciones;

    public LiquidacionesDashboardDTO(ArrayList<LiquidacionModel> liquidacionModels,
            ArrayList<PagoDynamicModel> pagoDynamicModels) {
        this.totalDeDescuento = MyUtil
                .getDouble(liquidacionModels.stream().mapToDouble(LiquidacionModel::getDescuentoEnDinero).sum());
        this.liquidaciones = MyUtil.getDouble(getLiquidaciones(liquidacionModels, pagoDynamicModels));
    }

    private Double getLiquidaciones(ArrayList<LiquidacionModel> liquidacionModels,
            ArrayList<PagoDynamicModel> pagoDynamicModels) {
        Double liquidaciones = 0D;

        for (LiquidacionModel liquidacionModel : liquidacionModels) {
            for (PagoDynamicModel pagoDynamicModel : pagoDynamicModels) {
                if (liquidacionModel.getPrestamoId().equals(pagoDynamicModel.getPrestamoId())) {
                    liquidaciones += liquidacionModel.getLiquidoCon() - pagoDynamicModel.getTarifa();
                    break;
                }
            }
        }

        return liquidaciones;
    }
}

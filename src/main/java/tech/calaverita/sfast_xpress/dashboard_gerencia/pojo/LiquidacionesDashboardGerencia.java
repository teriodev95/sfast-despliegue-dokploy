package tech.calaverita.sfast_xpress.dashboard_gerencia.pojo;

import java.util.ArrayList;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.LiquidacionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.dynamic.PagoDynamicModel;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class LiquidacionesDashboardGerencia {
    private Double totalDeDescuento;
    private Double liquidaciones;

    public LiquidacionesDashboardGerencia(ArrayList<LiquidacionModel> liquidacionModels,
            ArrayList<PagoDynamicModel> pagoDynamicModels, ArrayList<PagoDynamicModel> liquidacionPagoModels) {
        this.totalDeDescuento = MyUtil
                .getDouble(liquidacionModels.stream().mapToDouble(LiquidacionModel::getDescuentoEnDinero).sum());
        this.liquidaciones = MyUtil.getDouble(getLiquidaciones(liquidacionPagoModels, pagoDynamicModels));
    }

    private Double getLiquidaciones(ArrayList<PagoDynamicModel> liquidacionPagoModels,
            ArrayList<PagoDynamicModel> pagoDynamicModels) {

        Double liquidaciones = 0D;

        for (PagoDynamicModel liquidacionPagoModel : liquidacionPagoModels) {
            for (PagoDynamicModel pagoDynamicModel : pagoDynamicModels) {
                if (liquidacionPagoModel.getPrestamoId().equals(pagoDynamicModel.getPrestamoId())) {
                    liquidaciones += liquidacionPagoModel.getMonto() - pagoDynamicModel.getTarifa();
                    break;
                }
            }
        }

        return liquidaciones;
    }
}

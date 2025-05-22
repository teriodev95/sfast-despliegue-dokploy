package tech.calaverita.sfast_xpress.f_by_f_dashboard_gerencia.pojo;

import java.util.List;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.VentaModel;

@Data
public class VentasDashboardGerencia {
    Integer numeroVentas;
    Double ventas;

    public VentasDashboardGerencia(List<VentaModel> ventaModels) {
        this.numeroVentas = ventaModels.size();
        this.ventas = ventaModels.stream().mapToDouble(VentaModel::getMonto).sum();
    }
}

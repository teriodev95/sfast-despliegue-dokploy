package tech.calaverita.sfast_xpress.DTOs.dashboard;

import java.util.ArrayList;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.dynamic.PagoDynamicModel;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class PagosDashboardDTO {
    private Integer clientesCobrados;
    private Integer noPagos;
    private Integer numeroLiquidaciones;
    private Integer pagosReducidos;
    private Double totalCobranzaPura;
    private Double montoExcedente;
    private Double multas;
    private Double cobranzaTotal;

    public PagosDashboardDTO(ArrayList<PagoDynamicModel> pagoDynamicModels, double liquidaciones) {
        this.clientesCobrados = (int) pagoDynamicModels.stream()
                .filter(pagoModel -> !pagoModel.getTipo().equals("Multa"))
                .count();
        this.noPagos = (int) pagoDynamicModels.stream().filter(pagoModel -> pagoModel.getTipo().equals("No_pago"))
                .count();
        this.numeroLiquidaciones = (int) pagoDynamicModels.stream()
                .filter(pagoModel -> pagoModel.getTipo().equals("Liquidacion")).count();
        this.pagosReducidos = (int) pagoDynamicModels.stream()
                .filter(pagoModel -> pagoModel.getTipo().equals("Reducido"))
                .count();
        this.cobranzaTotal = MyUtil
                .getDouble(pagoDynamicModels.stream().filter(pagoModel -> !pagoModel.getTipo().equals("Multa"))
                        .mapToDouble(PagoDynamicModel::getMonto).sum());
        this.montoExcedente = MyUtil.getDouble(pagoDynamicModels.stream()
                .filter(pagoModel -> pagoModel.getMonto() > pagoModel.getTarifa() && !pagoModel.getTipo().equals("Liquidacion"))
                .mapToDouble(pagoModel -> pagoModel.getMonto() - pagoModel.getTarifa()).sum());
        this.multas = MyUtil
                .getDouble(pagoDynamicModels.stream().filter(pagoModel -> pagoModel.getTipo().equals("Multa"))
                        .mapToDouble(PagoDynamicModel::getMonto)
                        .sum());
        this.totalCobranzaPura = MyUtil.getDouble(this.cobranzaTotal - this.montoExcedente - liquidaciones);
    }
}

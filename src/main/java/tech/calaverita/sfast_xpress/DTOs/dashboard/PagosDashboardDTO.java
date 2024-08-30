package tech.calaverita.sfast_xpress.DTOs.dashboard;

import java.util.ArrayList;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PagoAgrupadoModel;
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

    public PagosDashboardDTO(ArrayList<PagoAgrupadoModel> pagoAgrupadoModel) {
        this.clientesCobrados = (int) pagoAgrupadoModel.stream()
                .filter(pagoModel -> !pagoModel.getTipo().equals("Multa"))
                .count();
        this.noPagos = (int) pagoAgrupadoModel.stream().filter(pagoModel -> pagoModel.getTipo().equals("No_pago"))
                .count();
        this.numeroLiquidaciones = (int) pagoAgrupadoModel.stream()
                .filter(pagoModel -> pagoModel.getTipo().equals("Liquidacion")).count();
        this.pagosReducidos = (int) pagoAgrupadoModel.stream()
                .filter(pagoModel -> pagoModel.getTipo().equals("Reducido"))
                .count();
        this.cobranzaTotal = MyUtil
                .getDouble(pagoAgrupadoModel.stream().filter(pagoModel -> !pagoModel.getTipo().equals("Multa"))
                        .mapToDouble(PagoAgrupadoModel::getMonto).sum());
        this.montoExcedente = MyUtil.getDouble(pagoAgrupadoModel.stream()
                .filter(pagoModel -> pagoModel.getMonto() > pagoModel.getTarifa())
                .mapToDouble(pagoModel -> pagoModel.getMonto() - pagoModel.getTarifa()).sum());
        this.multas = MyUtil
                .getDouble(pagoAgrupadoModel.stream().filter(pagoModel -> pagoModel.getTipo().equals("Multa"))
                        .mapToDouble(PagoAgrupadoModel::getMonto)
                        .sum());
        this.totalCobranzaPura = MyUtil.getDouble(this.cobranzaTotal - this.montoExcedente);
    }
}

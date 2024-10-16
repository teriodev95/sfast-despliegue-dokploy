package tech.calaverita.sfast_xpress.DTOs.solicitud;

import lombok.Data;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class EgresosDTO {
    private Double pagoMensualRenta;
    private Double gastoMensualAlimentos;
    private Double pagoServicios;
    private Double gastoMensualTransporte;
    private Double pagoEstudios;
    private Double otrosGastos;
    private Double totalGastos;

    public EgresosDTO(String pagoMensualRenta, String gastoMensualAlimentos, String pagoServicios,
            String gastoMensualTransporte, String pagoEstudios, String otrosGastos, String totalGastos) {
        this.pagoMensualRenta = MyUtil.monetaryToDouble(pagoMensualRenta);
        this.gastoMensualAlimentos = MyUtil.monetaryToDouble(gastoMensualAlimentos);
        this.pagoServicios = MyUtil.monetaryToDouble(pagoServicios);
        this.gastoMensualTransporte = MyUtil.monetaryToDouble(gastoMensualTransporte);
        this.pagoEstudios = MyUtil.monetaryToDouble(pagoEstudios);
        this.otrosGastos = MyUtil.monetaryToDouble(otrosGastos);
        this.totalGastos = MyUtil.monetaryToDouble(totalGastos);
    }
}

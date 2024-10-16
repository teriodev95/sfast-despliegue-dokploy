package tech.calaverita.sfast_xpress.DTOs.solicitud;

import lombok.Data;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class EgresosDTO {
    private Object pagoMensualRenta;
    private Object gastoMensualAlimentos;
    private Object pagoServicios;
    private Object gastoMensualTransporte;
    private Object pagoEstudios;
    private Object otrosGastos;
    private Object totalGastos;

    public EgresosDTO(Object pagoMensualRenta, Object gastoMensualAlimentos, Object pagoServicios,
            Object gastoMensualTransporte, Object pagoEstudios, Object otrosGastos, Object totalGastos) {
        this.pagoMensualRenta = pagoMensualRenta;
        this.gastoMensualAlimentos = gastoMensualAlimentos;
        this.pagoServicios = pagoServicios;
        this.gastoMensualTransporte = gastoMensualTransporte;
        this.pagoEstudios = pagoEstudios;
        this.otrosGastos = otrosGastos;
        this.totalGastos = totalGastos;

        monetaryToDouble();
    }

    public void monetaryToDouble() {
        this.pagoMensualRenta = MyUtil.monetaryToDouble(this.pagoMensualRenta);
        this.gastoMensualAlimentos = MyUtil.monetaryToDouble(this.gastoMensualAlimentos);
        this.pagoServicios = MyUtil.monetaryToDouble(this.pagoServicios);
        this.gastoMensualTransporte = MyUtil.monetaryToDouble(this.gastoMensualTransporte);
        this.pagoEstudios = MyUtil.monetaryToDouble(this.pagoEstudios);
        this.otrosGastos = MyUtil.monetaryToDouble(this.otrosGastos);
        this.totalGastos = MyUtil.monetaryToDouble(this.totalGastos);
    }
}

package tech.calaverita.sfast_xpress.DTOs;

import lombok.Data;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class LiquidacionDTO {
    private String cliente;
    private String identificador;
    private String semEntrega;
    private Double entregado;
    private Double cargo;
    private Double montoTotal;
    private Double cobrado;
    private Double saldo;
    private Double descuentoDinero;
    private Integer descuentoPorcentaje;
    private Integer semanasTranscurridas;
    private Double liquidaCon;
    private String prestamoId;
    private Double saldoAlIniciarSemana;
    private Double excedente;
    private Double pagoSemanal;
    private Double tarifa;

    public LiquidacionDTO() {
        this.cliente = "";
        this.identificador = "";
        this.semEntrega = "";
        this.entregado = 0.0;
        this.cargo = 0.0;
        this.montoTotal = 0.0;
        this.cobrado = 0.0;
        this.saldo = 0.0;
        this.descuentoDinero = 0.0;
        this.descuentoPorcentaje = 0;
        this.semanasTranscurridas = 0;
        this.liquidaCon = 0.0;
        this.prestamoId = "";
        this.saldoAlIniciarSemana = 0.0;
        this.excedente = 0.0;
        this.pagoSemanal = 0.0;
        this.tarifa = 0.0;
    }

    public void formatToDoubles() {
        this.entregado = MyUtil.getDouble(this.entregado);
        this.cargo = MyUtil.getDouble(this.cargo);
        this.montoTotal = MyUtil.getDouble(this.montoTotal);
        this.cobrado = MyUtil.getDouble(this.cobrado);
        this.saldo = MyUtil.getDouble(this.saldo);
        this.descuentoDinero = MyUtil.getDouble(this.descuentoDinero);
        this.liquidaCon = MyUtil.getDouble(this.liquidaCon);
        this.saldoAlIniciarSemana = MyUtil.getDouble(this.saldoAlIniciarSemana);
        this.excedente = MyUtil.getDouble(this.excedente);
        this.pagoSemanal = MyUtil.getDouble(this.pagoSemanal);
        this.tarifa = MyUtil.getDouble(this.tarifa);
    }
}

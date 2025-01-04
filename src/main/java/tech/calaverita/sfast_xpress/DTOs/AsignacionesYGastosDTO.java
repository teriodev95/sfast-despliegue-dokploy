package tech.calaverita.sfast_xpress.DTOs;

import java.util.HashMap;

import lombok.Data;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class AsignacionesYGastosDTO {
    private Double balance;
    private Double totalIngresos;
    private Double totalEgresos;
    private Double ingresosAdministracion;
    private Double egresosAdministracion;
    private Double ingresosSeguridad;
    private Double egresosSeguridad;
    private Double ingresosOperacionGerentes;
    private Double egresosOperacionGerentes;
    private Double ingresosOperacionAgentes;
    private Double egresosVentas;
    private Double ingresosPrimerosPagos;
    private Double egresosPagoComisiones;
    private Double ingresosCierresAgencias;
    private Double egresosPagoBonos;
    private Double ingresosIncidentes;
    private Double egresosIncidentes;
    private Double ingresosReposiciones;
    private Double egresosReposiciones;
    private Double egresosGasolina;
    private Double egresosCasetas;
    private Double egresosMantenimientoAuto;
    private Double egresosTelefono;
    private HashMap<String, Object> desgloce;

    public AsignacionesYGastosDTO() {
        this.balance = 0D;
        this.totalIngresos = 0D;
        this.totalEgresos = 0D;
        this.ingresosAdministracion = 0D;
        this.egresosAdministracion = 0D;
        this.ingresosSeguridad = 0D;
        this.egresosSeguridad = 0D;
        this.ingresosOperacionGerentes = 0D;
        this.egresosOperacionGerentes = 0D;
        this.ingresosOperacionAgentes = 0D;
        this.egresosVentas = 0D;
        this.ingresosPrimerosPagos = 0D;
        this.egresosPagoComisiones = 0D;
        this.ingresosCierresAgencias = 0D;
        this.egresosPagoBonos = 0D;
        this.ingresosIncidentes = 0D;
        this.egresosIncidentes = 0D;
        this.ingresosReposiciones = 0D;
        this.egresosReposiciones = 0D;
        this.egresosGasolina = 0D;
        this.egresosCasetas = 0D;
        this.egresosMantenimientoAuto = 0D;
        this.egresosTelefono = 0D;
    }

    public void formatToDouble() {
        this.balance = MyUtil.getDouble(this.balance);
        this.totalIngresos = MyUtil.getDouble(this.totalIngresos);
        this.totalEgresos = MyUtil.getDouble(this.totalEgresos);
        this.ingresosAdministracion = MyUtil.getDouble(this.ingresosAdministracion);
        this.egresosAdministracion = MyUtil.getDouble(this.egresosAdministracion);
        this.ingresosSeguridad = MyUtil.getDouble(this.ingresosSeguridad);
        this.egresosSeguridad = MyUtil.getDouble(this.egresosSeguridad);
        this.ingresosOperacionGerentes = MyUtil.getDouble(this.ingresosOperacionGerentes);
        this.egresosOperacionGerentes = MyUtil.getDouble(this.egresosOperacionGerentes);
        this.ingresosOperacionAgentes = MyUtil.getDouble(this.ingresosOperacionAgentes);
        this.egresosVentas = MyUtil.getDouble(this.egresosVentas);
        this.ingresosPrimerosPagos = MyUtil.getDouble(this.ingresosPrimerosPagos);
        this.egresosPagoComisiones = MyUtil.getDouble(this.egresosPagoComisiones);
        this.ingresosCierresAgencias = MyUtil.getDouble(this.ingresosCierresAgencias);
        this.egresosPagoBonos = MyUtil.getDouble(this.egresosPagoBonos);
        this.ingresosIncidentes = MyUtil.getDouble(this.ingresosIncidentes);
        this.egresosIncidentes = MyUtil.getDouble(this.egresosIncidentes);
        this.ingresosReposiciones = MyUtil.getDouble(this.ingresosReposiciones);
        this.egresosReposiciones = MyUtil.getDouble(this.egresosReposiciones);
        this.egresosGasolina = MyUtil.getDouble(this.egresosGasolina);
        this.egresosCasetas = MyUtil.getDouble(this.egresosCasetas);
        this.egresosMantenimientoAuto = MyUtil.getDouble(this.egresosMantenimientoAuto);
        this.egresosTelefono = MyUtil.getDouble(this.egresosTelefono);
    }

    public void sumaIngresos() {
        this.totalIngresos = this.ingresosAdministracion + this.ingresosSeguridad + this.ingresosOperacionGerentes
                + this.ingresosOperacionAgentes + this.ingresosPrimerosPagos + this.ingresosCierresAgencias
                + this.ingresosIncidentes + this.ingresosReposiciones;
    }

    public void sumaEgresos() {
        this.totalEgresos = this.egresosAdministracion + this.egresosSeguridad + this.egresosOperacionGerentes
                + this.egresosVentas + this.egresosPagoComisiones + this.egresosPagoBonos
                + this.egresosIncidentes + this.egresosReposiciones + this.egresosGasolina + this.egresosCasetas
                + this.egresosMantenimientoAuto + this.egresosTelefono;
    }

    public void setBalance() {
        this.balance = this.totalIngresos - this.totalEgresos;
    }
}

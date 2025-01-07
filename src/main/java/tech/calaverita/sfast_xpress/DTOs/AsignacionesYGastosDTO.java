package tech.calaverita.sfast_xpress.DTOs;

import java.util.ArrayList;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.ResumenYBalanceModel;

@Data
public class AsignacionesYGastosDTO {
    // private Double balance;
    // private Double totalIngresos;
    // private Double totalEgresos;
    // private Double ingresosAdministracion;
    // private Double egresosAdministracion;
    // private Double ingresosSeguridad;
    // private Double egresosSeguridad;
    // private Double ingresosOperacionGerentes;
    // private Double egresosOperacionGerentes;
    // private Double ingresosOperacionAgentes;
    // private Double egresosVentas;
    // private Double ingresosPorPrimerosPagos;
    // private Double egresosPagoComisiones;
    // private Double ingresosCierresAgencias;
    // private Double egresosPagoBonos;
    // private Double ingresosIncidentes;
    // private Double egresosIncidentes;
    // private Double ingresosReposiciones;
    // private Double egresosReposiciones;
    // private Double egresosGasolina;
    // private Double egresosCasetas;
    // private Double egresosMantenimientoAuto;
    // private Double egresosTelefono;
    // private HashMap<String, Object> desgloce;

    private Double ingresosPorAsignaciones;
    private Double ingresosPorPrimerosPagos;
    private Double ingresosPorCierres;
    private Double ingresosPorIncidentesYReposiciones;
    private Double egresosPorAsignaciones;
    private Double egresosPorVentas;
    private Double egresosPorComisionDeCobranza;
    private Double egresosPorComisionDeVentas;
    private Double egresosPorGastos;
    private Double egresosPorIncidentesYReposiciones;
    private Double totalIngresos;
    private Double totalEgresos;
    private Double balance;

    public AsignacionesYGastosDTO() {

    }

    public AsignacionesYGastosDTO(ArrayList<ResumenYBalanceModel> resumenYBalanceModels) {
        this.setIngresosPorAsignaciones(
                resumenYBalanceModels.get(0).getMonto() != null ? resumenYBalanceModels.get(0).getMonto() : 0D);
        this.setIngresosPorPrimerosPagos(
                resumenYBalanceModels.get(1).getMonto() != null ? resumenYBalanceModels.get(1).getMonto() : 0D);
        this.setIngresosPorCierres(
                resumenYBalanceModels.get(2).getMonto() != null ? resumenYBalanceModels.get(2).getMonto() : 0D);
        this.setIngresosPorIncidentesYReposiciones(
                resumenYBalanceModels.get(3).getMonto() != null ? resumenYBalanceModels.get(3).getMonto() : 0D);
        this.setEgresosPorAsignaciones(
                resumenYBalanceModels.get(4).getMonto() != null ? resumenYBalanceModels.get(4).getMonto() : 0D);
        this.setEgresosPorVentas(
                resumenYBalanceModels.get(5).getMonto() != null ? resumenYBalanceModels.get(5).getMonto() : 0D);
        this.setEgresosPorComisionDeCobranza(
                resumenYBalanceModels.get(6).getMonto() != null ? resumenYBalanceModels.get(6).getMonto() : 0D);
        this.setEgresosPorComisionDeVentas(
                resumenYBalanceModels.get(7).getMonto() != null ? resumenYBalanceModels.get(7).getMonto() : 0D);
        this.setEgresosPorGastos(
                resumenYBalanceModels.get(8).getMonto() != null ? resumenYBalanceModels.get(8).getMonto() : 0D);
        this.setEgresosPorIncidentesYReposiciones(
                resumenYBalanceModels.get(9).getMonto() != null ? resumenYBalanceModels.get(9).getMonto() : 0D);
        this.setTotalIngresos(
                resumenYBalanceModels.get(10).getMonto() != null ? resumenYBalanceModels.get(10).getMonto() : 0D);
        this.setTotalEgresos(
                resumenYBalanceModels.get(11).getMonto() != null ? resumenYBalanceModels.get(11).getMonto() : 0D);
        this.setBalance(
                resumenYBalanceModels.get(12).getMonto() != null ? resumenYBalanceModels.get(12).getMonto() : 0D);
    }
}

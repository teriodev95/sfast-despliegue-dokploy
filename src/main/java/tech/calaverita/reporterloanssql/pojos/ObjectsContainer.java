package tech.calaverita.reporterloanssql.pojos;

import lombok.Data;
import tech.calaverita.reporterloanssql.persistence.entities.AsignacionEntity;
import tech.calaverita.reporterloanssql.persistence.entities.CalendarioEntity;
import tech.calaverita.reporterloanssql.persistence.entities.LiquidacionEntity;
import tech.calaverita.reporterloanssql.persistence.entities.PagoEntity;
import tech.calaverita.reporterloanssql.persistence.entities.view.PagoUtilEntity;
import tech.calaverita.reporterloanssql.persistence.entities.view.PrestamoEntity;
import tech.calaverita.reporterloanssql.persistence.entities.view.PrestamoUtilEntity;

import java.util.ArrayList;

@Data
public class ObjectsContainer {
    private String fechaPago;
    private String dia;
    private CalendarioEntity calendarioEntity;
    private Cobranza cobranza;
    private Dashboard dashboard;
    private ArrayList<PrestamoEntity> prestamosToCobranza;
    private ArrayList<PagoUtilEntity> pagosVistaToCobranza;
    private ArrayList<PrestamoUtilEntity> prestamosToDashboard;
    private ArrayList<PagoUtilEntity> pagosVistaToDashboard;
    private ArrayList<PagoEntity> pagEntPagoModelsToDashboard;
    private ArrayList<PagoEntity> pagos;
    private ArrayList<Cobranza> cobranzas;
    private ArrayList<Dashboard> dashboards;
    private ArrayList<String> agencias;
    private ArrayList<LiquidacionEntity> liquidaciones;
    private ArrayList<PagoEntity> pagosOfLiquidaciones;
    private ArrayList<AsignacionEntity> asignaciones;
}

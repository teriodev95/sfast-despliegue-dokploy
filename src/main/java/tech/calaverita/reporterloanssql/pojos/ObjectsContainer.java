package tech.calaverita.reporterloanssql.pojos;

import lombok.Data;
import tech.calaverita.reporterloanssql.models.*;

import java.util.ArrayList;

@Data
public class ObjectsContainer {
    private String fechaPago;
    private String dia;
    private CalendarioModel calendarioModel;
    private Cobranza cobranza;
    private Dashboard dashboard;
    private ArrayList<PrestamoModel> prestamosToCobranza;
    private ArrayList<PagoUtilModel> pagosVistaToCobranza;
    private ArrayList<PrestamoUtilModel> prestamosToDashboard;
    private ArrayList<PagoUtilModel> pagosVistaToDashboard;
    private ArrayList<PagoModel> pagoModelsToDashboard;
    private ArrayList<PagoModel> pagos;
    private ArrayList<Cobranza> cobranzas;
    private ArrayList<Dashboard> dashboards;
    private ArrayList<String> agencias;
    private ArrayList<LiquidacionModel> liquidaciones;
    private ArrayList<PagoModel> pagosOfLiquidaciones;
    private ArrayList<AsignacionModel> asignaciones;
}

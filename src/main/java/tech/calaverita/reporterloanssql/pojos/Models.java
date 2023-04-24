package tech.calaverita.reporterloanssql.pojos;

import lombok.Data;
import tech.calaverita.reporterloanssql.models.*;

import java.util.ArrayList;

@Data
public class Models {
    private Cobranza cobranza;
    private Dashboard dashboard;
    private ArrayList<PrestamoModel> prestamosToCobranza;
    private ArrayList<PagoVistaModel> pagosVistaToCobranza;
    private ArrayList<PrestamoModel> prestamosToDashboard;
    private ArrayList<PagoVistaModel> pagosVistaToDashboard;
    private ArrayList<PagoModel> pagos;
    private ArrayList<Cobranza> cobranzas;
    private ArrayList<Dashboard> dashboards;
    private ArrayList<String> agencias;
    private ArrayList<LiquidacionModel> liquidaciones;
    private ArrayList<PagoModel> pagosOfLiquidaciones;
    private ArrayList<AsignacionModel> asignaciones;
}

package tech.calaverita.sfast_xpress.pojos;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.AsignacionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.CalendarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.LiquidacionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.PagoModel;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PagoUtilModel;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoModel;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoUtilModel;

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
    private ArrayList<PagoModel> pagEntPagoModelsToDashboard;
    private ArrayList<PagoModel> pagos;
    private ArrayList<Cobranza> cobranzas;
    private ArrayList<Dashboard> dashboards;
    private ArrayList<String> agencias;
    private ArrayList<LiquidacionModel> liquidaciones;
    private ArrayList<PagoModel> pagosOfLiquidaciones;
    private ArrayList<AsignacionModel> asignaciones;
}

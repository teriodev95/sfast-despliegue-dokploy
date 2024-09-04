package tech.calaverita.sfast_xpress.pojos;

import java.util.ArrayList;

import lombok.Data;
import tech.calaverita.sfast_xpress.DTOs.cobranza.CobranzaDTO;
import tech.calaverita.sfast_xpress.DTOs.dashboard.DashboardDTO;
import tech.calaverita.sfast_xpress.models.mariaDB.AsignacionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.CalendarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.LiquidacionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.PagoModel;
import tech.calaverita.sfast_xpress.models.mariaDB.dynamic.PagoDynamicModel;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoViewModel;

@Data
public class ObjectsContainer {

    private String fechaPago;
    private String dia;
    private CalendarioModel calendarioModel;
    private CobranzaDTO cobranza;
    private DashboardDTO dashboard;
    private ArrayList<PrestamoViewModel> prestamoViewModelsCobranza;
    private ArrayList<PagoDynamicModel> pagoDynamicViewModelsCobranza;
    private ArrayList<PrestamoViewModel> prestamoViewModelsDashboard;
    private ArrayList<PagoDynamicModel> pagoDynamicModelsDashboard;
    private ArrayList<PagoModel> pagEntPagoModelsToDashboard;
    private ArrayList<PagoModel> pagos;
    private ArrayList<CobranzaDTO> cobranzas;
    private ArrayList<DashboardDTO> dashboards;
    private ArrayList<String> agencias;
    private ArrayList<LiquidacionModel> liquidaciones;
    private ArrayList<PagoModel> pagosOfLiquidaciones;
    private ArrayList<AsignacionModel> asignaciones;
}

package tech.calaverita.sfast_xpress.threads.pwa;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.calaverita.sfast_xpress.models.mariaDB.AgenciaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.CalendarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.VentaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.dynamic.PagoDynamicModel;
import tech.calaverita.sfast_xpress.pojos.ObjectsContainer;
import tech.calaverita.sfast_xpress.services.AgenciaService;
import tech.calaverita.sfast_xpress.services.AsignacionService;
import tech.calaverita.sfast_xpress.services.CalendarioService;
import tech.calaverita.sfast_xpress.services.LiquidacionService;
import tech.calaverita.sfast_xpress.services.PagoService;
import tech.calaverita.sfast_xpress.services.UsuarioService;
import tech.calaverita.sfast_xpress.services.VentaService;
import tech.calaverita.sfast_xpress.services.cierre_semanal.CierreSemanalService;
import tech.calaverita.sfast_xpress.services.dynamic.PagoDynamicService;
import tech.calaverita.sfast_xpress.services.views.PrestamoViewService;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Component
public class DashboardPorDiaYHoraPWAThread implements Runnable {

    private ObjectsContainer objectsContainer;
    private int opc;
    private Thread[] threads;
    private static AsignacionService asignacionService;
    private static CalendarioService calendarioService;
    private static LiquidacionService liquidacionService;
    private static PagoService pagoService;
    private static PrestamoViewService prestamoViewService;
    private static PagoDynamicService pagoDynamicService;
    private static CierreSemanalService cierreSemanalService;
    private static AgenciaService agenciaService;
    private static UsuarioService usuarioService;
    private static VentaService ventaService;

    @Autowired
    private DashboardPorDiaYHoraPWAThread(
            AsignacionService asignacionService,
            CalendarioService calendarioService,
            LiquidacionService liquidacionService,
            PagoService pagoService,
            PrestamoViewService prestamoViewService,
            PagoDynamicService pagoDynamicService,
            CierreSemanalService cierreSemanalService,
            AgenciaService agenciaService, UsuarioService usuarioService, VentaService ventaService) {
        DashboardPorDiaYHoraPWAThread.asignacionService = asignacionService;
        DashboardPorDiaYHoraPWAThread.calendarioService = calendarioService;
        DashboardPorDiaYHoraPWAThread.liquidacionService = liquidacionService;
        DashboardPorDiaYHoraPWAThread.pagoService = pagoService;
        DashboardPorDiaYHoraPWAThread.prestamoViewService = prestamoViewService;
        DashboardPorDiaYHoraPWAThread.pagoDynamicService = pagoDynamicService;
        DashboardPorDiaYHoraPWAThread.cierreSemanalService = cierreSemanalService;
        DashboardPorDiaYHoraPWAThread.agenciaService = agenciaService;
        DashboardPorDiaYHoraPWAThread.usuarioService = usuarioService;
        DashboardPorDiaYHoraPWAThread.ventaService = ventaService;
    }

    public DashboardPorDiaYHoraPWAThread(
            ObjectsContainer objectsContainer,
            int opc,
            Thread[] threads) {
        this.objectsContainer = objectsContainer;
        this.opc = opc;
        this.threads = threads;
    }

    @Override
    public void run() {
        switch (opc) {
            case 0 ->
                setPrestamosToCobranza();
            case 1 ->
                setPrestamosToDashboard();
            case 2 ->
                setPagosToCobranza();
            case 3 ->
                setPagosToDashboard();
            case 4 ->
                setLiquidacionesBd();
            case 5 ->
                setCalendario();
            case 6 ->
                setAsignaciones();
        }
    }

    public void setCalendario() {
        objectsContainer.setCalendarioModel(
                DashboardPorDiaYHoraPWAThread.calendarioService.findByFechaActual(objectsContainer.getFechaPago()));
        objectsContainer.getDashboard().setAnio(objectsContainer.getCalendarioModel().getAnio());
        objectsContainer.getDashboard().setSemana(objectsContainer.getCalendarioModel().getSemana());
        setIsCerrada();
    }

    public void setPrestamosToCobranza() {
        try {
            threads[5].join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // To easy code
        String gerencia = this.objectsContainer.getGerenciaModel().getDeprecatedName();
        String sucursal = this.objectsContainer.getGerenciaModel().getSucursal();

        objectsContainer.setPrestamoViewModelsCobranza(DashboardPorDiaYHoraPWAThread.prestamoViewService
                .findByGerenciaSucursalAndSaldoAlIniciarSemanaGreaterThan(gerencia, sucursal, 0D).join());

        try {
            threads[2].join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        setClientes();
    }

    public void setPrestamosToDashboard() {
        try {
            threads[5].join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // To easy code
        String gerencia = this.objectsContainer.getGerenciaModel().getDeprecatedName();
        String sucursal = this.objectsContainer.getGerenciaModel().getSucursal();

        objectsContainer.setPrestamoViewModelsDashboard(DashboardPorDiaYHoraPWAThread.prestamoViewService
                .darrprestUtilEntByGerenciaSucursalAndFechaPagoLessThanEqualToDashboard(
                        gerencia, sucursal,
                        objectsContainer.getFechaPago(),
                        objectsContainer.getDashboard().getAnio(), objectsContainer.getDashboard().getSemana()));
    }

    public void setPagosToCobranza() {
        try {
            threads[5].join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // To easy code
        String agencia = this.objectsContainer.getDashboard().getAgencia();
        int anio = this.objectsContainer.getDashboard().getAnio();
        int semana = this.objectsContainer.getDashboard().getSemana();

        CalendarioModel calendarioModel = new CalendarioModel();
        calendarioModel.setAnio(anio);
        calendarioModel.setSemana(semana);
        MyUtil.funSemanaAnterior(calendarioModel);

        double cierraConGreaterThan = 0;
        objectsContainer.setPagoDynamicViewModelsCobranza(DashboardPorDiaYHoraPWAThread.pagoDynamicService
                .findByAgenciaInAnioSemanaAndCierraConGreaterThan(
                        objectsContainer.getAgencias().toArray(new String[objectsContainer.getAgencias().size()]),
                        calendarioModel.getAnio(),
                        calendarioModel.getSemana(), cierraConGreaterThan));
    }

    public void setPagosToDashboard() {
        try {
            threads[5].join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        String gerencia = this.objectsContainer.getGerenciaModel().getDeprecatedName();
        String sucursal = this.objectsContainer.getGerenciaModel().getSucursal();

        objectsContainer.setPagoDynamicModelsDashboard(DashboardPorDiaYHoraPWAThread.pagoService
                .findByGerenciaAndFechaPagoLessThanEqualAndEsPrimerPagoInnerJoinPagoModel(
                        gerencia, sucursal,
                        objectsContainer.getFechaPago(), false, this.objectsContainer.getDashboard().getAnio(),
                        this.objectsContainer.getDashboard().getSemana()));

        try {
            threads[0].join();
            threads[1].join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        setClientesCobrados();
        setMultas();
        setNoPagos();
        setPagosReducidos();
        setCobranzaTotal();

        try {
            threads[4].join();
        } catch (InterruptedException e) {
            System.out.println("Ha fallado el setMontoExcedente " + e);
        }

        setMontoExcedente();

        try {
            threads[4].join();
        } catch (InterruptedException e) {
            System.out.println("Ha fallado el setTotalCobranzaPura " + e);
        }

        setTotalCobranzaPura();

        try {
            threads[2].join();
        } catch (InterruptedException e) {
            System.out.println("Ha fallado el setMontoDeDebitoFaltante " + e);
        }

        setMontoDeDebitoFaltante();

        try {
            threads[2].join();
        } catch (InterruptedException e) {
            System.out.println("Ha fallado el setRendimiento " + e);
        }

        setRendmiento();
    }

    public void setLiquidacionesBd() {
        objectsContainer.setLiquidaciones(DashboardPorDiaYHoraPWAThread.liquidacionService.findByAgenciaInAndFechaPago(
                objectsContainer.getAgencias().toArray(new String[objectsContainer.getAgencias().size()]),
                objectsContainer.getFechaPago()));
        objectsContainer.setPagosOfLiquidaciones(
                DashboardPorDiaYHoraPWAThread.pagoService.findByAgenteAndFechaPagoInnerJoinLiquidacionModel(
                        objectsContainer.getDashboard().getAgencia(), objectsContainer.getFechaPago()));

        setNumeroDeLiquidaciones();
        setTotalDeDescuento();

        try {
            threads[5].join();
        } catch (InterruptedException e) {
            System.out.println("Ha fallado el setLiquidaciones " + e);
        }

        setLiquidaciones();
    }

    public void setAsignaciones() {
        try {
            threads[5].join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        ArrayList<Integer> usuarioIds = new ArrayList<>();
        for (String agencia : objectsContainer.getAgencias()) {
            usuarioIds
                    .add(DashboardPorDiaYHoraPWAThread.usuarioService.findByAgenciaTipoAndStatus(
                            agencia, "Agente",
                            true) == null ? 0
                                    : DashboardPorDiaYHoraPWAThread.usuarioService
                                            .findByAgenciaTipoAndStatus(agencia, "Agente", true).getUsuarioId());
        }

        objectsContainer.setAsignaciones(
                DashboardPorDiaYHoraPWAThread.asignacionService.findByQuienEntregoUsuarioIdInAnioAndSemana(
                        usuarioIds.toArray(new Integer[usuarioIds.size()]),
                        objectsContainer.getDashboard().getAnio(),
                        objectsContainer.getDashboard().getSemana()));

        setVentasYNumeroVentas();
    }

    public void setGerencia() {
        objectsContainer.getDashboard()
                .setGerencia(objectsContainer.getPrestamoViewModelsCobranza().get(0).getGerencia());
    }

    public void setClientes() {
        objectsContainer.getDashboard().setClientes(getClientesTotales());
        getClientesMiercoles();
        getClientesJueves();
        getClientesViernes();
    }

    public void setClientesCobrados() {
        objectsContainer.getDashboard().setClientesCobrados(objectsContainer.getPrestamoViewModelsDashboard().size());
    }

    public void setNumeroDeLiquidaciones() {
        if (objectsContainer.getLiquidaciones().isEmpty()) {
            objectsContainer.getDashboard().setNumeroLiquidaciones(0);
        } else {
            objectsContainer.getDashboard().setNumeroLiquidaciones(objectsContainer.getLiquidaciones().size());
        }
    }

    public void setMultas() {
        objectsContainer.getDashboard().setMultas(0.0);
    }

    public void setNoPagos() {
        int noPagos = 0;

        for (int i = 0; i < objectsContainer.getPagoDynamicModelsDashboard().size(); i++) {
            if (objectsContainer.getPagoDynamicModelsDashboard().get(i).getMonto() == 0) {
                noPagos++;
            }
        }

        objectsContainer.getDashboard().setNoPagos(noPagos);
    }

    public void setPagosReducidos() {
        int pagosReducidos = 0;

        for (int i = 0; i < objectsContainer.getPagoDynamicModelsDashboard().size(); i++) {
            if (objectsContainer.getPagoDynamicModelsDashboard().get(i).getAbreCon() < objectsContainer
                    .getPagoDynamicModelsDashboard().get(i).getTarifa()) {
                if (objectsContainer.getPagoDynamicModelsDashboard().get(i).getMonto() < objectsContainer
                        .getPagoDynamicModelsDashboard().get(i).getAbreCon()) {
                    pagosReducidos++;
                }
            } else {
                if (objectsContainer.getPagoDynamicModelsDashboard().get(i).getMonto() < objectsContainer
                        .getPagoDynamicModelsDashboard().get(i).getTarifa()) {
                    pagosReducidos++;
                }
            }
        }

        objectsContainer.getDashboard().setPagosReducidos(pagosReducidos);
    }

    public int getClientesMiercoles() {
        int clientesMiercoles = 0;
        double debitoMiercoles = 0;

        for (int i = 0; i < objectsContainer.getPrestamoViewModelsCobranza().size(); i++) {
            if (objectsContainer.getPrestamoViewModelsCobranza().get(i).getDiaDePago().equals("MIERCOLES")) {
                clientesMiercoles += 1;
                if (objectsContainer.getPrestamoViewModelsCobranza().get(i).getSaldoAlIniciarSemana() < objectsContainer
                        .getPrestamoViewModelsCobranza().get(i).getTarifa()) {
                    debitoMiercoles += objectsContainer.getPrestamoViewModelsCobranza().get(i)
                            .getSaldoAlIniciarSemana();
                } else {
                    debitoMiercoles += objectsContainer.getPrestamoViewModelsCobranza().get(i).getTarifa();
                }
            }
        }

        objectsContainer.getDashboard().setDebitoMiercoles(MyUtil.getDouble(debitoMiercoles));

        return clientesMiercoles;
    }

    public int getClientesJueves() {
        int clientesJueves = 0;
        double debitoJueves = 0;

        for (int i = 0; i < objectsContainer.getPrestamoViewModelsCobranza().size(); i++) {
            if (objectsContainer.getPrestamoViewModelsCobranza().get(i).getDiaDePago().equals("JUEVES")) {
                clientesJueves += 1;
                if (objectsContainer.getPrestamoViewModelsCobranza().get(i).getSaldoAlIniciarSemana() < objectsContainer
                        .getPrestamoViewModelsCobranza().get(i).getTarifa()) {
                    debitoJueves += objectsContainer.getPrestamoViewModelsCobranza().get(i).getSaldoAlIniciarSemana();
                } else {
                    debitoJueves += objectsContainer.getPrestamoViewModelsCobranza().get(i).getTarifa();
                }
            }
        }

        objectsContainer.getDashboard().setDebitoJueves(MyUtil.getDouble(debitoJueves));

        return clientesJueves;
    }

    public int getClientesViernes() {
        int clientesViernes = 0;
        double debitoViernes = 0;

        for (int i = 0; i < objectsContainer.getPrestamoViewModelsCobranza().size(); i++) {
            if (objectsContainer.getPrestamoViewModelsCobranza().get(i).getDiaDePago().equals("VIERNES")) {
                clientesViernes += 1;
                if (objectsContainer.getPrestamoViewModelsCobranza().get(i).getSaldoAlIniciarSemana() < objectsContainer
                        .getPrestamoViewModelsCobranza().get(i).getTarifa()) {
                    debitoViernes += objectsContainer.getPrestamoViewModelsCobranza().get(i).getSaldoAlIniciarSemana();
                } else {
                    debitoViernes += objectsContainer.getPrestamoViewModelsCobranza().get(i).getTarifa();
                }
            }
        }

        objectsContainer.getDashboard().setDebitoViernes(MyUtil.getDouble(debitoViernes));

        return clientesViernes;
    }

    public int getClientesTotales() {
        int clientesTotales = objectsContainer.getPrestamoViewModelsCobranza().size();
        double debitoTotal = 0;

        for (int i = 0; i < objectsContainer.getPrestamoViewModelsCobranza().size(); i++) {
            if (objectsContainer.getPrestamoViewModelsCobranza().get(i).getSaldoAlIniciarSemana() < objectsContainer
                    .getPrestamoViewModelsCobranza().get(i).getTarifa()) {
                debitoTotal += objectsContainer.getPrestamoViewModelsCobranza().get(i).getSaldoAlIniciarSemana();
            } else {
                debitoTotal += objectsContainer.getPrestamoViewModelsCobranza().get(i).getTarifa();
            }
        }

        objectsContainer.getDashboard().setDebitoTotal(MyUtil.getDouble(debitoTotal));

        return clientesTotales;
    }

    public void setTotalDeDescuento() {
        double totalDeDescuento = 0;

        if (objectsContainer.getLiquidaciones() != null) {
            if (!objectsContainer.getLiquidaciones().isEmpty()) {
                for (int i = 0; i < objectsContainer.getLiquidaciones().size(); i++) {
                    totalDeDescuento += objectsContainer.getLiquidaciones().get(i).getDescuentoEnDinero();
                }
            }
        }

        objectsContainer.getDashboard().setTotalDeDescuento(totalDeDescuento);
    }

    public void setMontoExcedente() {
        double montoExcedente = 0;
        PagoDynamicModel pagoDynamicModel;
        for (int i = 0; i < objectsContainer.getPagoDynamicModelsDashboard().size(); i++) {
            // To easy code
            pagoDynamicModel = objectsContainer.getPagoDynamicModelsDashboard().get(i);

            if (pagoDynamicModel.getTipo().equals("Excedente")) {
                montoExcedente += pagoDynamicModel.getMonto() - pagoDynamicModel.getTarifa();
            }
        }

        objectsContainer.getDashboard().setMontoExcedente(MyUtil.getDouble(montoExcedente));
    }

    public void setLiquidaciones() {
        double liquidaciones = 0;

        if (objectsContainer.getLiquidaciones() != null) {
            if (!objectsContainer.getLiquidaciones().isEmpty()) {
                for (int i = 0; i < objectsContainer.getLiquidaciones().size(); i++) {
                    liquidaciones += objectsContainer.getLiquidaciones().get(i).getLiquidoCon()
                            - objectsContainer.getPagosOfLiquidaciones().get(i).getTarifa();
                }
            }
        }

        objectsContainer.getDashboard().setLiquidaciones(liquidaciones);
    }

    public void setCobranzaTotal() {
        double cobranzaTotal = 0;

        for (int i = 0; i < objectsContainer.getPagoDynamicModelsDashboard().size(); i++) {
            cobranzaTotal += objectsContainer.getPagoDynamicModelsDashboard().get(i).getMonto();
        }

        objectsContainer.getDashboard().setCobranzaTotal(MyUtil.getDouble(cobranzaTotal));
    }

    public void setTotalCobranzaPura() {
        double totalCobranzaPura = objectsContainer.getDashboard().getCobranzaTotal()
                - objectsContainer.getDashboard().getMontoExcedente();

        if (objectsContainer.getDashboard().getLiquidaciones() != null) {
            if (objectsContainer.getDashboard().getLiquidaciones() > 0) {
                totalCobranzaPura -= objectsContainer.getDashboard().getLiquidaciones();
            }
        }

        objectsContainer.getDashboard().setTotalCobranzaPura(MyUtil.getDouble(totalCobranzaPura));
    }

    public void setMontoDeDebitoFaltante() {
        // To easy code
        double debitoTotal = objectsContainer.getDashboard().getDebitoTotal();
        double cobranzaPura = objectsContainer.getDashboard().getTotalCobranzaPura();

        objectsContainer.getDashboard()
                .setMontoDeDebitoFaltante(MyUtil.getDouble(debitoTotal - cobranzaPura));
    }

    public void setRendmiento() {
        if (objectsContainer.getDashboard().getDebitoTotal() != null
                && objectsContainer.getDashboard().getDebitoTotal() > 0) {
            double rendimiento = objectsContainer.getDashboard().getTotalCobranzaPura()
                    / objectsContainer.getDashboard().getDebitoTotal() * 100;

            objectsContainer.getDashboard().setRendimiento((Math.round(rendimiento * 100.0) / 100.0));
        }
    }

    public void setIsCerrada() {
        AgenciaModel agenciaModel = DashboardPorDiaYHoraPWAThread.agenciaService
                .findById(objectsContainer.getDashboard().getAgencia());

        // To easy code
        String cierreId = agenciaModel.getGerenciaId() + "-" + objectsContainer.getDashboard().getAgencia()
                + "-" + objectsContainer.getDashboard().getAnio() + "-" + objectsContainer.getDashboard().getSemana();
        Boolean status_agencia = DashboardPorDiaYHoraPWAThread.cierreSemanalService.existsById(cierreId);

        System.out.println("CierreId: " + cierreId);

        objectsContainer.getDashboard().setIsCerrada(status_agencia);
    }

    public void setVentasYNumeroVentas() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd+HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(objectsContainer.getFechaPago(), formato);
        String createdAt = localDateTime.plusHours(6).format(formato);

        // To easy code
        int anio = objectsContainer.getCalendarioModel().getAnio();
        int semana = objectsContainer.getCalendarioModel().getSemana();
        String gerencia = objectsContainer.getDashboard().getGerencia();

        ArrayList<VentaModel> ventaModels = DashboardPorDiaYHoraPWAThread.ventaService
                .findByGerenciaCreatedAtLessThanEqualAnioAndSemana(gerencia, createdAt, anio, semana);

        objectsContainer.getDashboard().setNumeroVentas(ventaModels.size());
        objectsContainer.getDashboard()
                .setVentas(ventaModels.stream().mapToDouble(ventaModel -> ventaModel.getMonto()).sum());
    }
}

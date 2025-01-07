package tech.calaverita.sfast_xpress.threads.pwa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.calaverita.sfast_xpress.models.mariaDB.CalendarioModel;
import tech.calaverita.sfast_xpress.pojos.ObjectsContainer;
import tech.calaverita.sfast_xpress.services.AsignacionService;
import tech.calaverita.sfast_xpress.services.CalendarioService;
import tech.calaverita.sfast_xpress.services.LiquidacionService;
import tech.calaverita.sfast_xpress.services.PagoService;
import tech.calaverita.sfast_xpress.services.dynamic.PagoDynamicService;
import tech.calaverita.sfast_xpress.services.views.PrestamoViewService;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Component
public class DashboardPorDiaPWAThread implements Runnable {

    private ObjectsContainer objectsContainer;
    private int opc;
    private Thread[] threads;
    private static AsignacionService asignacionService;
    private static CalendarioService calendarioService;
    private static LiquidacionService liquidacionService;
    private static PagoService pagoService;
    private static PrestamoViewService prestamoViewService;
    private static PagoDynamicService pagoDynamicService;

    @Autowired
    private DashboardPorDiaPWAThread(
            AsignacionService asignacionService,
            CalendarioService calendarioService,
            LiquidacionService liquidacionService,
            PagoService pagoService,
            PrestamoViewService prestamoViewService,
            PagoDynamicService pagoDynamicService) {
        DashboardPorDiaPWAThread.asignacionService = asignacionService;
        DashboardPorDiaPWAThread.calendarioService = calendarioService;
        DashboardPorDiaPWAThread.liquidacionService = liquidacionService;
        DashboardPorDiaPWAThread.pagoService = pagoService;
        DashboardPorDiaPWAThread.prestamoViewService = prestamoViewService;
        DashboardPorDiaPWAThread.pagoDynamicService = pagoDynamicService;
    }

    public DashboardPorDiaPWAThread(
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
                DashboardPorDiaPWAThread.calendarioService.findByFechaActual(objectsContainer.getFechaPago()));
        objectsContainer.getDashboard().setAnio(objectsContainer.getCalendarioModel().getAnio());
        objectsContainer.getDashboard().setSemana(objectsContainer.getCalendarioModel().getSemana());
    }

    public void setPrestamosToCobranza() {
        try {
            threads[5].join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        String agencia = this.objectsContainer.getDashboard().getAgencia();

        // To easy code
        int anio = objectsContainer.getDashboard().getAnio();
        int semana = objectsContainer.getDashboard().getSemana();

        objectsContainer.setPrestamoViewModelsCobranza(DashboardPorDiaPWAThread.prestamoViewService
                .findByAgenciaSaldoAlIniciarSemanaGreaterThanAndNotAnioAndSemana(agencia, 0D, anio, semana).join());

        setGerencia();
        setClientes();
    }

    public void setPrestamosToDashboard() {
        objectsContainer.setPrestamoViewModelsDashboard(DashboardPorDiaPWAThread.prestamoViewService
                .darrprestUtilModByAgenciaAndFechaPagoToDashboard(objectsContainer.getDashboard().getAgencia(),
                        objectsContainer.getFechaPago()));
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
        objectsContainer.setPagoDynamicViewModelsCobranza(DashboardPorDiaPWAThread.pagoDynamicService
                .findByAgenciaAnioSemanaAndCierraConGreaterThan(agencia, calendarioModel.getAnio(),
                        calendarioModel.getSemana(), cierraConGreaterThan));
    }

    public void setPagosToDashboard() {
        objectsContainer.setPagEntPagoModelsToDashboard(DashboardPorDiaPWAThread.pagoService
                .findByAgenteFechaPagoAndEsPrimerPagoInnerJoinPagoModel(objectsContainer.getDashboard().getAgencia(),
                        objectsContainer.getFechaPago(), false));

        try {
            threads[0].join();
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
        objectsContainer.setLiquidaciones(DashboardPorDiaPWAThread.liquidacionService.findByAgenciaAndFechaPago(
                objectsContainer.getDashboard().getAgencia(), objectsContainer.getFechaPago()));
        objectsContainer.setPagosOfLiquidaciones(
                DashboardPorDiaPWAThread.pagoService.findByAgenteAndFechaPagoInnerJoinLiquidacionModel(
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

        objectsContainer.setAsignaciones(DashboardPorDiaPWAThread.asignacionService.findByAgenciaAnioAndSemana(
                objectsContainer.getDashboard().getAgencia(), objectsContainer.getDashboard().getAnio(),
                objectsContainer.getDashboard().getSemana()));
    }

    public void setGerencia() {
        objectsContainer.getDashboard()
                .setGerencia(objectsContainer.getPrestamoViewModelsCobranza().get(0).getGerencia());
    }

    public void setClientes() {
        switch (objectsContainer.getDia()) {
            case "miércoles", "Miércoles", "wednesday", "Wednesday" ->
                objectsContainer.getDashboard().setClientes(getClientesMiercoles());
            case "jueves", "Jueves", "thursday", "Thursday" ->
                objectsContainer.getDashboard().setClientes(getClientesJueves());
            case "viernes", "Viernes", "friday", "Friday" ->
                objectsContainer.getDashboard().setClientes(getClientesViernes());
            default ->
                objectsContainer.getDashboard().setClientes(null);
        }
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

        for (int i = 0; i < objectsContainer.getPagEntPagoModelsToDashboard().size(); i++) {
            if (objectsContainer.getPagEntPagoModelsToDashboard().get(i).getMonto() == 0) {
                noPagos++;
            }
        }
        objectsContainer.getDashboard().setNoPagos(noPagos);
    }

    public void setPagosReducidos() {
        int pagosReducidos = 0;

        for (int i = 0; i < objectsContainer.getPagEntPagoModelsToDashboard().size(); i++) {
            if (objectsContainer.getPagEntPagoModelsToDashboard().get(i).getAbreCon() < objectsContainer
                    .getPagEntPagoModelsToDashboard().get(i).getTarifa()) {
                if (objectsContainer.getPagEntPagoModelsToDashboard().get(i).getMonto() < objectsContainer
                        .getPagEntPagoModelsToDashboard().get(i).getAbreCon()) {
                    pagosReducidos++;
                }
            } else {
                if (objectsContainer.getPagEntPagoModelsToDashboard().get(i).getMonto() < objectsContainer
                        .getPagEntPagoModelsToDashboard().get(i).getTarifa()) {
                    pagosReducidos++;
                }
            }
        }

        objectsContainer.getDashboard().setPagosReducidos(pagosReducidos);
    }

    public int getClientesMiercoles() {
        int clientesMiercoles = 0;
        double debitoTotal = 0;

        for (int i = 0; i < objectsContainer.getPrestamoViewModelsCobranza().size(); i++) {
            if (objectsContainer.getPrestamoViewModelsCobranza().get(i).getDiaDePago().equals("MIERCOLES")) {
                clientesMiercoles += 1;
                if (objectsContainer.getPagoDynamicViewModelsCobranza().get(i).getCierraCon() < objectsContainer
                        .getPrestamoViewModelsCobranza().get(i).getTarifa()) {
                    debitoTotal += objectsContainer.getPagoDynamicViewModelsCobranza().get(i).getCierraCon();
                } else {
                    debitoTotal += objectsContainer.getPrestamoViewModelsCobranza().get(i).getTarifa();
                }
            }
        }
        objectsContainer.getDashboard().setDebitoTotal(debitoTotal);

        return clientesMiercoles;
    }

    public int getClientesJueves() {
        int clientesJueves = 0;
        double debitoTotal = 0;

        for (int i = 0; i < objectsContainer.getPrestamoViewModelsCobranza().size(); i++) {
            if (objectsContainer.getPrestamoViewModelsCobranza().get(i).getDiaDePago().equals("JUEVES")) {
                clientesJueves += 1;
                if (objectsContainer.getPagoDynamicViewModelsCobranza().get(i).getCierraCon() < objectsContainer
                        .getPrestamoViewModelsCobranza().get(i).getTarifa()) {
                    debitoTotal += objectsContainer.getPagoDynamicViewModelsCobranza().get(i).getCierraCon();
                } else {
                    debitoTotal += objectsContainer.getPrestamoViewModelsCobranza().get(i).getTarifa();
                }
            }
        }
        objectsContainer.getDashboard().setDebitoTotal(debitoTotal);

        return clientesJueves;
    }

    public int getClientesViernes() {
        int clientesViernes = 0;
        double debitoTotal = 0;

        for (int i = 0; i < objectsContainer.getPrestamoViewModelsCobranza().size(); i++) {
            if (objectsContainer.getPrestamoViewModelsCobranza().get(i).getDiaDePago().equals("VIERNES")) {
                clientesViernes += 1;
                if (objectsContainer.getPagoDynamicViewModelsCobranza().get(i).getCierraCon() < objectsContainer
                        .getPrestamoViewModelsCobranza().get(i).getTarifa()) {
                    debitoTotal += objectsContainer.getPagoDynamicViewModelsCobranza().get(i).getCierraCon();
                } else {
                    debitoTotal += objectsContainer.getPrestamoViewModelsCobranza().get(i).getTarifa();
                }
            }
        }
        objectsContainer.getDashboard().setDebitoTotal(debitoTotal);

        return clientesViernes;
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

        for (int i = 0; i < objectsContainer.getPagEntPagoModelsToDashboard().size(); i++) {
            if (objectsContainer.getPagEntPagoModelsToDashboard().get(i).getMonto() > objectsContainer
                    .getPagEntPagoModelsToDashboard().get(i).getTarifa()) {
                montoExcedente += objectsContainer.getPagEntPagoModelsToDashboard().get(i).getMonto()
                        - objectsContainer.getPagEntPagoModelsToDashboard().get(i).getTarifa();
            }
        }

        if (objectsContainer.getDashboard().getLiquidaciones() != null) {
            if (objectsContainer.getDashboard().getLiquidaciones() > 0) {
                montoExcedente -= objectsContainer.getDashboard().getLiquidaciones();
            }
        }

        objectsContainer.getDashboard().setMontoExcedente(montoExcedente);
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

        for (int i = 0; i < objectsContainer.getPagEntPagoModelsToDashboard().size(); i++) {
            cobranzaTotal += objectsContainer.getPagEntPagoModelsToDashboard().get(i).getMonto();
        }

        objectsContainer.getDashboard().setCobranzaTotal(cobranzaTotal);
    }

    public void setTotalCobranzaPura() {
        double totalCobranzaPura = objectsContainer.getDashboard().getCobranzaTotal()
                - objectsContainer.getDashboard().getMontoExcedente();

        if (objectsContainer.getDashboard().getLiquidaciones() != null) {
            if (objectsContainer.getDashboard().getLiquidaciones() > 0) {
                totalCobranzaPura -= objectsContainer.getDashboard().getLiquidaciones();
            }
        }

        objectsContainer.getDashboard().setTotalCobranzaPura(totalCobranzaPura);
    }

    public void setMontoDeDebitoFaltante() {
        if (objectsContainer.getDashboard().getDebitoTotal() != null
                && objectsContainer.getDashboard().getDebitoTotal() > 0) {
            objectsContainer.getDashboard().setMontoDeDebitoFaltante(objectsContainer.getDashboard().getDebitoTotal()
                    - objectsContainer.getDashboard().getTotalCobranzaPura());
        }
    }

    public void setRendmiento() {
        if (objectsContainer.getDashboard().getDebitoTotal() != null
                && objectsContainer.getDashboard().getDebitoTotal() > 0) {
            double rendimiento = objectsContainer.getDashboard().getTotalCobranzaPura()
                    / objectsContainer.getDashboard().getDebitoTotal() * 100;

            objectsContainer.getDashboard().setRendimiento((Math.round(rendimiento * 100.0) / 100.0));
        }
    }
}

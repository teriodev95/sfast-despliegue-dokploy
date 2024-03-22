package tech.calaverita.reporterloanssql.threads.pwa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.calaverita.reporterloanssql.models.mariaDB.CalendarioModel;
import tech.calaverita.reporterloanssql.pojos.ObjectsContainer;
import tech.calaverita.reporterloanssql.services.AsignacionService;
import tech.calaverita.reporterloanssql.services.CalendarioService;
import tech.calaverita.reporterloanssql.services.LiquidacionService;
import tech.calaverita.reporterloanssql.services.PagoService;
import tech.calaverita.reporterloanssql.services.view.PrestamoService;
import tech.calaverita.reporterloanssql.utils.CobranzaUtil;

@Component
public class DashboardPorDiaPWAThread implements Runnable {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private ObjectsContainer objectsContainer;
    private int opc;
    private Thread[] threads;
    private static AsignacionService asignServ;
    private static CalendarioService calServ;
    private static LiquidacionService liqServ;
    private static PagoService pagServ;
    private static PrestamoService prestServ;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    @Autowired
    private DashboardPorDiaPWAThread(
            AsignacionService asignServ_S,
            CalendarioService calServ_S,
            LiquidacionService liqServ_S,
            PagoService pagServ_S,
            PrestamoService prestServ_S
    ) {
        DashboardPorDiaPWAThread.asignServ = asignServ_S;
        DashboardPorDiaPWAThread.calServ = calServ_S;
        DashboardPorDiaPWAThread.liqServ = liqServ_S;
        DashboardPorDiaPWAThread.pagServ = pagServ_S;
        DashboardPorDiaPWAThread.prestServ = prestServ_S;
    }

    public DashboardPorDiaPWAThread(
            ObjectsContainer objectsContainer,
            int opc,
            Thread[] threads
    ) {
        this.objectsContainer = objectsContainer;
        this.opc = opc;
        this.threads = threads;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public void run() {
        switch (opc) {
            case 0 -> setPrestamosToCobranza();
            case 1 -> setPrestamosToDashboard();
            case 2 -> setPagosToCobranza();
            case 3 -> setPagosToDashboard();
            case 4 -> setLiquidacionesBd();
            case 5 -> setCalendario();
            case 6 -> setAsignaciones();
        }
    }

    public void setCalendario() {
        objectsContainer.setCalendarioModel(DashboardPorDiaPWAThread.calServ.findByFechaActual(objectsContainer.getFechaPago()));
        objectsContainer.getDashboard().setAnio(objectsContainer.getCalendarioModel().getAnio());
        objectsContainer.getDashboard().setSemana(objectsContainer.getCalendarioModel().getSemana());
    }

    public void setPrestamosToCobranza() {
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
        CobranzaUtil.funSemanaAnterior(calendarioModel);

        objectsContainer.setPrestamosToCobranza(DashboardPorDiaPWAThread.prestServ
                .darrprestModFindByAgenciaAnioAndSemanaToCobranzaPGS(agencia, calendarioModel.getAnio(),
                        calendarioModel.getSemana()));

        setGerencia();
        setClientes();
    }

    public void setPrestamosToDashboard() {
        objectsContainer.setPrestamosToDashboard(DashboardPorDiaPWAThread.prestServ
                .darrprestUtilModByAgenciaAndFechaPagoToDashboard(objectsContainer.getDashboard().getAgencia(), objectsContainer.getFechaPago()));
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
        CobranzaUtil.funSemanaAnterior(calendarioModel);

        objectsContainer.setPagosVistaToCobranza(DashboardPorDiaPWAThread.pagServ
                .darrpagUtilModFindByAgenciaAnioAndSemanaToCobranza(agencia, calendarioModel.getAnio(),
                        calendarioModel.getSemana()));
    }

    public void setPagosToDashboard() {
        objectsContainer.setPagEntPagoModelsToDashboard(DashboardPorDiaPWAThread.pagServ
                .darrpagModFindByAgenciaFechaPagoAndNoPrimerPagoToDashboard(objectsContainer.getDashboard().getAgencia(), objectsContainer.getFechaPago()));

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
        objectsContainer.setLiquidaciones(DashboardPorDiaPWAThread.liqServ.darrliqModFindByAgenciaAndFechaPagoToDashboard(objectsContainer.getDashboard().getAgencia(), objectsContainer.getFechaPago()));
        objectsContainer.setPagosOfLiquidaciones(DashboardPorDiaPWAThread.pagServ.darrpagModFindByAgenciaAndFechaPagoToDashboard(objectsContainer.getDashboard().getAgencia(), objectsContainer.getFechaPago()));

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

        objectsContainer.setAsignaciones(DashboardPorDiaPWAThread.asignServ.darrasignModFindByAgenciaAnioAndSemanaToDashboard(objectsContainer.getDashboard().getAgencia(), objectsContainer.getDashboard().getAnio(), objectsContainer.getDashboard().getSemana()));
    }

    public void setGerencia() {
        objectsContainer.getDashboard().setGerencia(objectsContainer.getPrestamosToCobranza().get(0).getGerencia());
    }

    public void setClientes() {
        switch (objectsContainer.getDia()) {
            case "miércoles", "Miércoles", "wednesday", "Wednesday" ->
                    objectsContainer.getDashboard().setClientes(getClientesMiercoles());
            case "jueves", "Jueves", "thursday", "Thursday" ->
                    objectsContainer.getDashboard().setClientes(getClientesJueves());
            case "viernes", "Viernes", "friday", "Friday" ->
                    objectsContainer.getDashboard().setClientes(getClientesViernes());
            default -> objectsContainer.getDashboard().setClientes(null);
        }
    }

    public void setClientesCobrados() {
        objectsContainer.getDashboard().setClientesCobrados(objectsContainer.getPrestamosToDashboard().size());
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
            if (objectsContainer.getPagEntPagoModelsToDashboard().get(i).getAbreCon() < objectsContainer.getPagEntPagoModelsToDashboard().get(i).getTarifa()) {
                if (objectsContainer.getPagEntPagoModelsToDashboard().get(i).getMonto() < objectsContainer.getPagEntPagoModelsToDashboard().get(i).getAbreCon()) {
                    pagosReducidos++;
                }
            } else {
                if (objectsContainer.getPagEntPagoModelsToDashboard().get(i).getMonto() < objectsContainer.getPagEntPagoModelsToDashboard().get(i).getTarifa()) {
                    pagosReducidos++;
                }
            }
        }

        objectsContainer.getDashboard().setPagosReducidos(pagosReducidos);
    }

    public int getClientesMiercoles() {
        int clientesMiercoles = 0;
        double debitoTotal = 0;

        for (int i = 0; i < objectsContainer.getPrestamosToCobranza().size(); i++) {
            if (objectsContainer.getPrestamosToCobranza().get(i).getDiaDePago().equals("MIERCOLES")) {
                clientesMiercoles += 1;
                if (objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon() < objectsContainer.getPrestamosToCobranza().get(i).getTarifa())
                    debitoTotal += objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon();
                else
                    debitoTotal += objectsContainer.getPrestamosToCobranza().get(i).getTarifa();
            }
        }
        objectsContainer.getDashboard().setDebitoTotal(debitoTotal);

        return clientesMiercoles;
    }

    public int getClientesJueves() {
        int clientesJueves = 0;
        double debitoTotal = 0;

        for (int i = 0; i < objectsContainer.getPrestamosToCobranza().size(); i++) {
            if (objectsContainer.getPrestamosToCobranza().get(i).getDiaDePago().equals("JUEVES")) {
                clientesJueves += 1;
                if (objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon() < objectsContainer.getPrestamosToCobranza().get(i).getTarifa())
                    debitoTotal += objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon();
                else
                    debitoTotal += objectsContainer.getPrestamosToCobranza().get(i).getTarifa();
            }
        }
        objectsContainer.getDashboard().setDebitoTotal(debitoTotal);

        return clientesJueves;
    }

    public int getClientesViernes() {
        int clientesViernes = 0;
        double debitoTotal = 0;

        for (int i = 0; i < objectsContainer.getPrestamosToCobranza().size(); i++) {
            if (objectsContainer.getPrestamosToCobranza().get(i).getDiaDePago().equals("VIERNES")) {
                clientesViernes += 1;
                if (objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon() < objectsContainer.getPrestamosToCobranza().get(i).getTarifa())
                    debitoTotal += objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon();
                else
                    debitoTotal += objectsContainer.getPrestamosToCobranza().get(i).getTarifa();
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
            if (objectsContainer.getPagEntPagoModelsToDashboard().get(i).getMonto() > objectsContainer.getPagEntPagoModelsToDashboard().get(i).getTarifa()) {
                montoExcedente += objectsContainer.getPagEntPagoModelsToDashboard().get(i).getMonto() - objectsContainer.getPagEntPagoModelsToDashboard().get(i).getTarifa();
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
                    liquidaciones += objectsContainer.getLiquidaciones().get(i).getLiquidoCon() - objectsContainer.getPagosOfLiquidaciones().get(i).getTarifa();
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
        double totalCobranzaPura = objectsContainer.getDashboard().getCobranzaTotal() - objectsContainer.getDashboard().getMontoExcedente();

        if (objectsContainer.getDashboard().getLiquidaciones() != null) {
            if (objectsContainer.getDashboard().getLiquidaciones() > 0) {
                totalCobranzaPura -= objectsContainer.getDashboard().getLiquidaciones();
            }
        }

        objectsContainer.getDashboard().setTotalCobranzaPura(totalCobranzaPura);
    }

    public void setMontoDeDebitoFaltante() {
        if (objectsContainer.getDashboard().getDebitoTotal() != null && objectsContainer.getDashboard().getDebitoTotal() > 0) {
            objectsContainer.getDashboard().setMontoDeDebitoFaltante(objectsContainer.getDashboard().getDebitoTotal() - objectsContainer.getDashboard().getTotalCobranzaPura());
        }
    }

    public void setRendmiento() {
        if (objectsContainer.getDashboard().getDebitoTotal() != null && objectsContainer.getDashboard().getDebitoTotal() > 0) {
            double rendimiento = objectsContainer.getDashboard().getTotalCobranzaPura() / objectsContainer.getDashboard().getDebitoTotal() * 100;

            objectsContainer.getDashboard().setRendimiento((Math.round(rendimiento * 100.0) / 100.0));
        }
    }
}
package tech.calaverita.sfast_xpress.threads;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.calaverita.sfast_xpress.models.mariaDB.AgenciaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.CalendarioModel;
import tech.calaverita.sfast_xpress.pojos.ObjectsContainer;
import tech.calaverita.sfast_xpress.services.AgenciaService;
import tech.calaverita.sfast_xpress.services.AsignacionService;
import tech.calaverita.sfast_xpress.services.LiquidacionService;
import tech.calaverita.sfast_xpress.services.PagoService;
import tech.calaverita.sfast_xpress.services.views.PagoUtilService;
import tech.calaverita.sfast_xpress.services.views.PrestamoService;
import tech.calaverita.sfast_xpress.services.views.PrestamoUtilService;
import tech.calaverita.sfast_xpress.utils.CobranzaUtil;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Component
public class DashboardThread implements Runnable {
    private ObjectsContainer objectsContainer;
    private int opc;
    private Thread[] threads;
    private static AsignacionService asignacionService;
    private static LiquidacionService liquidacionService;
    private static PagoService pagoService;
    private static PrestamoService prestamoService;
    private static AgenciaService agenciaService;
    private static PrestamoUtilService prestamoUtilService;
    private static PagoUtilService pagoUtilService;

    @Autowired
    private DashboardThread(
            AsignacionService asignacionService,
            LiquidacionService liquidacionService,
            PagoService pagoService,
            PrestamoService prestamoService,
            AgenciaService agenciaService,
            PrestamoUtilService prestamoUtilService,
            PagoUtilService pagoUtilService
    ) {
        DashboardThread.asignacionService = asignacionService;
        DashboardThread.liquidacionService = liquidacionService;
        DashboardThread.pagoService = pagoService;
        DashboardThread.prestamoService = prestamoService;
        DashboardThread.agenciaService = agenciaService;
        DashboardThread.prestamoUtilService = prestamoUtilService;
        DashboardThread.pagoUtilService = pagoUtilService;
    }

    public DashboardThread(
            ObjectsContainer objectsContainer,
            int opc
    ) {
        this.objectsContainer = objectsContainer;
        this.opc = opc;
    }

    public DashboardThread(
            ObjectsContainer objectsContainer,
            int opc,
            Thread[] threads
    ) {
        this.objectsContainer = objectsContainer;
        this.opc = opc;
        this.threads = threads;
    }

    @Override
    public void run() {
        switch (this.opc) {
            case 0 -> setPrestamosToCobranza();
            case 1 -> setPrestamosToDashboard();
            case 2 -> setPagosToCobranza();
            case 3 -> setPagosToDashboard();
            case 4 -> setLiquidacionesBd();
            case 5 -> setPagosOfLiquidaciones();
            case 6 -> setAsignaciones();
        }
    }

    public void setPrestamosToCobranza() {
        // To easy code
        String agencia = this.objectsContainer.getDashboard().getAgencia();
        int anio = this.objectsContainer.getDashboard().getAnio();
        int semana = this.objectsContainer.getDashboard().getSemana();

        CalendarioModel calendarioModel = new CalendarioModel();
        calendarioModel.setAnio(anio);
        calendarioModel.setSemana(semana);

        CobranzaUtil.funSemanaAnterior(calendarioModel);

        this.objectsContainer.setPrestamosToCobranza(DashboardThread.prestamoService
                .darrprestModFindByAgenciaAnioAndSemanaToCobranzaPGS(agencia, calendarioModel.getAnio(),
                        calendarioModel.getSemana()));

        this.setGerencia();
        this.setClientes();
    }

    public void setPrestamosToDashboard() {
        this.objectsContainer.setPrestamosToDashboard(DashboardThread.prestamoUtilService
                .darrprestUtilModFindByAgenciaAnioAndSemanaToDashboard(this.objectsContainer.getDashboard()
                        .getAgencia(), this.objectsContainer.getDashboard().getAnio(), this.objectsContainer
                        .getDashboard().getSemana()));
    }

    public void setPagosToCobranza() {
        // To easy code
        String agencia = this.objectsContainer.getDashboard().getAgencia();
        int anio = this.objectsContainer.getDashboard().getAnio();
        int semana = this.objectsContainer.getDashboard().getSemana();

        CalendarioModel calendarioModel = new CalendarioModel();
        calendarioModel.setAnio(anio);
        calendarioModel.setSemana(semana);
        CobranzaUtil.funSemanaAnterior(calendarioModel);

        double cierraConGreaterThan = 0;
        this.objectsContainer.setPagosVistaToCobranza(DashboardThread.pagoUtilService
                .findByAgenciaAnioSemanaAndCierraConGreaterThan(agencia, calendarioModel.getAnio(),
                        calendarioModel.getSemana(), cierraConGreaterThan));

        try {
            this.threads[0].join();
        } catch (InterruptedException e) {
            System.out.println("Ha fallado el seteoDeDebitos " + e);
        }

        this.setDebitoMiercoles();
        this.setDebitoJueves();
        this.setDebitoViernes();
        this.setDebitoTotal();
    }

    public void setPagosToDashboard() {
        boolean esPrimerPago = false;
        this.objectsContainer.setPagosVistaToDashboard(DashboardThread.pagoUtilService
                .findByAgenciaAnioSemanaAndEsPrimerPago(this.objectsContainer.getDashboard().getAgencia(),
                        this.objectsContainer.getDashboard().getAnio(), this.objectsContainer.getDashboard()
                                .getSemana(), esPrimerPago));

        this.setClientesCobrados();
        this.setMultas();
        this.setNoPagos();
        this.setPagosReducidos();
        this.setCobranzaTotal();

        try {
            this.threads[4].join();
        } catch (InterruptedException e) {
            System.out.println("Ha fallado el setMontoExcedente " + e);
        }

        this.setMontoExcedente();

        try {
            this.threads[4].join();
        } catch (InterruptedException e) {
            System.out.println("Ha fallado el setTotalCobranzaPura " + e);
        }

        this.setTotalCobranzaPura();

        try {
            this.threads[2].join();
        } catch (InterruptedException e) {
            System.out.println("Ha fallado el setMontoDeDebitoFaltante " + e);
        }

        this.setMontoDeDebitoFaltante();

        try {
            this.threads[2].join();
        } catch (InterruptedException e) {
            System.out.println("Ha fallado el setRendimiento " + e);
        }

        this.setRendmiento();
    }

    public void setLiquidacionesBd() {
        this.objectsContainer.setLiquidaciones(DashboardThread.liquidacionService
                .findByAgenciaAnioAndSemana(this.objectsContainer.getDashboard().getAgencia(),
                        this.objectsContainer.getDashboard().getAnio(), this.objectsContainer.getDashboard()
                                .getSemana()));

        this.setNumeroDeLiquidaciones();
        this.setTotalDeDescuento();

        try {
            this.threads[5].join();
        } catch (InterruptedException e) {
            System.out.println("Ha fallado el setLiquidaciones " + e);
        }

        this.setLiquidaciones();
    }

    public void setPagosOfLiquidaciones() {
        this.objectsContainer.setPagosOfLiquidaciones(DashboardThread.pagoService
                .findByAgenteAnioAndSemanaInnerJoinLiquidacionModel(this.objectsContainer.getDashboard().getAgencia(),
                        this.objectsContainer.getDashboard().getAnio(), this.objectsContainer.getDashboard()
                                .getSemana()));
    }

    public void setAsignaciones() {
        this.objectsContainer.setAsignaciones(DashboardThread.asignacionService
                .findByAgenciaAnioAndSemana(this.objectsContainer.getDashboard().getAgencia(),
                        this.objectsContainer.getDashboard().getAnio(), this.objectsContainer.getDashboard()
                                .getSemana()));

        try {
            this.threads[3].join();

        } catch (InterruptedException e) {
            System.out.println("Ha fallado el setEfectivoEnCampo " + e);
        }

        this.setEfectivoEnCampo();
    }

    public void setGerencia() {
        AgenciaModel agenciaEntity = DashboardThread.agenciaService.findById(this
                .objectsContainer.getDashboard().getAgencia());

        if (
                this.objectsContainer.getDashboard().getGerencia() == null && agenciaEntity != null
        ) {
            this.objectsContainer.getDashboard().setGerencia(agenciaEntity.getGerenciaId());
        }
    }

    public void setClientes() {
        this.objectsContainer.getDashboard().setClientes(this.objectsContainer.getPrestamosToCobranza().size());
    }

    public void setClientesCobrados() {
        this.objectsContainer.getDashboard().setClientesCobrados(this.objectsContainer.getPagosVistaToDashboard()
                .size());
    }

    public void setNumeroDeLiquidaciones() {
        this.objectsContainer.getDashboard().setNumeroLiquidaciones(this.objectsContainer.getLiquidaciones().size());
    }

    public void setMultas() {
        this.objectsContainer.getDashboard().setMultas(0.0);
    }

    public void setNoPagos() {
        int noPagos = 0;

        for (int i = 0; i < this.objectsContainer.getPagosVistaToDashboard().size(); i++) {
            if (
                    this.objectsContainer.getPagosVistaToDashboard().get(i).getMonto() == 0
            ) {
                noPagos++;
            }
        }
        this.objectsContainer.getDashboard().setNoPagos(noPagos);
    }

    public void setPagosReducidos() {
        int pagosReducidos = 0;

        for (int i = 0; i < this.objectsContainer.getPagosVistaToDashboard().size(); i++) {
            if (
                    this.objectsContainer.getPagosVistaToDashboard().get(i).getMonto() != 0
            ) {
                if (
                        this.objectsContainer.getPagosVistaToDashboard().get(i).getAbreCon() < this.objectsContainer
                                .getPagosVistaToDashboard().get(i).getTarifa()
                ) {
                    if (
                            this.objectsContainer.getPagosVistaToDashboard().get(i).getMonto() < this.objectsContainer
                                    .getPagosVistaToDashboard().get(i).getAbreCon()
                    ) {
                        pagosReducidos++;
                    }
                } else {
                    if (
                            this.objectsContainer.getPagosVistaToDashboard().get(i).getMonto() < this.objectsContainer
                                    .getPagosVistaToDashboard().get(i).getTarifa()
                    ) {
                        pagosReducidos++;
                    }
                }
            }
        }

        this.objectsContainer.getDashboard().setPagosReducidos(pagosReducidos);
    }

    public void setDebitoMiercoles() {
        double debitoMiercoles = 0;

        for (int i = 0; i < this.objectsContainer.getPrestamosToCobranza().size(); i++) {
            if (
                    this.objectsContainer.getPrestamosToCobranza().get(i).getDiaDePago().equals("MIERCOLES")
            ) {
                if (
                        this.objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon() < this.objectsContainer
                                .getPrestamosToCobranza().get(i).getTarifa()
                )
                    debitoMiercoles += this.objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon();
                else
                    debitoMiercoles += this.objectsContainer.getPrestamosToCobranza().get(i).getTarifa();
            }

        }
        this.objectsContainer.getDashboard().setDebitoMiercoles(MyUtil.getDouble(debitoMiercoles));
    }

    public void setDebitoJueves() {
        double debitoJueves = 0;

        for (int i = 0; i < this.objectsContainer.getPrestamosToCobranza().size(); i++) {
            if (
                    this.objectsContainer.getPrestamosToCobranza().get(i).getDiaDePago().equals("JUEVES")
            ) {
                if (
                        this.objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon() < this.objectsContainer
                                .getPrestamosToCobranza().get(i).getTarifa()
                )
                    debitoJueves += this.objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon();
                else
                    debitoJueves += this.objectsContainer.getPrestamosToCobranza().get(i).getTarifa();
            }
        }
        this.objectsContainer.getDashboard().setDebitoJueves(MyUtil.getDouble(debitoJueves));
    }

    public void setDebitoViernes() {
        double debitoViernes = 0;

        for (int i = 0; i < this.objectsContainer.getPrestamosToCobranza().size(); i++) {
            if (
                    this.objectsContainer.getPrestamosToCobranza().get(i).getDiaDePago().equals("VIERNES")
            ) {
                if (
                        this.objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon() < this.objectsContainer
                                .getPrestamosToCobranza().get(i).getTarifa()
                )
                    debitoViernes += this.objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon();
                else
                    debitoViernes += this.objectsContainer.getPrestamosToCobranza().get(i).getTarifa();
            }
        }
        this.objectsContainer.getDashboard().setDebitoViernes(MyUtil.getDouble(debitoViernes));
    }

    public void setDebitoTotal() {
        double debitoTotal = 0;

        for (
                int i = 0; i < this.objectsContainer.getPrestamosToCobranza().size(); i++
        ) {
            if (
                    this.objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon() < this.objectsContainer
                            .getPrestamosToCobranza().get(i).getTarifa()
            )
                debitoTotal += this.objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon();
            else
                debitoTotal += this.objectsContainer.getPrestamosToCobranza().get(i).getTarifa();
        }
        this.objectsContainer.getDashboard().setDebitoTotal(MyUtil.getDouble(debitoTotal));
    }

    public void setTotalDeDescuento() {
        double totalDeDescuento = 0;

        if (
                this.objectsContainer.getLiquidaciones() != null && !this.objectsContainer.getLiquidaciones().isEmpty()
        ) {
            for (int i = 0; i < this.objectsContainer.getLiquidaciones().size(); i++) {
                totalDeDescuento += this.objectsContainer.getLiquidaciones().get(i).getDescuentoEnDinero();
            }
        }

        this.objectsContainer.getDashboard().setTotalDeDescuento(MyUtil.getDouble(totalDeDescuento));
    }

    public void setMontoExcedente() {
        double montoExcedente = 0;

        for (int i = 0; i < this.objectsContainer.getPagosVistaToDashboard().size(); i++) {
            if (
                    this.objectsContainer.getPagosVistaToDashboard().get(i).getMonto() > this.objectsContainer
                            .getPagosVistaToDashboard().get(i).getTarifa()
            ) {
                montoExcedente += this.objectsContainer.getPagosVistaToDashboard().get(i).getMonto()
                        - this.objectsContainer.getPagosVistaToDashboard().get(i).getTarifa();
            }
        }

        if (this.objectsContainer.getDashboard().getLiquidaciones() != null) {
            if (this.objectsContainer.getDashboard().getLiquidaciones() > 0) {
                montoExcedente -= this.objectsContainer.getDashboard().getLiquidaciones();
            }
        }

        this.objectsContainer.getDashboard().setMontoExcedente(MyUtil.getDouble(montoExcedente));
    }

    public void setLiquidaciones() {
        double liquidaciones = 0;

        if (this.objectsContainer.getLiquidaciones() != null) {
            if (
                    !this.objectsContainer.getLiquidaciones().isEmpty()
            ) {
                for (int i = 0; i < this.objectsContainer.getLiquidaciones().size(); i++) {
                    liquidaciones += this.objectsContainer.getLiquidaciones().get(i).getLiquidoCon()
                            - this.objectsContainer.getPagosOfLiquidaciones().get(i).getTarifa();
                }
            }
        }

        this.objectsContainer.getDashboard().setLiquidaciones(MyUtil.getDouble(liquidaciones));
    }

    public void setCobranzaTotal() {
        double cobranzaTotal = 0;

        for (int i = 0; i < this.objectsContainer.getPagosVistaToDashboard().size(); i++) {
            cobranzaTotal += this.objectsContainer.getPagosVistaToDashboard().get(i).getMonto();
        }

        this.objectsContainer.getDashboard().setCobranzaTotal(MyUtil.getDouble(cobranzaTotal));
    }

    public void setTotalCobranzaPura() {
        double totalCobranzaPura = this.objectsContainer.getDashboard().getCobranzaTotal() - this.objectsContainer
                .getDashboard().getMontoExcedente();

        if (this.objectsContainer.getDashboard().getLiquidaciones() != null) {
            if (
                    this.objectsContainer.getDashboard().getLiquidaciones() > 0
            ) {
                totalCobranzaPura -= this.objectsContainer.getDashboard().getLiquidaciones();
            }
        }

        this.objectsContainer.getDashboard().setTotalCobranzaPura(MyUtil.getDouble(totalCobranzaPura));
    }

    public void setMontoDeDebitoFaltante() {
        Double montoDebitoFaltante = this.objectsContainer.getDashboard()
                .getDebitoTotal() - this.objectsContainer.getDashboard().getTotalCobranzaPura();

        this.objectsContainer.getDashboard().setMontoDeDebitoFaltante(MyUtil.getDouble(montoDebitoFaltante));
    }

    public void setEfectivoEnCampo() {
        double asignaciones = 0;

        if (
                this.objectsContainer.getAsignaciones() != null
        ) {
            if (
                    !this.objectsContainer.getAsignaciones().isEmpty()
            ) {
                for (int i = 0; i < this.objectsContainer.getAsignaciones().size(); i++) {
                    asignaciones += this.objectsContainer.getAsignaciones().get(i).getMonto();
                }
            }
        }

        double efectivoEnCampo = this.objectsContainer.getDashboard().getCobranzaTotal() - asignaciones;

        this.objectsContainer.getDashboard().setEfectivoEnCampo((double) Math.round(efectivoEnCampo * 100.0) / 100.0);
    }

    public void setRendmiento() {
        double rendimiento = this.objectsContainer.getDashboard().getTotalCobranzaPura() / this.objectsContainer
                .getDashboard().getDebitoTotal() * 100;

        this.objectsContainer.getDashboard().setRendimiento((Math.round(rendimiento * 100.0) / 100.0));
    }
}
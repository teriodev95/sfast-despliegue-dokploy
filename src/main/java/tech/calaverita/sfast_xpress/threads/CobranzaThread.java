package tech.calaverita.sfast_xpress.threads;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.calaverita.sfast_xpress.models.mariaDB.CalendarioModel;
import tech.calaverita.sfast_xpress.pojos.ObjectsContainer;
import tech.calaverita.sfast_xpress.services.views.PagoUtilService;
import tech.calaverita.sfast_xpress.services.views.PrestamoService;
import tech.calaverita.sfast_xpress.utils.CobranzaUtil;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Component
public class CobranzaThread implements Runnable {
    private ObjectsContainer objectsContainer;
    private int opc;
    private Thread[] threads;
    private static PrestamoService prestamoService;
    private static PagoUtilService pagoUtilService;

    @Autowired
    private CobranzaThread(
            PrestamoService prestamoService,
            PagoUtilService pagoUtilService
    ) {
        CobranzaThread.prestamoService = prestamoService;
        CobranzaThread.pagoUtilService = pagoUtilService;
    }

    public CobranzaThread(
            ObjectsContainer objectsContainer,
            int ope
    ) {
        this.objectsContainer = objectsContainer;
        this.opc = ope;
    }

    public CobranzaThread(
            ObjectsContainer objectsContainer,
            int ope,
            Thread[] threads
    ) {
        this.objectsContainer = objectsContainer;
        this.opc = ope;
        this.threads = threads;
    }

    @Override
    public void run() {
        switch (this.opc) {
            case 0:
                this.getPrestamos();
                break;
            case 1:
                this.getPagos();
                break;
            case 2:
                this.getGerencia();
                break;
            case 3:
                this.getClientes();
                break;
            case 4:
                this.getDebitoMiercoles();
                break;
            case 5:
                this.getDebitoJueves();
                break;
            case 6:
                this.getDebitoViernes();
                break;
            case 7:
                this.getDebitoTotal();
                break;
        }
    }

    public void getGerencia() {
        try {
            this.threads[0].join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        this.objectsContainer.getCobranza().setGerencia(this.objectsContainer.getPrestamosToCobranza().get(0)
                .getGerencia());
    }

    public void getClientes() {
        try {
            this.threads[0].join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        this.objectsContainer.getCobranza().setClientes(this.objectsContainer.getPrestamosToCobranza().size());
    }

    public void getPrestamos() {
        // To easy code
        String agencia = this.objectsContainer.getCobranza().getAgencia();
        int anio = this.objectsContainer.getCobranza().getAnio();
        int semana = this.objectsContainer.getCobranza().getSemana();

        CalendarioModel calendarioModel = new CalendarioModel();
        calendarioModel.setAnio(anio);
        calendarioModel.setSemana(semana);
        CobranzaUtil.funSemanaAnterior(calendarioModel);

        this.objectsContainer.setPrestamosToCobranza(CobranzaThread.prestamoService
                .darrprestModFindByAgenciaAnioAndSemanaToCobranzaPGS(agencia, calendarioModel.getAnio(),
                        calendarioModel.getSemana()));
        this.objectsContainer.getCobranza().setPrestamos(this.objectsContainer.getPrestamosToCobranza());
    }

    public void getPagos() {
        // To easy code
        String agencia = this.objectsContainer.getCobranza().getAgencia();
        int anio = this.objectsContainer.getCobranza().getAnio();
        int semana = this.objectsContainer.getCobranza().getSemana();

        CalendarioModel calendarioModel = new CalendarioModel();
        calendarioModel.setAnio(anio);
        calendarioModel.setSemana(semana);
        CobranzaUtil.funSemanaAnterior(calendarioModel);

        double cierraConGreaterThan = 0;
        objectsContainer.setPagosVistaToCobranza(CobranzaThread.pagoUtilService
                .findByAgenciaAnioSemanaAndCierraConGreaterThan(agencia, calendarioModel.getAnio(),
                        calendarioModel.getSemana(), cierraConGreaterThan));
    }

    public void getDebitoMiercoles() {
        try {
            this.threads[0].join();
            this.threads[1].join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        double debitoMiercoles = 0;

        for (int i = 0; i < this.objectsContainer.getPrestamosToCobranza().size(); i++) {
            if (this.objectsContainer.getPrestamosToCobranza().get(i).getDiaDePago().equals("MIERCOLES")) {
                if (this.objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon() < this.objectsContainer
                        .getPrestamosToCobranza().get(i).getTarifa()) {
                    debitoMiercoles += this.objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon();
                } else {
                    debitoMiercoles += this.objectsContainer.getPrestamosToCobranza().get(i).getTarifa();
                }
            }

            debitoMiercoles = MyUtil.getDouble(debitoMiercoles);
            this.objectsContainer.getCobranza().setDebitoMiercoles(debitoMiercoles);
        }
    }

    public void getDebitoJueves() {
        try {
            this.threads[0].join();
            this.threads[1].join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        double debitoJueves = 0;

        for (int i = 0; i < this.objectsContainer.getPrestamosToCobranza().size(); i++) {
            if (this.objectsContainer.getPrestamosToCobranza().get(i).getDiaDePago().equals("JUEVES")) {
                if (this.objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon() < this.objectsContainer
                        .getPrestamosToCobranza().get(i).getTarifa()) {
                    debitoJueves += this.objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon();
                } else {
                    debitoJueves += this.objectsContainer.getPrestamosToCobranza().get(i).getTarifa();
                }
            }

            debitoJueves = MyUtil.getDouble(debitoJueves);
            this.objectsContainer.getCobranza().setDebitoJueves(debitoJueves);
        }
    }

    public void getDebitoViernes() {
        try {
            threads[0].join();
            threads[1].join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        double debitoViernes = 0;

        for (int i = 0; i < this.objectsContainer.getPrestamosToCobranza().size(); i++) {
            if (this.objectsContainer.getPrestamosToCobranza().get(i).getDiaDePago().equals("VIERNES")) {
                if (this.objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon() < this.objectsContainer
                        .getPrestamosToCobranza().get(i).getTarifa()) {
                    debitoViernes += this.objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon();
                } else {
                    debitoViernes += this.objectsContainer.getPrestamosToCobranza().get(i).getTarifa();
                }
            }

            debitoViernes = MyUtil.getDouble(debitoViernes);
            this.objectsContainer.getCobranza().setDebitoViernes(debitoViernes);
        }
    }

    public void getDebitoTotal() {
        try {
            this.threads[0].join();
            this.threads[1].join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        double debitoTotal = 0;

        for (int i = 0; i < this.objectsContainer.getPrestamosToCobranza().size(); i++) {
            if (this.objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon() < this.objectsContainer
                    .getPrestamosToCobranza().get(i).getTarifa()) {
                debitoTotal += this.objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon();
            } else {
                debitoTotal += this.objectsContainer.getPrestamosToCobranza().get(i).getTarifa();
            }

            debitoTotal = MyUtil.getDouble(debitoTotal);
            this.objectsContainer.getCobranza().setDebitoTotal(debitoTotal);
        }
    }
}

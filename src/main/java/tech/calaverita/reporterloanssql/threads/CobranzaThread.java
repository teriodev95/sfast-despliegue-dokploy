package tech.calaverita.reporterloanssql.threads;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.calaverita.reporterloanssql.pojos.ObjectsContainer;
import tech.calaverita.reporterloanssql.services.PagoService;
import tech.calaverita.reporterloanssql.services.view.PrestamoService;

@Component
public class CobranzaThread implements Runnable {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private ObjectsContainer objectsContainer;
    private int opc;
    private Thread[] threads;
    private static PagoService pagServ;
    private static PrestamoService prestServ;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    @Autowired
    private CobranzaThread(
            PagoService pagServ_S,
            PrestamoService prestServ_S
    ) {
        CobranzaThread.pagServ = pagServ_S;
        CobranzaThread.prestServ = prestServ_S;
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public CobranzaThread(
            ObjectsContainer objectsContainer,
            int ope
    ) {
        this.objectsContainer = objectsContainer;
        this.opc = ope;
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public CobranzaThread(
            ObjectsContainer objectsContainer,
            int ope,
            Thread[] threads
    ) {
        this.objectsContainer = objectsContainer;
        this.opc = ope;
        this.threads = threads;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
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

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public void getGerencia() {
        try {
            this.threads[0].join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        this.objectsContainer.getCobranza().setGerencia(this.objectsContainer.getPrestamosToCobranza().get(0).getGerencia());
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public void getClientes() {
        try {
            this.threads[0].join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        this.objectsContainer.getCobranza().setClientes(this.objectsContainer.getPrestamosToCobranza().size());
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public void getPrestamos() {
        this.objectsContainer.setPrestamosToCobranza(CobranzaThread.prestServ
                .darrprestModFindByAgenciaAnioAndSemanaToCobranzaPGS(this.objectsContainer.getCobranza().getAgencia(),
                        this.objectsContainer.getCobranza().getAnio(), objectsContainer.getCobranza().getSemana()));
        this.objectsContainer.getCobranza().setPrestamos(this.objectsContainer.getPrestamosToCobranza());
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public void getPagos() {
        objectsContainer.setPagosVistaToCobranza(CobranzaThread.pagServ
                .darrpagUtilModFindByAgenciaAnioAndSemanaToCobranza(this.objectsContainer.getCobranza().getAgencia(),
                        this.objectsContainer.getCobranza().getAnio(), this.objectsContainer.getCobranza().getSemana()));
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public void getDebitoMiercoles() {
        try {
            this.threads[0].join();
            this.threads[1].join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

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

            this.objectsContainer.getCobranza().setDebitoMiercoles(debitoMiercoles);
        }
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public void getDebitoJueves() {
        try {
            this.threads[0].join();
            this.threads[1].join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

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

            this.objectsContainer.getCobranza().setDebitoJueves(debitoJueves);
        }
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public void getDebitoViernes() {
        try {
            threads[0].join();
            threads[1].join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

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

            this.objectsContainer.getCobranza().setDebitoViernes(debitoViernes);
        }
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public void getDebitoTotal() {
        try {
            this.threads[0].join();
            this.threads[1].join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        double debitoTotal = 0;

        for (int i = 0; i < this.objectsContainer.getPrestamosToCobranza().size(); i++) {
            if (
                    this.objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon() < this.objectsContainer
                    .getPrestamosToCobranza().get(i).getTarifa()
            )
                debitoTotal += this.objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon();
            else
                debitoTotal += this.objectsContainer.getPrestamosToCobranza().get(i).getTarifa();

            this.objectsContainer.getCobranza().setDebitoTotal(debitoTotal);
        }
    }
}

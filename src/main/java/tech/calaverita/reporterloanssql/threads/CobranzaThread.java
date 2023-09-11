package tech.calaverita.reporterloanssql.threads;

import tech.calaverita.reporterloanssql.pojos.ObjectsContainer;
import tech.calaverita.reporterloanssql.services.PagoService;
import tech.calaverita.reporterloanssql.services.PrestamoService;

public class CobranzaThread implements Runnable {
    public ObjectsContainer objectsContainer;
    public int opc;
    public Thread[] threads;

    public CobranzaThread(ObjectsContainer objectsContainer, int ope) {
        this.objectsContainer = objectsContainer;
        this.opc = ope;
    }

    public CobranzaThread(ObjectsContainer objectsContainer, int ope, Thread[] threads) {
        this.objectsContainer = objectsContainer;
        this.opc = ope;
        this.threads = threads;
    }

    @Override
    public void run() {
        switch (opc) {
            case 0:
                getPrestamos();
                break;
            case 1:
                getPagos();
                break;
            case 2:
                getGerencia();
                break;
            case 3:
                getClientes();
                break;
            case 4:
                getDebitoMiercoles();
                break;
            case 5:
                getDebitoJueves();
                break;
            case 6:
                getDebitoViernes();
                break;
            case 7:
                getDebitoTotal();
                break;
        }
    }

    public void getGerencia() {
        try {
            threads[0].join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        objectsContainer.getCobranza().setGerencia(objectsContainer.getPrestamosToCobranza().get(0).getGerencia());
    }

    public void getClientes() {
        try {
            threads[0].join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        objectsContainer.getCobranza().setClientes(objectsContainer.getPrestamosToCobranza().size());
    }

    public void getPrestamos() {
        objectsContainer.setPrestamosToCobranza(PrestamoService.getPrestamoModelsByAgenciaAnioAndSemanaToCobranzaPGS(objectsContainer.getCobranza().getAgencia(), objectsContainer.getCobranza().getAnio(), objectsContainer.getCobranza().getSemana()));
        objectsContainer.getCobranza().setPrestamos(objectsContainer.getPrestamosToCobranza());
    }

    public void getPagos() {
        objectsContainer.setPagosVistaToCobranza(PagoService.getPagoUtilModelsByAgenciaAnioAndSemanaToCobranza(objectsContainer.getCobranza().getAgencia(), objectsContainer.getCobranza().getAnio(), objectsContainer.getCobranza().getSemana()));
    }

    public void getDebitoMiercoles() {
        try {
            threads[0].join();
            threads[1].join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        double debitoMiercoles = 0;

        for (int i = 0; i < objectsContainer.getPrestamosToCobranza().size(); i++) {
            if (objectsContainer.getPrestamosToCobranza().get(i).getDiaDePago().equals("MIERCOLES")) {
                if (objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon() < objectsContainer.getPrestamosToCobranza().get(i).getTarifa())
                    debitoMiercoles += objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon();
                else
                    debitoMiercoles += objectsContainer.getPrestamosToCobranza().get(i).getTarifa();
            }

            objectsContainer.getCobranza().setDebitoMiercoles(debitoMiercoles);
        }
    }

    public void getDebitoJueves() {
        try {
            threads[0].join();
            threads[1].join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        double debitoJueves = 0;

        for (int i = 0; i < objectsContainer.getPrestamosToCobranza().size(); i++) {
            if (objectsContainer.getPrestamosToCobranza().get(i).getDiaDePago().equals("JUEVES")) {
                if (objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon() < objectsContainer.getPrestamosToCobranza().get(i).getTarifa())
                    debitoJueves += objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon();
                else
                    debitoJueves += objectsContainer.getPrestamosToCobranza().get(i).getTarifa();
            }

            objectsContainer.getCobranza().setDebitoJueves(debitoJueves);
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

        for (int i = 0; i < objectsContainer.getPrestamosToCobranza().size(); i++) {
            if (objectsContainer.getPrestamosToCobranza().get(i).getDiaDePago().equals("VIERNES")) {
                if (objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon() < objectsContainer.getPrestamosToCobranza().get(i).getTarifa())
                    debitoViernes += objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon();
                else
                    debitoViernes += objectsContainer.getPrestamosToCobranza().get(i).getTarifa();
            }

            objectsContainer.getCobranza().setDebitoViernes(debitoViernes);
        }
    }

    public void getDebitoTotal() {
        try {
            threads[0].join();
            threads[1].join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        double debitoTotal = 0;

        for (int i = 0; i < objectsContainer.getPrestamosToCobranza().size(); i++) {
            if (objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon() < objectsContainer.getPrestamosToCobranza().get(i).getTarifa())
                debitoTotal += objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon();
            else
                debitoTotal += objectsContainer.getPrestamosToCobranza().get(i).getTarifa();

            objectsContainer.getCobranza().setDebitoTotal(debitoTotal);
        }
    }
}

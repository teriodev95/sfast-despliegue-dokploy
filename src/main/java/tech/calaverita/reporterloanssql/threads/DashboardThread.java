package tech.calaverita.reporterloanssql.threads;

import tech.calaverita.reporterloanssql.pojos.ObjectsContainer;
import tech.calaverita.reporterloanssql.services.RepositoriesContainer;

public class DashboardThread implements Runnable {
    public RepositoriesContainer repositoriesContainer;
    public ObjectsContainer objectsContainer;
    public int opc;

    public DashboardThread(RepositoriesContainer repositoriesContainer, ObjectsContainer objectsContainer, int opc) {
        this.repositoriesContainer = repositoriesContainer;
        this.objectsContainer = objectsContainer;
        this.opc = opc;
    }

    @Override
    public void run() {
        switch (opc) {
            case 0 -> getPrestamosToCobranza();
            case 1 -> getPrestamosToDashboard();
            case 2 -> getPagosToCobranza();
            case 3 -> getPagosToDashboard();
            case 4 -> getLiquidacionesBd();
            case 5 -> getAsignaciones();
            case 6 -> getAgencia();
            case 7 -> getClientes();
            case 8 -> getDebitoMiercoles();
            case 9 -> getDebitoJueves();
            case 10 -> getDebitoViernes();
            case 11 -> getDebitoTotal();
            case 12 -> getLiquidaciones();
            case 13 -> getNumeroDeLiquidaciones();
            case 14 -> getTotalDeDescuento();
            case 15 -> getClientesCobrados();
            case 16 -> getNoPagos();
            case 17 -> getPagosReducidos();
            case 18 -> getMontoExcedente();
            case 19 -> getCobranzaTotal();
            case 20 -> getTotalCobranzaPura();
            case 21 -> getEfectivoEnCampo();
            case 22 -> getRendmiento();
            case 23 -> getMontoDeDebitoFaltante();
        }
    }

    public void getPrestamosToCobranza() {
        objectsContainer.setPrestamosToCobranza(repositoriesContainer.getPrestamoRepository().getPrestamosByAgenciaAnioAndSemanaToCobranza(objectsContainer.getDashboard().getAgencia(), objectsContainer.getDashboard().getAnio(), objectsContainer.getDashboard().getSemana()));
    }

    public void getPrestamosToDashboard() {
        objectsContainer.setPrestamosToDashboard(repositoriesContainer.getPrestamoRepository().getPrestamosByAgenciaAnioAndSemanaToDashboard(objectsContainer.getDashboard().getAgencia(), objectsContainer.getDashboard().getAnio(), objectsContainer.getDashboard().getSemana()));
    }

    public void getPagosToCobranza() {
        objectsContainer.setPagosVistaToCobranza(repositoriesContainer.getPagoRepository().getPagosToCobranza(objectsContainer.getDashboard().getAgencia(), objectsContainer.getDashboard().getAnio(), objectsContainer.getDashboard().getSemana()));
    }

    public void getPagosToDashboard() {
        objectsContainer.setPagosVistaToDashboard(repositoriesContainer.getPagoRepository().getPagosToDashboard(objectsContainer.getDashboard().getAgencia(), objectsContainer.getDashboard().getAnio(), objectsContainer.getDashboard().getSemana()));
    }

    public void getLiquidacionesBd() {
        objectsContainer.setLiquidaciones(repositoriesContainer.getLiquidacionRepository().getLiquidacionesToDashboard(objectsContainer.getDashboard().getAgencia(), objectsContainer.getDashboard().getAnio(), objectsContainer.getDashboard().getSemana()));
        objectsContainer.setPagosOfLiquidaciones(repositoriesContainer.getPagoRepository().getPagosOfLiquidacionesToDashboard(objectsContainer.getDashboard().getAgencia(), objectsContainer.getDashboard().getAnio(), objectsContainer.getDashboard().getSemana()));
    }

    public void getAsignaciones() {
        objectsContainer.setAsignaciones(repositoriesContainer.getAsignacionRepository().getAsignacionesToDashboard(objectsContainer.getDashboard().getAgencia(), objectsContainer.getDashboard().getAnio(), objectsContainer.getDashboard().getSemana()));
    }

    public void getAgencia() {
        objectsContainer.getDashboard().setGerencia(objectsContainer.getPrestamosToCobranza().get(0).getGerencia());
    }

    public void getClientes() {
        objectsContainer.getDashboard().setClientes(objectsContainer.getPrestamosToCobranza().size());
    }

    public void getClientesCobrados() {
        objectsContainer.getDashboard().setClientesCobrados(objectsContainer.getPagosVistaToDashboard().size());
    }

    public void getNumeroDeLiquidaciones() {
        objectsContainer.getDashboard().setNumeroLiquidaciones(objectsContainer.getLiquidaciones().size());
    }

    public void getMultas() {

    }

    public void getNoPagos() {
        int noPagos = 0;

        for (int i = 0; i < objectsContainer.getPagosVistaToDashboard().size(); i++) {
            if (objectsContainer.getPagosVistaToDashboard().get(i).getMonto() == 0) {
                noPagos++;
            }
        }
        objectsContainer.getDashboard().setNoPagos(noPagos);
    }

    public void getPagosReducidos() {
        int pagosReducidos = 0;

        for (int i = 0; i < objectsContainer.getPagosVistaToDashboard().size(); i++) {
            if (objectsContainer.getPagosVistaToDashboard().get(i).getAbreCon() < objectsContainer.getPagosVistaToDashboard().get(i).getTarifa()) {
                if (objectsContainer.getPagosVistaToDashboard().get(i).getMonto() < objectsContainer.getPagosVistaToDashboard().get(i).getAbreCon()) {
                    pagosReducidos++;
                }
            } else {
                if (objectsContainer.getPagosVistaToDashboard().get(i).getMonto() < objectsContainer.getPagosVistaToDashboard().get(i).getTarifa()) {
                    pagosReducidos++;
                }
            }
        }

        objectsContainer.getDashboard().setPagosReducidos(pagosReducidos);
    }

    public void getDebitoMiercoles() {
        double debitoMiercoles = 0;

        for (int i = 0; i < objectsContainer.getPrestamosToCobranza().size(); i++) {
            if (objectsContainer.getPrestamosToCobranza().get(i).getDiaDePago().equals("MIERCOLES")) {
                if (objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon() < objectsContainer.getPrestamosToCobranza().get(i).getTarifa())
                    debitoMiercoles += objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon();
                else
                    debitoMiercoles += objectsContainer.getPrestamosToCobranza().get(i).getTarifa();
            }

            objectsContainer.getDashboard().setDebitoMiercoles(debitoMiercoles);
        }
    }

    public void getDebitoJueves() {
        double debitoJueves = 0;

        for (int i = 0; i < objectsContainer.getPrestamosToCobranza().size(); i++) {
            if (objectsContainer.getPrestamosToCobranza().get(i).getDiaDePago().equals("JUEVES")) {
                if (objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon() < objectsContainer.getPrestamosToCobranza().get(i).getTarifa())
                    debitoJueves += objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon();
                else
                    debitoJueves += objectsContainer.getPrestamosToCobranza().get(i).getTarifa();
            }

            objectsContainer.getDashboard().setDebitoJueves(debitoJueves);
        }
    }

    public void getDebitoViernes() {
        double debitoViernes = 0;

        for (int i = 0; i < objectsContainer.getPrestamosToCobranza().size(); i++) {
            if (objectsContainer.getPrestamosToCobranza().get(i).getDiaDePago().equals("VIERNES")) {
                if (objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon() < objectsContainer.getPrestamosToCobranza().get(i).getTarifa())
                    debitoViernes += objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon();
                else
                    debitoViernes += objectsContainer.getPrestamosToCobranza().get(i).getTarifa();
            }

            objectsContainer.getDashboard().setDebitoViernes(debitoViernes);
        }
    }

    public void getDebitoTotal() {
        double debitoTotal = 0;

        for (int i = 0; i < objectsContainer.getPrestamosToCobranza().size(); i++) {
            if (objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon() < objectsContainer.getPrestamosToCobranza().get(i).getTarifa())
                debitoTotal += objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon();
            else
                debitoTotal += objectsContainer.getPrestamosToCobranza().get(i).getTarifa();

            objectsContainer.getDashboard().setDebitoTotal(debitoTotal);
        }
    }

    public void getTotalDeDescuento() {
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

    public void getMontoExcedente() {
        double montoExcedente = 0;

        for (int i = 0; i < objectsContainer.getPagosVistaToDashboard().size(); i++) {
            if (objectsContainer.getPagosVistaToDashboard().get(i).getMonto() > objectsContainer.getPagosVistaToDashboard().get(i).getTarifa()) {
                montoExcedente += objectsContainer.getPagosVistaToDashboard().get(i).getMonto() - objectsContainer.getPagosVistaToDashboard().get(i).getTarifa();
            }
        }

        if (objectsContainer.getDashboard().getLiquidaciones() != null) {
            if (objectsContainer.getDashboard().getLiquidaciones() > 0) {
                montoExcedente -= objectsContainer.getDashboard().getLiquidaciones();
            }
        }

        objectsContainer.getDashboard().setMontoExcedente(montoExcedente);
    }

    public void getLiquidaciones() {
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

    public void getCobranzaTotal() {
        double cobranzaTotal = 0;

        for (int i = 0; i < objectsContainer.getPagosVistaToDashboard().size(); i++) {
            cobranzaTotal += objectsContainer.getPagosVistaToDashboard().get(i).getMonto();
        }

        objectsContainer.getDashboard().setCobranzaTotal(cobranzaTotal);
    }

    public void getTotalCobranzaPura() {
        double totalCobranzaPura = objectsContainer.getDashboard().getCobranzaTotal() - objectsContainer.getDashboard().getMontoExcedente();

        if (objectsContainer.getDashboard().getLiquidaciones() != null) {
            if (objectsContainer.getDashboard().getLiquidaciones() > 0) {
                totalCobranzaPura -= objectsContainer.getDashboard().getLiquidaciones();
            }
        }

        objectsContainer.getDashboard().setTotalCobranzaPura(totalCobranzaPura);

    }

    public void getMontoDeDebitoFaltante() {
        objectsContainer.getDashboard().setMontoDeDebitoFaltante(objectsContainer.getDashboard().getDebitoTotal() - objectsContainer.getDashboard().getTotalCobranzaPura());
    }

    public void getEfectivoEnCampo() {
        double asignaciones = 0;

        if (objectsContainer.getAsignaciones() != null) {
            if (!objectsContainer.getAsignaciones().isEmpty()) {
                for (int i = 0; i < objectsContainer.getAsignaciones().size(); i++) {
                    asignaciones += objectsContainer.getAsignaciones().get(i).getMonto();
                }
            }
        }

        double efectivoEnCampo = objectsContainer.getDashboard().getCobranzaTotal() - asignaciones;

        objectsContainer.getDashboard().setEfectivoEnCampo((double) Math.round(efectivoEnCampo * 100.0) / 100.0);
    }

    public void getRendmiento() {
        double rendimiento = objectsContainer.getDashboard().getTotalCobranzaPura() / objectsContainer.getDashboard().getDebitoTotal() * 100;

        objectsContainer.getDashboard().setRendimiento((Math.round(rendimiento * 100.0) / 100.0));
    }
}
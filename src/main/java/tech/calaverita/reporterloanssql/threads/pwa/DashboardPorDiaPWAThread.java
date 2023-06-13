package tech.calaverita.reporterloanssql.threads.pwa;

import tech.calaverita.reporterloanssql.pojos.ObjectsContainer;
import tech.calaverita.reporterloanssql.services.RepositoriesContainer;

public class DashboardPorDiaPWAThread implements Runnable {
    public RepositoriesContainer repositoriesContainer;
    public ObjectsContainer objectsContainer;
    public int opc;

    public DashboardPorDiaPWAThread(RepositoriesContainer repositoriesContainer, ObjectsContainer objectsContainer, int opc) {
        this.repositoriesContainer = repositoriesContainer;
        this.objectsContainer = objectsContainer;
        this.opc = opc;
    }

    @Override
    public void run() {
        switch (opc) {
            case 0 -> getCalendario();
            case 1 -> getPrestamosToCobranza();
            case 2 -> getPrestamosToDashboard();
            case 3 -> getPagosToCobranza();
            case 4 -> getPagosToDashboard();
            case 5 -> getLiquidacionesBd();
            case 6 -> getAsignaciones();
            case 7 -> getAgencia();
            case 8 -> getClientes();
            case 9 -> getLiquidaciones();
            case 10 -> getNumeroDeLiquidaciones();
            case 11 -> getTotalDeDescuento();
            case 12 -> getClientesCobrados();
            case 13 -> getNoPagos();
            case 14 -> getPagosReducidos();
            case 15 -> getMontoExcedente();
            case 16 -> getCobranzaTotal();
            case 17 -> getTotalCobranzaPura();
            case 18 -> getMontoDeDebitoFaltante();
            case 19 -> getRendmiento();
            case 20 -> getMultas();
        }
    }

    public void getCalendario() {
        objectsContainer.setCalendarioModel(repositoriesContainer.getCalendarioRepository().getSemanaActualXpressByFechaActual(objectsContainer.getFechaPago()));
        objectsContainer.getDashboard().setAnio(objectsContainer.getCalendarioModel().getAnio());
        objectsContainer.getDashboard().setSemana(objectsContainer.getCalendarioModel().getSemana());
    }

    public void getPrestamosToCobranza() {
        objectsContainer.setPrestamosToCobranza(repositoriesContainer.getPrestamoRepository().getPrestamosByAgenciaAnioAndSemanaToCobranza(objectsContainer.getDashboard().getAgencia(), objectsContainer.getCalendarioModel().getAnio(), objectsContainer.getCalendarioModel().getSemana()));
    }

    public void getPrestamosToDashboard() {
        objectsContainer.setPrestamosToDashboard(repositoriesContainer.getPrestamoRepository().getPrestamosByAgenciaAndFechaPagoToDashboard(objectsContainer.getDashboard().getAgencia(), objectsContainer.getFechaPago()));
    }

    public void getPagosToCobranza() {
        objectsContainer.setPagosVistaToCobranza(repositoriesContainer.getPagoRepository().getPagosByAgenciaAnioAndSemanaToCobranza(objectsContainer.getDashboard().getAgencia(), objectsContainer.getDashboard().getAnio(), objectsContainer.getDashboard().getSemana()));
    }

    public void getPagosToDashboard() {
        objectsContainer.setPagoModelsToDashboard(repositoriesContainer.getPagoRepository().getPagosByAgenciaAndFechaPagoToDashboard(objectsContainer.getDashboard().getAgencia(), objectsContainer.getFechaPago()));
    }

    public void getLiquidacionesBd() {
        objectsContainer.setLiquidaciones(repositoriesContainer.getLiquidacionRepository().getLiquidacionesByAgenciaAndFechaPagoToDashboard(objectsContainer.getDashboard().getAgencia(), objectsContainer.getFechaPago()));
        objectsContainer.setPagosOfLiquidaciones(repositoriesContainer.getPagoRepository().getPagosOfLiquidacionesByAgenciaAndFechaPagoToDashboard(objectsContainer.getDashboard().getAgencia(), objectsContainer.getFechaPago()));
    }

    public void getAsignaciones() {
        objectsContainer.setAsignaciones(repositoriesContainer.getAsignacionRepository().getAsignacionesToDashboard(objectsContainer.getDashboard().getAgencia(), objectsContainer.getDashboard().getAnio(), objectsContainer.getDashboard().getSemana()));
    }

    public void getAgencia() {
        objectsContainer.getDashboard().setGerencia(objectsContainer.getPrestamosToCobranza().get(0).getGerencia());
    }

    public void getClientes() {
        switch (objectsContainer.getDia()) {
            case "miércoles" -> objectsContainer.getDashboard().setClientes(getClientesMiercoles());
            case "jueves" -> objectsContainer.getDashboard().setClientes(getClientesJueves());
            case "viernes" -> objectsContainer.getDashboard().setClientes(getClientesViernes());
            default -> objectsContainer.getDashboard().setClientes(null);
        }
    }

    public void getClientesCobrados() {
        objectsContainer.getDashboard().setClientesCobrados(objectsContainer.getPrestamosToDashboard().size());
    }

    public void getNumeroDeLiquidaciones() {
        if (objectsContainer.getLiquidaciones().isEmpty()) {
            objectsContainer.getDashboard().setNumeroLiquidaciones(0);
        } else {
            objectsContainer.getDashboard().setNumeroLiquidaciones(objectsContainer.getLiquidaciones().size());
        }
    }

    public void getMultas() {
        objectsContainer.getDashboard().setMultas(0.0);
    }

    public void getNoPagos() {
        int noPagos = 0;

        for (int i = 0; i < objectsContainer.getPagoModelsToDashboard().size(); i++) {
            if (objectsContainer.getPagoModelsToDashboard().get(i).getMonto() == 0) {
                noPagos++;
            }
        }
        objectsContainer.getDashboard().setNoPagos(noPagos);
    }

    public void getPagosReducidos() {
        int pagosReducidos = 0;

        for (int i = 0; i < objectsContainer.getPagoModelsToDashboard().size(); i++) {
            if (objectsContainer.getPagoModelsToDashboard().get(i).getAbreCon() < objectsContainer.getPagoModelsToDashboard().get(i).getTarifa()) {
                if (objectsContainer.getPagoModelsToDashboard().get(i).getMonto() < objectsContainer.getPagoModelsToDashboard().get(i).getAbreCon()) {
                    pagosReducidos++;
                }
            } else {
                if (objectsContainer.getPagoModelsToDashboard().get(i).getMonto() < objectsContainer.getPagoModelsToDashboard().get(i).getTarifa()) {
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

        for (int i = 0; i < objectsContainer.getPagoModelsToDashboard().size(); i++) {
            if (objectsContainer.getPagoModelsToDashboard().get(i).getMonto() > objectsContainer.getPagoModelsToDashboard().get(i).getTarifa()) {
                montoExcedente += objectsContainer.getPagoModelsToDashboard().get(i).getMonto() - objectsContainer.getPagoModelsToDashboard().get(i).getTarifa();
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

        for (int i = 0; i < objectsContainer.getPagoModelsToDashboard().size(); i++) {
            cobranzaTotal += objectsContainer.getPagoModelsToDashboard().get(i).getMonto();
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
        if (objectsContainer.getDashboard().getDebitoTotal() != null && objectsContainer.getDashboard().getDebitoTotal() > 0) {
            objectsContainer.getDashboard().setMontoDeDebitoFaltante(objectsContainer.getDashboard().getDebitoTotal() - objectsContainer.getDashboard().getTotalCobranzaPura());
        }
    }

    public void getRendmiento() {
        if (objectsContainer.getDashboard().getDebitoTotal() != null && objectsContainer.getDashboard().getDebitoTotal() > 0) {
            double rendimiento = objectsContainer.getDashboard().getTotalCobranzaPura() / objectsContainer.getDashboard().getDebitoTotal() * 100;

            objectsContainer.getDashboard().setRendimiento((Math.round(rendimiento * 100.0) / 100.0));
        }
    }
}
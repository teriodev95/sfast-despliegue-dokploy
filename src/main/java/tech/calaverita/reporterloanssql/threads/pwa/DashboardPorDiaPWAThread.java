package tech.calaverita.reporterloanssql.threads.pwa;

import tech.calaverita.reporterloanssql.pojos.ObjectsContainer;
import tech.calaverita.reporterloanssql.services.*;

public class DashboardPorDiaPWAThread implements Runnable {
    public ObjectsContainer objectsContainer;
    public int opc;
    public Thread[] threads;

    public DashboardPorDiaPWAThread(ObjectsContainer objectsContainer, int opc, Thread[] threads) {
        this.objectsContainer = objectsContainer;
        this.opc = opc;
        this.threads = threads;
    }

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
        objectsContainer.setCalendarioModel(CalendarioService.getSemanaActualXpressByFechaActual(objectsContainer.getFechaPago()));
        objectsContainer.getDashboard().setAnio(objectsContainer.getCalendarioModel().getAnio());
        objectsContainer.getDashboard().setSemana(objectsContainer.getCalendarioModel().getSemana());
    }

    public void setPrestamosToCobranza() {
        try {
            threads[5].join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        objectsContainer.setPrestamosToCobranza(PrestamoService.getPrestamoModelsByAgenciaAnioAndSemanaToCobranzaPGS(objectsContainer.getDashboard().getAgencia(), objectsContainer.getCalendarioModel().getAnio(), objectsContainer.getCalendarioModel().getSemana()));

        setGerencia();
        setClientes();
    }

    public void setPrestamosToDashboard() {
        objectsContainer.setPrestamosToDashboard(PrestamoService.getPrestamoModelsByAgenciaAndFechaPagoToDashboard(objectsContainer.getDashboard().getAgencia(), objectsContainer.getFechaPago()));
    }

    public void setPagosToCobranza() {
        try {
            threads[5].join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        objectsContainer.setPagosVistaToCobranza(PagoService.getPagoUtilModelsByAgenciaAnioAndSemanaToCobranza(objectsContainer.getDashboard().getAgencia(), objectsContainer.getDashboard().getAnio(), objectsContainer.getDashboard().getSemana()));
    }

    public void setPagosToDashboard() {
        objectsContainer.setPagoModelsToDashboard(PagoService.getPagoModelsByAgenciaAndFechaPagoToDashboard(objectsContainer.getDashboard().getAgencia(), objectsContainer.getFechaPago()));

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
        objectsContainer.setLiquidaciones(LiquidacionService.getLiquidacionModelsByAgenciaAndFechaPagoToDashboard(objectsContainer.getDashboard().getAgencia(), objectsContainer.getFechaPago()));
        objectsContainer.setPagosOfLiquidaciones(PagoService.getPagosOfLiquidacionesByAgenciaAndFechaPagoToDashboard(objectsContainer.getDashboard().getAgencia(), objectsContainer.getFechaPago()));

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

        objectsContainer.setAsignaciones(AsignacionService.getAsignacionesByAgenciaAnioAndSemanaToDashboard(objectsContainer.getDashboard().getAgencia(), objectsContainer.getDashboard().getAnio(), objectsContainer.getDashboard().getSemana()));
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

        for (int i = 0; i < objectsContainer.getPagoModelsToDashboard().size(); i++) {
            if (objectsContainer.getPagoModelsToDashboard().get(i).getMonto() == 0) {
                noPagos++;
            }
        }
        objectsContainer.getDashboard().setNoPagos(noPagos);
    }

    public void setPagosReducidos() {
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

        for (int i = 0; i < objectsContainer.getPagoModelsToDashboard().size(); i++) {
            cobranzaTotal += objectsContainer.getPagoModelsToDashboard().get(i).getMonto();
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
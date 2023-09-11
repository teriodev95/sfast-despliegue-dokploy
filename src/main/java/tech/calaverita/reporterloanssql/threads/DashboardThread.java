package tech.calaverita.reporterloanssql.threads;

import tech.calaverita.reporterloanssql.pojos.ObjectsContainer;
import tech.calaverita.reporterloanssql.services.AsignacionService;
import tech.calaverita.reporterloanssql.services.LiquidacionService;
import tech.calaverita.reporterloanssql.services.PagoService;
import tech.calaverita.reporterloanssql.services.PrestamoService;

public class DashboardThread implements Runnable {
    public ObjectsContainer objectsContainer;
    public int opc;
    private Thread[] threads;

    public DashboardThread(ObjectsContainer objectsContainer, int opc) {
        this.objectsContainer = objectsContainer;
        this.opc = opc;
    }

    public DashboardThread(ObjectsContainer objectsContainer, int opc, Thread[] threads) {
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
            case 5 -> setPagosOfLiquidaciones();
            case 6 -> setAsignaciones();
        }
    }

    public void setPrestamosToCobranza() {
        objectsContainer.setPrestamosToCobranza(PrestamoService.getPrestamoModelsByAgenciaAnioAndSemanaToCobranzaPGS(objectsContainer.getDashboard().getAgencia(), objectsContainer.getDashboard().getAnio(), objectsContainer.getDashboard().getSemana()));

        setGerencia();
        setClientes();
    }

    public void setPrestamosToDashboard() {
        objectsContainer.setPrestamosToDashboard(PrestamoService.getPrestamoModelsByAgenciaAnioAndSemanaToDashboard(objectsContainer.getDashboard().getAgencia(), objectsContainer.getDashboard().getAnio(), objectsContainer.getDashboard().getSemana()));
    }

    public void setPagosToCobranza() {
        objectsContainer.setPagosVistaToCobranza(PagoService.getPagoUtilModelsByAgenciaAnioAndSemanaToCobranza(objectsContainer.getDashboard().getAgencia(), objectsContainer.getDashboard().getAnio(), objectsContainer.getDashboard().getSemana()));

        try {
            threads[0].join();
        } catch (InterruptedException e) {
            System.out.println("Ha fallado el seteoDeDebitos " + e);
        }

        setDebitoMiercoles();
        setDebitoJueves();
        setDebitoViernes();
        setDebitoTotal();
    }

    public void setPagosToDashboard() {
        objectsContainer.setPagosVistaToDashboard(PagoService.getPagoUtilModelsByAgenciaAnioAndSemanaToDashboard(objectsContainer.getDashboard().getAgencia(), objectsContainer.getDashboard().getAnio(), objectsContainer.getDashboard().getSemana()));

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
        objectsContainer.setLiquidaciones(LiquidacionService.getLiquidacionModelsByAgenciaAnioAndSemanaToDashboard(objectsContainer.getDashboard().getAgencia(), objectsContainer.getDashboard().getAnio(), objectsContainer.getDashboard().getSemana()));

        setNumeroDeLiquidaciones();
        setTotalDeDescuento();

        try {
            threads[5].join();
        } catch (InterruptedException e) {
            System.out.println("Ha fallado el setLiquidaciones " + e);
        }

        setLiquidaciones();
    }

    public void setPagosOfLiquidaciones() {
        objectsContainer.setPagosOfLiquidaciones(PagoService.getPagoModelsOfLiquidacionesByAgenciaAnioAndSemanaToDashboard(objectsContainer.getDashboard().getAgencia(), objectsContainer.getDashboard().getAnio(), objectsContainer.getDashboard().getSemana()));
    }

    public void setAsignaciones() {
        objectsContainer.setAsignaciones(AsignacionService.getAsignacionesByAgenciaAnioAndSemanaToDashboard(objectsContainer.getDashboard().getAgencia(), objectsContainer.getDashboard().getAnio(), objectsContainer.getDashboard().getSemana()));

        try {
            threads[3].join();

        } catch (InterruptedException e) {
            System.out.println("Ha fallado el setEfectivoEnCampo " + e);
        }

        setEfectivoEnCampo();
    }

    public void setGerencia() {
        objectsContainer.getDashboard().setGerencia(objectsContainer.getPrestamosToCobranza().get(0).getGerencia());
    }

    public void setClientes() {
        objectsContainer.getDashboard().setClientes(objectsContainer.getPrestamosToCobranza().size());
    }

    public void setClientesCobrados() {
        objectsContainer.getDashboard().setClientesCobrados(objectsContainer.getPagosVistaToDashboard().size());
    }

    public void setNumeroDeLiquidaciones() {
        objectsContainer.getDashboard().setNumeroLiquidaciones(objectsContainer.getLiquidaciones().size());
    }

    public void setMultas() {
        objectsContainer.getDashboard().setMultas(0.0);
    }

    public void setNoPagos() {
        int noPagos = 0;

        for (int i = 0; i < objectsContainer.getPagosVistaToDashboard().size(); i++) {
            if (objectsContainer.getPagosVistaToDashboard().get(i).getMonto() == 0) {
                noPagos++;
            }
        }
        objectsContainer.getDashboard().setNoPagos(noPagos);
    }

    public void setPagosReducidos() {
        int pagosReducidos = 0;

        for (int i = 0; i < objectsContainer.getPagosVistaToDashboard().size(); i++) {
            if (objectsContainer.getPagosVistaToDashboard().get(i).getMonto() != 0) {
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
        }

        objectsContainer.getDashboard().setPagosReducidos(pagosReducidos);
    }

    public void setDebitoMiercoles() {
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

    public void setDebitoJueves() {
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

    public void setDebitoViernes() {
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

    public void setDebitoTotal() {
        double debitoTotal = 0;

        for (int i = 0; i < objectsContainer.getPrestamosToCobranza().size(); i++) {
            if (objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon() < objectsContainer.getPrestamosToCobranza().get(i).getTarifa())
                debitoTotal += objectsContainer.getPagosVistaToCobranza().get(i).getCierraCon();
            else
                debitoTotal += objectsContainer.getPrestamosToCobranza().get(i).getTarifa();

            objectsContainer.getDashboard().setDebitoTotal(debitoTotal);
        }
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

        for (int i = 0; i < objectsContainer.getPagosVistaToDashboard().size(); i++) {
            cobranzaTotal += objectsContainer.getPagosVistaToDashboard().get(i).getMonto();
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
        objectsContainer.getDashboard().setMontoDeDebitoFaltante(objectsContainer.getDashboard().getDebitoTotal() - objectsContainer.getDashboard().getTotalCobranzaPura());
    }

    public void setEfectivoEnCampo() {


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

    public void setRendmiento() {
        double rendimiento = objectsContainer.getDashboard().getTotalCobranzaPura() / objectsContainer.getDashboard().getDebitoTotal() * 100;

        objectsContainer.getDashboard().setRendimiento((Math.round(rendimiento * 100.0) / 100.0));
    }
}
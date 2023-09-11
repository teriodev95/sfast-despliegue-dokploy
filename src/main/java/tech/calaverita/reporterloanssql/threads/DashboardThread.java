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
            case 0 -> getPrestamosToCobranza();
            case 1 -> getPrestamosToDashboard();
            case 2 -> getPagosToCobranza();
            case 3 -> getPagosToDashboard();
            case 4 -> getLiquidacionesBd();
            case 5 -> getPagosOfLiquidaciones();
            case 6 -> getAsignaciones();
            case 7 -> getGerencia();
            case 8 -> getClientes();
            case 9 -> getDebitoMiercoles();
            case 10 -> getDebitoJueves();
            case 11 -> getDebitoViernes();
            case 12 -> getDebitoTotal();
            case 13 -> getLiquidaciones();
            case 14 -> getNumeroDeLiquidaciones();
            case 15 -> getTotalDeDescuento();
            case 16 -> getClientesCobrados();
            case 17 -> getNoPagos();
            case 18 -> getPagosReducidos();
            case 19 -> getMontoExcedente();
            case 20 -> getCobranzaTotal();
            case 21 -> getTotalCobranzaPura();
            case 22 -> getEfectivoEnCampo();
            case 23 -> getRendmiento();
            case 24 -> getMontoDeDebitoFaltante();
            case 25 -> getMultas();
        }
    }

    public void getPrestamosToCobranza() {
        objectsContainer.setPrestamosToCobranza(PrestamoService.getPrestamoModelsByAgenciaAnioAndSemanaToCobranzaPGS(objectsContainer.getDashboard().getAgencia(), objectsContainer.getDashboard().getAnio(), objectsContainer.getDashboard().getSemana()));
    }

    public void getPrestamosToDashboard() {
        objectsContainer.setPrestamosToDashboard(PrestamoService.getPrestamoModelsByAgenciaAnioAndSemanaToDashboard(objectsContainer.getDashboard().getAgencia(), objectsContainer.getDashboard().getAnio(), objectsContainer.getDashboard().getSemana()));
    }

    public void getPagosToCobranza() {
        objectsContainer.setPagosVistaToCobranza(PagoService.getPagoUtilModelsByAgenciaAnioAndSemanaToCobranza(objectsContainer.getDashboard().getAgencia(), objectsContainer.getDashboard().getAnio(), objectsContainer.getDashboard().getSemana()));
    }

    public void getPagosToDashboard() {
        objectsContainer.setPagosVistaToDashboard(PagoService.getPagoUtilModelsByAgenciaAnioAndSemanaToDashboard(objectsContainer.getDashboard().getAgencia(), objectsContainer.getDashboard().getAnio(), objectsContainer.getDashboard().getSemana()));
    }

    public void getLiquidacionesBd() {
        objectsContainer.setLiquidaciones(LiquidacionService.getLiquidacionModelsByAgenciaAnioAndSemanaToDashboard(objectsContainer.getDashboard().getAgencia(), objectsContainer.getDashboard().getAnio(), objectsContainer.getDashboard().getSemana()));
    }

    public void getPagosOfLiquidaciones() {
        objectsContainer.setPagosOfLiquidaciones(PagoService.getPagoModelsOfLiquidacionesByAgenciaAnioAndSemanaToDashboard(objectsContainer.getDashboard().getAgencia(), objectsContainer.getDashboard().getAnio(), objectsContainer.getDashboard().getSemana()));
    }

    public void getAsignaciones() {
        objectsContainer.setAsignaciones(AsignacionService.getAsignacionesToDashboard(objectsContainer.getDashboard().getAgencia(), objectsContainer.getDashboard().getAnio(), objectsContainer.getDashboard().getSemana()));
    }

    public void getGerencia() {
        try {
            threads[0].join();
        } catch (InterruptedException e) {
            System.out.println("Ha fallado el getAgencia " + e);
        }

        objectsContainer.getDashboard().setGerencia(objectsContainer.getPrestamosToCobranza().get(0).getGerencia());
    }

    public void getClientes() {
        try {
            threads[0].join();
        } catch (InterruptedException e) {
            System.out.println("Ha fallado el getClientes " + e);
        }
        objectsContainer.getDashboard().setClientes(objectsContainer.getPrestamosToCobranza().size());
    }

    public void getClientesCobrados() {
        try {
            threads[3].join();
        } catch (InterruptedException e) {
            System.out.println("Ha fallado el getClientesCobrados " + e);
        }
        objectsContainer.getDashboard().setClientesCobrados(objectsContainer.getPagosVistaToDashboard().size());
    }

    public void getNumeroDeLiquidaciones() {
        try {
            threads[4].join();
        } catch (InterruptedException e) {
            System.out.println("Ha fallado el getNumeroDeLiquidaciones " + e);
        }

        objectsContainer.getDashboard().setNumeroLiquidaciones(objectsContainer.getLiquidaciones().size());
    }

    public void getMultas() {
        try {
            threads[0].join();
        } catch (InterruptedException e) {
            System.out.println("Ha fallado el getMultas " + e);
        }

        objectsContainer.getDashboard().setMultas(0.0);
    }

    public void getNoPagos() {
        try {
            threads[3].join();
        } catch (InterruptedException e) {
            System.out.println("Ha fallado el getNoPagos " + e);
        }

        int noPagos = 0;

        for (int i = 0; i < objectsContainer.getPagosVistaToDashboard().size(); i++) {
            if (objectsContainer.getPagosVistaToDashboard().get(i).getMonto() == 0) {
                noPagos++;
            }
        }
        objectsContainer.getDashboard().setNoPagos(noPagos);
    }

    public void getPagosReducidos() {
        try {
            threads[3].join();
        } catch (InterruptedException e) {
            System.out.println("Ha fallado el getPagosReducidos " + e);
        }

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

    public void getDebitoMiercoles() {
        try {
            threads[0].join();
            threads[2].join();

        } catch (InterruptedException e) {
            System.out.println("Ha fallado el getDebitoMiercoles " + e);
        }

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
        try {
            threads[0].join();
            threads[2].join();

        } catch (InterruptedException e) {
            System.out.println("Ha fallado el getDebitoJueves " + e);
        }

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
        try {
            threads[0].join();
            threads[2].join();

        } catch (InterruptedException e) {
            System.out.println("Ha fallado el getDebitoViernes " + e);
        }

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
        try {
            threads[0].join();
            threads[2].join();

        } catch (InterruptedException e) {
            System.out.println("Ha fallado el getDebitoTotal " + e);
        }

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
        try {
            threads[4].join();

        } catch (InterruptedException e) {
            System.out.println("Ha fallado el getTotalDeDescuento " + e);
        }

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
        try {
            threads[3].join();
            threads[4].join();
            threads[13].join();

        } catch (InterruptedException e) {
            System.out.println("Ha fallado el getMontoExcedente " + e);
        }

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
        try {
            threads[4].join();
            threads[5].join();
        } catch (InterruptedException e) {
            System.out.println("Ha fallado el getLiquidaciones " + e);
        }

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
        try {
            threads[3].join();

        } catch (InterruptedException e) {
            System.out.println("Ha fallado el getCobranzaTotal " + e);
        }

        double cobranzaTotal = 0;

        for (int i = 0; i < objectsContainer.getPagosVistaToDashboard().size(); i++) {
            cobranzaTotal += objectsContainer.getPagosVistaToDashboard().get(i).getMonto();
        }

        objectsContainer.getDashboard().setCobranzaTotal(cobranzaTotal);
    }

    public void getTotalCobranzaPura() {
        try {
            threads[13].join();
            threads[19].join();
            threads[20].join();

        } catch (InterruptedException e) {
            System.out.println("Ha fallado el getTotalCobranzaPura " + e);
        }

        double totalCobranzaPura = objectsContainer.getDashboard().getCobranzaTotal() - objectsContainer.getDashboard().getMontoExcedente();

        if (objectsContainer.getDashboard().getLiquidaciones() != null) {
            if (objectsContainer.getDashboard().getLiquidaciones() > 0) {
                totalCobranzaPura -= objectsContainer.getDashboard().getLiquidaciones();
            }
        }

        objectsContainer.getDashboard().setTotalCobranzaPura(totalCobranzaPura);

    }

    public void getMontoDeDebitoFaltante() {
        try {
            threads[12].join();
            threads[21].join();

        } catch (InterruptedException e) {
            System.out.println("Ha fallado el getMontoDeDebitoFaltante " + e);
        }

        objectsContainer.getDashboard().setMontoDeDebitoFaltante(objectsContainer.getDashboard().getDebitoTotal() - objectsContainer.getDashboard().getTotalCobranzaPura());
    }

    public void getEfectivoEnCampo() {
        try {
            threads[6].join();
            threads[20].join();

        } catch (InterruptedException e) {
            System.out.println("Ha fallado el getEfectivoEnCampo " + e);
        }

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
        try {
            threads[12].join();
            threads[21].join();

        } catch (InterruptedException e) {
            System.out.println("Ha fallado el getRendimiento " + e);
        }

        double rendimiento = objectsContainer.getDashboard().getTotalCobranzaPura() / objectsContainer.getDashboard().getDebitoTotal() * 100;

        objectsContainer.getDashboard().setRendimiento((Math.round(rendimiento * 100.0) / 100.0));
    }
}
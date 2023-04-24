package tech.calaverita.reporterloanssql.threads;

import tech.calaverita.reporterloanssql.pojos.Dashboard;
import tech.calaverita.reporterloanssql.pojos.Models;
import tech.calaverita.reporterloanssql.pojos.Repositories;

public class DashboardThread implements Runnable {
    public Repositories repositories;
    public Models models;
    public Dashboard dashboard;
    public int opc;

    public DashboardThread(Repositories repositories, Models models, int opc) {
        this.repositories = repositories;
        this.models = models;
        this.opc = opc;
    }

    @Override
    public void run() {
        switch (opc) {
            case 0:
                getPrestamosToCobranza();
                break;
            case 1:
                getPrestamosToDashboard();
                break;
            case 2:
                getPagosToCobranza();
                break;
            case 3:
                getPagosToDashboard();
                break;
            case 4:
                getLiquidacionesBd();
                break;
            case 5:
                getAsignaciones();
                break;
            case 6:
                getAgencia();
                break;
            case 7:
                getClientes();
                break;
            case 8:
                getDebitoMiercoles();
                break;
            case 9:
                getDebitoJueves();
                break;
            case 10:
                getDebitoViernes();
                break;
            case 11:
                getDebitoTotal();
                break;
            case 12:
                getLiquidaciones();
                break;
            case 13:
                getNumeroDeLiquidaciones();
                break;
            case 14:
                getTotalDeDescuento();
                break;
            case 15:
                getClientesCobrados();
                break;
            case 16:
                getNoPagos();
                break;
            case 17:
                getPagosReducidos();
                break;
            case 18:
                getMontoExcedente();
                break;
            case 19:
                getCobranzaTotal();
                break;
            case 20:
                getTotalCobranzaPura();
                break;
            case 21:
                getEfectivoEnCampo();
                break;
            case 22:
                getRendmiento();
                break;
            case 23:
                getMontoDeDebitoFaltante();
                break;
        }
    }

    public void getPrestamosToCobranza() {
        models.setPrestamosToCobranza(repositories.getPrestamoRepository().getPrestamosToCobranza(models.getDashboard().getAgencia(), models.getDashboard().getAnio(), models.getDashboard().getSemana()));
    }

    public void getPrestamosToDashboard() {
        models.setPrestamosToDashboard(repositories.getPrestamoRepository().getPrestamosToDashboard(models.getDashboard().getAgencia(), models.getDashboard().getAnio(), models.getDashboard().getSemana()));
    }

    public void getPagosToCobranza() {
        models.setPagosVistaToCobranza(repositories.getPagoRepository().getPagosToCobranza(models.getDashboard().getAgencia(), models.getDashboard().getAnio(), models.getDashboard().getSemana()));
    }

    public void getPagosToDashboard() {
        models.setPagosVistaToDashboard(repositories.getPagoRepository().getPagosToDashboard(models.getDashboard().getAgencia(), models.getDashboard().getAnio(), models.getDashboard().getSemana()));
    }

    public void getLiquidacionesBd() {
        models.setLiquidaciones(repositories.getLiquidacionRepository().getLiquidacionesToDashboard(models.getDashboard().getAgencia(), models.getDashboard().getAnio(), models.getDashboard().getSemana()));
        models.setPagosOfLiquidaciones(repositories.getPagoRepository().getPagosOfLiquidacionesToDashboard(models.getDashboard().getAgencia(), models.getDashboard().getAnio(), models.getDashboard().getSemana()));
    }

    public void getAsignaciones() {
        models.setAsignaciones(repositories.getAsignacionRepository().getAsignacionesToDashboard(models.getDashboard().getAgencia(), models.getDashboard().getAnio(), models.getDashboard().getSemana()));
    }

    public void getAgencia() {
        models.getDashboard().setGerencia(models.getPrestamosToCobranza().get(0).getGerencia());
    }

    public void getClientes() {
        models.getDashboard().setClientes(models.getPrestamosToCobranza().size());
    }

    public void getClientesCobrados() {
        models.getDashboard().setClientesCobrados(models.getPagosVistaToDashboard().size());
    }

    public void getNumeroDeLiquidaciones() {
        models.getDashboard().setNumeroLiquidaciones(models.getLiquidaciones().size());
    }

    public void getMultas() {

    }

    public void getNoPagos() {
        int noPagos = 0;

        for (int i = 0; i < models.getPagosVistaToDashboard().size(); i++) {
            if (models.getPagosVistaToDashboard().get(i).getMonto() == 0) {
                noPagos++;
            }
        }
        models.getDashboard().setNoPagos(noPagos);
    }

    public void getPagosReducidos() {
        int pagosReducidos = 0;

        for (int i = 0; i < models.getPagosVistaToDashboard().size(); i++) {
            if (models.getPagosVistaToDashboard().get(i).getAbreCon() < models.getPagosVistaToDashboard().get(i).getTarifa()) {
                if (models.getPagosVistaToDashboard().get(i).getMonto() < models.getPagosVistaToDashboard().get(i).getAbreCon()) {
                    pagosReducidos++;
                }
            } else {
                if (models.getPagosVistaToDashboard().get(i).getMonto() < models.getPagosVistaToDashboard().get(i).getTarifa()) {
                    pagosReducidos++;
                }
            }
        }

        models.getDashboard().setPagosReducidos(pagosReducidos);
    }

    public void getDebitoMiercoles() {
        double debitoMiercoles = 0;

        for (int i = 0; i < models.getPrestamosToCobranza().size(); i++) {
            if (models.getPrestamosToCobranza().get(i).getDiaDePago().equals("MIERCOLES")) {
                if (models.getPagosVistaToCobranza().get(i).getCierraCon() < models.getPrestamosToCobranza().get(i).getTarifa())
                    debitoMiercoles += models.getPagosVistaToCobranza().get(i).getCierraCon();
                else
                    debitoMiercoles += models.getPrestamosToCobranza().get(i).getTarifa();
            }

            models.getDashboard().setDebitoMiercoles(debitoMiercoles);
        }
    }

    public void getDebitoJueves() {
        double debitoJueves = 0;

        for (int i = 0; i < models.getPrestamosToCobranza().size(); i++) {
            if (models.getPrestamosToCobranza().get(i).getDiaDePago().equals("JUEVES")) {
                if (models.getPagosVistaToCobranza().get(i).getCierraCon() < models.getPrestamosToCobranza().get(i).getTarifa())
                    debitoJueves += models.getPagosVistaToCobranza().get(i).getCierraCon();
                else
                    debitoJueves += models.getPrestamosToCobranza().get(i).getTarifa();
            }

            models.getDashboard().setDebitoJueves(debitoJueves);
        }
    }

    public void getDebitoViernes() {
        double debitoViernes = 0;

        for (int i = 0; i < models.getPrestamosToCobranza().size(); i++) {
            if (models.getPrestamosToCobranza().get(i).getDiaDePago().equals("VIERNES")) {
                if (models.getPagosVistaToCobranza().get(i).getCierraCon() < models.getPrestamosToCobranza().get(i).getTarifa())
                    debitoViernes += models.getPagosVistaToCobranza().get(i).getCierraCon();
                else
                    debitoViernes += models.getPrestamosToCobranza().get(i).getTarifa();
            }

            models.getDashboard().setDebitoViernes(debitoViernes);
        }
    }

    public void getDebitoTotal() {
        double debitoTotal = 0;

        for (int i = 0; i < models.getPrestamosToCobranza().size(); i++) {
            if (models.getPagosVistaToCobranza().get(i).getCierraCon() < models.getPrestamosToCobranza().get(i).getTarifa())
                debitoTotal += models.getPagosVistaToCobranza().get(i).getCierraCon();
            else
                debitoTotal += models.getPrestamosToCobranza().get(i).getTarifa();

            models.getDashboard().setDebitoTotal(debitoTotal);
        }
    }

    public void getTotalDeDescuento() {
        double totalDeDescuento = 0;

        if (models.getLiquidaciones() != null) {
            if (!models.getLiquidaciones().isEmpty()) {
                for (int i = 0; i < models.getLiquidaciones().size(); i++) {
                    totalDeDescuento += models.getLiquidaciones().get(i).getDescuentoEnDinero();
                }
            }
        }

        models.getDashboard().setTotalDeDescuento(totalDeDescuento);
    }

    public void getMontoExcedente() {
        double montoExcedente = 0;

        for (int i = 0; i < models.getPagosVistaToDashboard().size(); i++) {
            if (models.getPagosVistaToDashboard().get(i).getMonto() > models.getPagosVistaToDashboard().get(i).getTarifa()) {
                montoExcedente += models.getPagosVistaToDashboard().get(i).getMonto() - models.getPagosVistaToDashboard().get(i).getTarifa();
            }
        }

        models.getDashboard().setMontoExcedente(montoExcedente);
    }

    public void getLiquidaciones() {
        double liquidaciones = 0;

        if (models.getLiquidaciones() != null) {
            if (!models.getLiquidaciones().isEmpty()) {
                for (int i = 0; i < models.getLiquidaciones().size(); i++) {
                    liquidaciones += models.getLiquidaciones().get(i).getLiquidoCon() - models.getPagosOfLiquidaciones().get(i).getTarifa();
                }
            }
        }

        models.getDashboard().setLiquidaciones(liquidaciones);
    }

    public void getCobranzaTotal() {
        double cobranzaTotal = 0;

        for (int i = 0; i < models.getPagosVistaToDashboard().size(); i++) {
            cobranzaTotal += models.getPagosVistaToDashboard().get(i).getMonto();
        }

        models.getDashboard().setCobranzaTotal(cobranzaTotal);
    }

    public void getTotalCobranzaPura() {
        models.getDashboard().setTotalCobranzaPura(models.getDashboard().getCobranzaTotal() - models.getDashboard().getMontoExcedente());
    }

    public void getMontoDeDebitoFaltante() {
        models.getDashboard().setMontoDeDebitoFaltante(models.getDashboard().getDebitoTotal() - models.getDashboard().getTotalCobranzaPura());
    }

    public void getEfectivoEnCampo() {
        double asignaciones = 0;

        if (models.getAsignaciones() != null) {
            if (!models.getAsignaciones().isEmpty()) {
                for (int i = 0; i < models.getAsignaciones().size(); i++) {
                    asignaciones += models.getAsignaciones().get(i).getMonto();
                }
            }
        }

        double efectivoEnCampo = models.getDashboard().getCobranzaTotal() - asignaciones;

        models.getDashboard().setEfectivoEnCampo((double) Math.round(efectivoEnCampo * 100.0) / 100.0);
    }

    public void getRendmiento() {
        double rendimiento = models.getDashboard().getTotalCobranzaPura() / models.getDashboard().getDebitoTotal() * 100;

        models.getDashboard().setRendimiento((Math.round(rendimiento * 100.0) / 100.0));
    }
}
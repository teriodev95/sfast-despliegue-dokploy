package tech.calaverita.reporterloanssql.threads;

import tech.calaverita.reporterloanssql.pojos.Cobranza;
import tech.calaverita.reporterloanssql.pojos.Models;
import tech.calaverita.reporterloanssql.pojos.Repositories;

public class CobranzaThread implements Runnable {
    public Repositories repositories;
    public Models models;
    //    public CobranzaUtil cobranzaUtil;
//    public DebitoUtil debitoUtil;
    public Cobranza cobranza;
    public int opc;

    public CobranzaThread(Repositories repositories, Models models, int ope) {
        this.repositories = repositories;
        this.models = models;
        this.opc = ope;
    }

//    public CobranzaThread(CobranzaUtil cobranzaUtil, DebitoUtil debitoUtil, int ope, String filtro) {
//        this.cobranzaUtil = cobranzaUtil;
//        this.debitoUtil = debitoUtil;
//        this.ope = ope;
//        this.filtro = filtro;
//    }

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
        models.getCobranza().setGerencia(models.getPrestamosToCobranza().get(0).getGerencia());
    }

    public void getClientes() {
        models.getCobranza().setClientes(models.getPrestamosToCobranza().size());
    }

    public void getPrestamos() {
        models.setPrestamosToCobranza(repositories.getPrestamoRepository().getPrestamosToCobranza(models.getCobranza().getAgencia(), models.getCobranza().getAnio(), models.getCobranza().getSemana()));

        models.getCobranza().setPrestamos(models.getPrestamosToCobranza());
    }

    public void getPagos() {
        models.setPagosVistaToCobranza(repositories.getPagoRepository().getPagosToCobranza(models.getCobranza().getAgencia(), models.getCobranza().getAnio(), models.getCobranza().getSemana()));
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

            models.getCobranza().setDebitoMiercoles(debitoMiercoles);
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

            models.getCobranza().setDebitoJueves(debitoJueves);
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

            models.getCobranza().setDebitoViernes(debitoViernes);
        }
    }

    public void getDebitoTotal() {
        double debitoTotal = 0;

        for (int i = 0; i < models.getPrestamosToCobranza().size(); i++) {
            if (models.getPagosVistaToCobranza().get(i).getCierraCon() < models.getPrestamosToCobranza().get(i).getTarifa())
                debitoTotal += models.getPagosVistaToCobranza().get(i).getCierraCon();
            else
                debitoTotal += models.getPrestamosToCobranza().get(i).getTarifa();

            models.getCobranza().setDebitoTotal(debitoTotal);
        }
    }
}

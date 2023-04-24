package tech.calaverita.reporterloanssql.threads;

import tech.calaverita.reporterloanssql.pojos.Models;
import tech.calaverita.reporterloanssql.pojos.Repositories;
import tech.calaverita.reporterloanssql.repositories.XpressRepository;

public class DashboardGerenciaThread implements Runnable {
    public Repositories repositories;
    public Models models;

    public DashboardGerenciaThread(Repositories repositories, Models models) {
        this.repositories = repositories;
        this.models = models;
    }

    @Override
    public void run() {
        Thread[] threads = new Thread[24];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new DashboardThread(repositories, models, i));
        }

        for (int i = 0; i < 6; i++) {
            threads[i].start();
        }

        while (models.getPrestamosToCobranza() == null) {

        }

        for (int i = 6; i < 8; i++) {
            threads[i].start();
        }

        while (models.getPagosVistaToCobranza() == null) {

        }

        for (int i = 8; i < 12; i++) {
            threads[i].start();
        }

        while (models.getLiquidaciones() == null || models.getPagosOfLiquidaciones() == null) {

        }

        for (int i = 12; i < 15; i++) {
            threads[i].start();
        }

        while (models.getPagosVistaToDashboard() == null) {

        }

//        if (models.getPagosVistaToDashboard().isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }

        for (int i = 15; i < 20; i++) {
            threads[i].start();
        }

        while (models.getDashboard().getCobranzaTotal() == null || models.getDashboard().getMontoExcedente() == null) {

        }

        for (int i = 20; i < 22; i++) {
            threads[i].start();
        }

        while (models.getDashboard().getTotalCobranzaPura() == null) {

        }

        for (int i = 22; i < 24; i++) {
            threads[i].start();
        }

        while (models.getDashboard().getRendimiento() == null || models.getDashboard().getMontoDeDebitoFaltante() == null) {

        }
    }
}
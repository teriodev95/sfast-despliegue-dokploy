package tech.calaverita.reporterloanssql.helpers;

import tech.calaverita.reporterloanssql.pojos.ObjectsContainer;
import tech.calaverita.reporterloanssql.services.RepositoriesContainer;
import tech.calaverita.reporterloanssql.threads.DashboardThread;

public class DashboardUtil implements Runnable {
    public RepositoriesContainer repositoriesContainer;
    public ObjectsContainer objectsContainer;

    public DashboardUtil(RepositoriesContainer repositoriesContainer, ObjectsContainer objectsContainer) {
        this.repositoriesContainer = repositoriesContainer;
        this.objectsContainer = objectsContainer;
    }

    @Override
    public void run() {
        Thread[] threads = new Thread[24];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new DashboardThread(repositoriesContainer, objectsContainer, i));
        }

        for (int i = 0; i < 6; i++) {
            threads[i].start();
        }

        while (objectsContainer.getPrestamosToCobranza() == null) {

        }

        for (int i = 6; i < 8; i++) {
            threads[i].start();
        }

        while (objectsContainer.getPagosVistaToCobranza() == null) {

        }

        for (int i = 8; i < 12; i++) {
            threads[i].start();
        }

        while (objectsContainer.getLiquidaciones() == null || objectsContainer.getPagosOfLiquidaciones() == null) {

        }

        for (int i = 12; i < 15; i++) {
            threads[i].start();
        }

        while (objectsContainer.getPagosVistaToDashboard() == null) {

        }

        for (int i = 15; i < 20; i++) {
            threads[i].start();
        }

        while (objectsContainer.getDashboard().getCobranzaTotal() == null || objectsContainer.getDashboard().getMontoExcedente() == null) {

        }

        for (int i = 20; i < 22; i++) {
            threads[i].start();
        }

        while (objectsContainer.getDashboard().getTotalCobranzaPura() == null) {

        }

        for (int i = 22; i < 24; i++) {
            threads[i].start();
        }
    }
}
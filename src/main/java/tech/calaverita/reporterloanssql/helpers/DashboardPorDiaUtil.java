package tech.calaverita.reporterloanssql.helpers;

import tech.calaverita.reporterloanssql.pojos.ObjectsContainer;
import tech.calaverita.reporterloanssql.services.RepositoriesContainer;
import tech.calaverita.reporterloanssql.threads.pwa.DashboardPorDiaPWAThread;

public class DashboardPorDiaUtil implements Runnable {
    public RepositoriesContainer repositoriesContainer;
    public ObjectsContainer objectsContainer;

    public DashboardPorDiaUtil(RepositoriesContainer repositoriesContainer, ObjectsContainer objectsContainer) {
        this.repositoriesContainer = repositoriesContainer;
        this.objectsContainer = objectsContainer;
    }

    @Override
    public void run() {
        Thread[] threads = new Thread[21];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new DashboardPorDiaPWAThread(repositoriesContainer, objectsContainer, i));
        }

        threads[0].start();

        while (objectsContainer.getCalendarioModel() == null) {

        }

        for (int i = 1; i < 7; i++) {
            threads[i].start();
        }

        while (objectsContainer.getPrestamosToCobranza() == null) {

        }

        threads[7].start();

        while (objectsContainer.getPagosVistaToCobranza() == null){

        }

        threads[8].start();

        while (objectsContainer.getLiquidaciones() == null || objectsContainer.getPagosOfLiquidaciones() == null) {

        }

        for (int i = 9; i < 12; i++) {
            threads[i].start();
        }

        while (objectsContainer.getPrestamosToDashboard() == null){

        }

        threads[12].start();

        while (objectsContainer.getPagoModelsToDashboard() == null) {

        }

        for (int i = 13; i < 17; i++) {
            threads[i].start();
        }

        while (objectsContainer.getDashboard().getCobranzaTotal() == null || objectsContainer.getDashboard().getMontoExcedente() == null) {

        }

        threads[17].start();

        while (objectsContainer.getDashboard().getTotalCobranzaPura() == null) {

        }

        for (int i = 18; i < 21; i++) {
            threads[i].start();
        }
    }
}
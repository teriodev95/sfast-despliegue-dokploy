package tech.calaverita.reporterloanssql.utils;

import tech.calaverita.reporterloanssql.pojos.ObjectsContainer;
import tech.calaverita.reporterloanssql.threads.pwa.DashboardPorDiaPWAThread;

public class DashboardPorDiaUtil implements Runnable {
    public ObjectsContainer objectsContainer;

    public DashboardPorDiaUtil(ObjectsContainer objectsContainer) {
        this.objectsContainer = objectsContainer;
    }

    @Override
    public void run() {
        Thread[] threads = new Thread[7];

        for (int i = 0; i < 7; i++) {
            threads[i] = new Thread(new DashboardPorDiaPWAThread(objectsContainer, i, threads));
            threads[i].setPriority(1);
        }

        for (int i = 0; i < 7; i++) {
            threads[i].start();
        }

        for (int i = 0; i < 7; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
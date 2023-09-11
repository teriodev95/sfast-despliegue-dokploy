package tech.calaverita.reporterloanssql.helpers;

import tech.calaverita.reporterloanssql.pojos.ObjectsContainer;
import tech.calaverita.reporterloanssql.threads.DashboardThread;

public class DashboardUtil implements Runnable {
    public ObjectsContainer objectsContainer;

    public DashboardUtil(ObjectsContainer objectsContainer) {
        this.objectsContainer = objectsContainer;
    }

    @Override
    public void run() {
        Thread[] threads = new Thread[7];

        for (int i = 0; i < 7; i++) {
            threads[i] = new Thread(new DashboardThread(objectsContainer, i, threads));
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
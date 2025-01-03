package tech.calaverita.sfast_xpress.utils;

import tech.calaverita.sfast_xpress.pojos.ObjectsContainer;
import tech.calaverita.sfast_xpress.threads.pwa.DashboardPorDiaYHoraPWAThread;

public class DashboardPorDiaYHoraUtil implements Runnable {
    public ObjectsContainer objectsContainer;

    public DashboardPorDiaYHoraUtil(ObjectsContainer objectsContainer) {
        this.objectsContainer = objectsContainer;
    }

    @Override
    public void run() {
        Thread[] threads = new Thread[7];

        for (int i = 0; i < 7; i++) {
            threads[i] = new Thread(new DashboardPorDiaYHoraPWAThread(objectsContainer, i, threads));
            threads[i].setPriority(1);
        }

        threads[5].start();

        for (int i = 0; i < 7; i++) {
            if (i != 5)
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
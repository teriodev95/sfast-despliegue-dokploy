package tech.calaverita.reporterloanssql.utils;

import tech.calaverita.reporterloanssql.pojos.ObjectsContainer;
import tech.calaverita.reporterloanssql.threads.CobranzaThread;

public class CobranzaUtil implements Runnable {
    public ObjectsContainer objectsContainer;

    public CobranzaUtil(ObjectsContainer objectsContainer) {
        this.objectsContainer = objectsContainer;
    }

    @Override
    public void run() {
        Thread[] threads = new Thread[8];

        for (int i = 0; i < 2; i++) {
            threads[i] = new Thread(new CobranzaThread(objectsContainer, i));
        }

        threads[0].start();
        threads[1].start();

        for (int i = 2; i < 8; i++) {
            threads[i] = new Thread(new CobranzaThread(objectsContainer, i, threads));
            threads[i].setPriority(1);
        }

        for (int i = 2; i < 8; i++) {
            threads[i].start();
        }

        for (int i = 2; i < 8; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

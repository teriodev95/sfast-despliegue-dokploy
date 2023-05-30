package tech.calaverita.reporterloanssql.helpers;

import tech.calaverita.reporterloanssql.pojos.ObjectsContainer;
import tech.calaverita.reporterloanssql.services.RepositoriesContainer;
import tech.calaverita.reporterloanssql.threads.CobranzaThread;

public class CobranzaUtil implements Runnable {
    public RepositoriesContainer repositoriesContainer;
    public ObjectsContainer objectsContainer;

    public CobranzaUtil(RepositoriesContainer repositoriesContainer, ObjectsContainer objectsContainer){
        this.repositoriesContainer = repositoriesContainer;
        this.objectsContainer = objectsContainer;
    }

    @Override
    public void run() {
        Thread[] threads = new Thread[8];

        for(int i = 0; i < 8; i++){
            threads[i] = new Thread(new CobranzaThread(repositoriesContainer, objectsContainer, i));
        }

        threads[0].start();
        threads[1].start();

        while(objectsContainer.getPrestamosToCobranza() == null){

        }

        threads[2].start();
        threads[3].start();

        while(objectsContainer.getPagosVistaToCobranza() == null){

        }

        threads[4].start();
        threads[5].start();
        threads[6].start();
        threads[7].start();

        while(objectsContainer.getCobranza().getDebitoTotal() == null){
            
        }
    }
}

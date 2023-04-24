package tech.calaverita.reporterloanssql.threads;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import tech.calaverita.reporterloanssql.pojos.Models;
import tech.calaverita.reporterloanssql.pojos.Repositories;
import tech.calaverita.reporterloanssql.repositories.XpressRepository;

public class CobranzaGerenciaThread implements Runnable{
    public Repositories repositories;
    public Models models;

    public CobranzaGerenciaThread(Repositories repositories, Models models){
        this.repositories = repositories;
        this.models = models;
    }

    @Override
    public void run() {
        Thread[] threads = new Thread[8];

        for(int i = 0; i < 8; i++){
            threads[i] = new Thread(new CobranzaThread(repositories, models, i));
        }

        threads[0].start();
        threads[1].start();

        while(models.getPrestamosToCobranza() == null){

        }

        threads[2].start();
        threads[3].start();

        while(models.getPagosVistaToCobranza() == null){

        }

        threads[4].start();
        threads[5].start();
        threads[6].start();
        threads[7].start();

        for (int i = 4; i < 8; i++){
            while(threads[i].isAlive()){
            }
        }
    }
}

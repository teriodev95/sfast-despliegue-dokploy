package tech.calaverita.reporterloanssql.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.calaverita.reporterloanssql.persistence.entities.CalendarioEntity;
import tech.calaverita.reporterloanssql.pojos.ObjectsContainer;
import tech.calaverita.reporterloanssql.services.CalendarioService;
import tech.calaverita.reporterloanssql.threads.CobranzaThread;

@Component
public class CobranzaUtil implements Runnable {
    private ObjectsContainer objectsContainer;
    private static CalendarioService calendarioService;

    @Autowired
    public CobranzaUtil(
            CalendarioService calendarioService
    ) {
        CobranzaUtil.calendarioService = calendarioService;
    }

    public CobranzaUtil(
            ObjectsContainer objectsContainer_S
    ) {
        this.objectsContainer = objectsContainer_S;
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
            } catch (
                    InterruptedException e
            ) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void funSemanaAnterior(
            CalendarioEntity calendarioEntity
    ) {
        // To easy code
        int anio = calendarioEntity.getAnio();
        int semana = calendarioEntity.getSemana();

        if (
                semana == 1
        ) {
            anio = anio - 1;
            semana = CobranzaUtil.calendarioService
                    .existsByAnioAndSemana(anio, 53) ? 53 : 52;
        } //
        else {
            semana = semana - 1;
        }

        calendarioEntity.setAnio(anio);
        calendarioEntity.setSemana(semana);
    }
}

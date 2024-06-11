package tech.calaverita.sfast_xpress.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.calaverita.sfast_xpress.models.mariaDB.CalendarioModel;
import tech.calaverita.sfast_xpress.pojos.ObjectsContainer;
import tech.calaverita.sfast_xpress.services.CalendarioService;
import tech.calaverita.sfast_xpress.threads.CobranzaThread;

@Component
public class CobranzaUtil implements Runnable {
    private ObjectsContainer objectsContainer;
    private static CalendarioService calendarioService;

    @Autowired
    public CobranzaUtil(CalendarioService calendarioService) {
        CobranzaUtil.calendarioService = calendarioService;
    }

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

    public static void funSemanaAnterior(CalendarioModel calendarioModel) {
        // To easy code
        int anio = calendarioModel.getAnio();
        int semana = calendarioModel.getSemana();

        if (semana == 1) {
            anio = anio - 1;
            semana = CobranzaUtil.calendarioService
                    .existsByAnioAndSemana(anio, 53) ? 53 : 52;
        } else {
            semana = semana - 1;
        }

        calendarioModel.setAnio(anio);
        calendarioModel.setSemana(semana);
    }
}

package tech.calaverita.reporterloanssql.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.mariaDB.VisitaModel;
import tech.calaverita.reporterloanssql.repositories.VisitaRepository;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

@Service
public class VisitaService {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private final VisitaRepository repo;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    public VisitaService(
            VisitaRepository repo
    ) {
        this.repo = repo;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    public void save(
            VisitaModel visMod_I
    ) {
        this.repo.save(visMod_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<VisitaModel> darrvisModFindAll() {
        return repo.darrvisEntFindAll();
    }

    public VisitaModel visModFindByVisitaId(
            String strVisitaId_I
    ) {
        return this.repo.visEntFindByVisitaId(strVisitaId_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<VisitaModel> darrvisModFindByPrestamoId(
            String strPrestamoId_I
    ) {
        return this.repo.darrvisEntFindByPrestamoId(strPrestamoId_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<VisitaModel> darrvisModFindByAgenciaAnioAndSemana(
            String strAgencia_I,
            int intAnio_I,
            int intSemana_I
    ) {
        return this.repo.darrvisEntFindByAgenciaAnioAndSemana(strAgencia_I, intAnio_I, intSemana_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<VisitaModel> darrVisModFindByPrestamoIdAnioAndSemana(
            String strPrestamoId_I,
            int intAnio_I,
            int intSemana_I
    ) {
        return this.repo.darrVisEntFindByPrestamoIdAnioAndSemana(strPrestamoId_I, intAnio_I, intSemana_I);
    }

    @Async("asyncExecutor")
    public CompletableFuture<ArrayList<VisitaModel>> findByGerenciasAnioAndSemanaAsync(ArrayList<String> gerencias, int anio, int semana) {
        return CompletableFuture.completedFuture(this.repo.findByGerenciasAndAnioAndSemana(gerencias, anio, semana));
    }
}

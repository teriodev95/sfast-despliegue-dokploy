package tech.calaverita.reporterloanssql.services;

import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.persistence.entities.VisitaEntity;
import tech.calaverita.reporterloanssql.persistence.repositories.VisitaRepository;

import java.util.ArrayList;

@Service
public final class VisitaService {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private final VisitaRepository visRepo;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    private VisitaService(
            VisitaRepository visRepo_S
    ) {
        this.visRepo = visRepo_S;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    public void save(
            VisitaEntity visMod_I
    ) {
        this.visRepo.save(visMod_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<VisitaEntity> darrvisModFindAll() {
        return visRepo.darrvisEntFindAll();
    }

    public VisitaEntity visModFindByVisitaId(
            String strVisitaId_I
    ) {
        return this.visRepo.visEntFindByVisitaId(strVisitaId_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<VisitaEntity> darrvisModFindByPrestamoId(
            String strPrestamoId_I
    ) {
        return this.visRepo.darrvisEntFindByPrestamoId(strPrestamoId_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<VisitaEntity> darrvisModFindByAgenciaAnioAndSemana(
            String strAgencia_I,
            int intAnio_I,
            int intSemana_I
    ) {
        return this.visRepo.darrvisEntFindByAgenciaAnioAndSemana(strAgencia_I, intAnio_I, intSemana_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<VisitaEntity> darrVisModFindByPrestamoIdAnioAndSemana(
            String strPrestamoId_I,
            int intAnio_I,
            int intSemana_I
    ) {
        return this.visRepo.darrVisEntFindByPrestamoIdAnioAndSemana(strPrestamoId_I, intAnio_I, intSemana_I);
    }
}

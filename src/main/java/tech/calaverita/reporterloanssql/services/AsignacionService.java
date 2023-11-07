package tech.calaverita.reporterloanssql.services;

import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.persistence.entities.AsignacionEntity;
import tech.calaverita.reporterloanssql.persistence.repositories.AsignacionRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
public final class AsignacionService {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private final AsignacionRepository asignRepo;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    public AsignacionService(
            AsignacionRepository asignRepo_S
    ) {
        this.asignRepo = asignRepo_S;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    public void save(
            AsignacionEntity asignMod_I
    ) {
        asignRepo.save(asignMod_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public Iterable<AsignacionEntity> iteasignModFindAll() {
        return asignRepo.findAll();
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public Optional<AsignacionEntity> optasignModFindById(
            String strId_I
    ) {
        return asignRepo.findById(strId_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<AsignacionEntity> darrasignModFindByAgenciaAnioAndSemana(
            String agencia_I,
            int anio_I,
            int semana_I
    ) {
        return asignRepo.darrasignModFindByAgenciaAnioAndSemana(agencia_I, anio_I, semana_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<AsignacionEntity> darrasignModFindByAgenciaAnioAndSemanaToDashboard(
            String agencia_I,
            int anio_I,
            int semana_I
    ) {
        return asignRepo.darrasignModFindByAgenciaAnioAndSemanaToDashboard(agencia_I, anio_I, semana_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public double getSumaDeAsigancionesByAgenciaAnioAndSemana(
            String strAgencia_I,
            int intAnio_I,
            int intSemana_I
    ) {
        return asignRepo.getSumaDeAsigancionesByAgenciaAnioAndSemana(strAgencia_I, intAnio_I, intSemana_I);
    }
}

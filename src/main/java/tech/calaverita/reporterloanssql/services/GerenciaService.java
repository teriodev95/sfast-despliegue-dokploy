package tech.calaverita.reporterloanssql.services;

import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.persistence.entities.GerenciaEntity;
import tech.calaverita.reporterloanssql.persistence.repositories.GerenciaRepository;

import java.util.ArrayList;

@Service
public final class GerenciaService {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private final GerenciaRepository gerRepo;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    private GerenciaService(
            GerenciaRepository gerRepo_S
    ) {
        this.gerRepo = gerRepo_S;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    public ArrayList<String> darrstrGerenciaIdFindBySeguridad(
            String seguridad_I
    ) {
        return this.gerRepo.darrstrGerenciaIdFindBySeguridad(seguridad_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<String> darrstrGerenciaIdByRegional(
            String regional_I
    ) {
        return this.gerRepo.darrstrGerenciaIdByRegional(regional_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<GerenciaEntity> darrGerModFindAll() {
        return this.gerRepo.darrGerEntFindAll();
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<String> darrstrGerenciaIdFindAll() {
        return this.gerRepo.darrstrGerenciaIdFindAll();
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public GerenciaEntity gerModFindByGerenciaId(
            String gerenciaId_I
    ) {
        return this.gerRepo.gerEntFindByGerenciaId(gerenciaId_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<String> darrstrGerenciaIdFindBySucursalId(
            int sucursalId_I
    ) {
        return this.gerRepo.darrstrGerenciaIdFindBySucursalId(sucursalId_I);
    }
}

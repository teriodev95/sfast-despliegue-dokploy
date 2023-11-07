package tech.calaverita.reporterloanssql.services;

import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.persistence.entities.AgenciaEntity;
import tech.calaverita.reporterloanssql.persistence.repositories.AgenciaRepository;

import java.util.ArrayList;

@Service
public final class AgenciaService {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private final AgenciaRepository agencRepo;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    public AgenciaService(
            AgenciaRepository agencRepo_S
    ) {
        this.agencRepo = agencRepo_S;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    public AgenciaEntity agencModFindByAgenciaId(
            String agenciaId_I
    ) {
        return this.agencRepo.agencModFindByAgenciaId(agenciaId_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<String> darrstrAgenciaIdFindByGerenciaId(
            String strGerencia_I
    ) {
        return this.agencRepo.darrstrAgenciaIdFindByGerenciaId(strGerencia_I);
    }
}

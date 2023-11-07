package tech.calaverita.reporterloanssql.services;

import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.persistence.entities.SucursalEntity;
import tech.calaverita.reporterloanssql.persistence.repositories.SucursalRepository;

@Service
public final class SucursalService {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private final SucursalRepository sucRepo;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    private SucursalService(
            SucursalRepository sucRepo_S
    ) {
        this.sucRepo = sucRepo_S;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    public SucursalEntity sucModFindBySucursalId(
            String strSucursalId_I
    ) {
        return this.sucRepo.sucEntFindBySucursalId(strSucursalId_I);
    }
}

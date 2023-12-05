package tech.calaverita.reporterloanssql.services.relation;

import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.persistence.repositories.relation.UsuarioSucursalRepository;

import java.util.ArrayList;

@Service
public final class UsuarioSucursalService {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private final UsuarioSucursalRepository usuarSucRepo;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    private UsuarioSucursalService(
            UsuarioSucursalRepository usuarSucRepo_S
    ) {
        this.usuarSucRepo = usuarSucRepo_S;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    public ArrayList<String> darrstrSucursalIdFindByUsuarioId(
            int intUsuarioId_I
    ) {
        return this.usuarSucRepo.darrstrSucursalIdFindByUsuarioId(intUsuarioId_I);
    }
}

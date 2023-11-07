package tech.calaverita.reporterloanssql.services.relation;

import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.persistence.repositories.relation.UsuarSucRepoUsuarioSucursalRepository;

import java.util.ArrayList;

@Service
public final class UsuarioSucursalService {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private final UsuarSucRepoUsuarioSucursalRepository usuarSucRepo;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    private UsuarioSucursalService(
            UsuarSucRepoUsuarioSucursalRepository usuarSucRepo_S
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

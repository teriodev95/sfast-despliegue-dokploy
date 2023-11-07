package tech.calaverita.reporterloanssql.services.relation;

import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.persistence.repositories.relation.UsuarGerRepoUsuarioGerenciaRepository;

import java.util.ArrayList;

@Service
public final class UsuarioGerenciaService {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private final UsuarGerRepoUsuarioGerenciaRepository usuarGerRepo;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    private UsuarioGerenciaService(
            UsuarGerRepoUsuarioGerenciaRepository usuarGerRepo_S
    ) {
        this.usuarGerRepo = usuarGerRepo_S;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    public ArrayList<String> darrstrGerenciaIdByUsuarioId(
            int intUsuarioId_I
    ) {
        return this.usuarGerRepo.darrstrGerenciaIdFindByUsuarioId(intUsuarioId_I);
    }
}

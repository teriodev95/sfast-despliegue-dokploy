package tech.calaverita.reporterloanssql.repositories.relation;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.relation.UsuarioGerenciaModel;

import java.util.ArrayList;

@Repository
public interface UsuarGerRepoUsuarioGerenciaRepository extends CrudRepository<UsuarioGerenciaModel,
        Integer> {
    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @Query("SELECT ugm.gerenciaId " +
            "FROM UsuarioGerenciaModel ugm " +
            "WHERE ugm.usuarioId = :usuarioId")
    ArrayList<String> darrstrGerenciaIdFindByUsuarioId(
            int usuarioId
    );
}

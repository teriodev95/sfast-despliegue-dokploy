package tech.calaverita.reporterloanssql.persistence.repositories.relation;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.persistence.entities.relation.UsuarioGerenciaEntity;

import java.util.ArrayList;

@Repository
public interface UsuarioGerenciaRepository extends CrudRepository<UsuarioGerenciaEntity,
        Integer> {
    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @Query("SELECT ugm.gerenciaId " +
            "FROM UsuarioGerenciaEntity ugm " +
            "WHERE ugm.usuarioId = :usuarioId")
    ArrayList<String> darrstrGerenciaIdFindByUsuarioId(
            int usuarioId
    );
}

package tech.calaverita.reporterloanssql.repositories.relation;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.relation.UsuarioSucursalModel;

import java.util.ArrayList;

@Repository
public interface UsuarioSucursalRepository extends CrudRepository<UsuarioSucursalModel,
        Integer> {
    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @Query("SELECT usm.sucursalId " +
            "FROM UsuarioSucursalModel usm " +
            "WHERE usm.usuarioId = :usuarioId")
    ArrayList<String> darrstrSucursalIdFindByUsuarioId(
            int usuarioId
    );
}

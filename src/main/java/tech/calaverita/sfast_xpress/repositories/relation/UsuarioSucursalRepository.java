package tech.calaverita.sfast_xpress.repositories.relation;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.sfast_xpress.models.mariaDB.relation.UsuarioSucursalModel;

import java.util.ArrayList;

@Repository
public interface UsuarioSucursalRepository extends CrudRepository<UsuarioSucursalModel,
        Integer> {
    @Query("SELECT usm.sucursalId FROM UsuarioSucursalModel usm WHERE usm.usuarioId = :usuarioId")
    ArrayList<String> darrstrSucursalIdFindByUsuarioId(int usuarioId);
}

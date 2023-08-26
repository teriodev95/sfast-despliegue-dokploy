package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.UsuarioSucursalModel;

import java.util.ArrayList;

@Repository
public interface UsuarioSucursalRepository extends CrudRepository<UsuarioSucursalModel, Integer> {
    @Query("SELECT usm.sucursalId FROM UsuarioSucursalModel usm WHERE usm.usuarioId = :usuarioId")
    ArrayList<String> getSucursalIdsByUsuarioId(int usuarioId);
}

package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.mariaDB.GerenciaModel;

import java.util.ArrayList;

@Repository
public interface GerenciaRepository extends CrudRepository<GerenciaModel, String> {
    @Query("SELECT ger.gerenciaId FROM GerenciaModel ger WHERE ger.sucursalId = :sucursalId ORDER BY ger.gerenciaId")
    ArrayList<String> findIdsBySucursalId(int sucursalId);

    @Query("SELECT ger FROM GerenciaModel ger WHERE ger.gerenciaId IN (SELECT usuar_ger.gerenciaId " +
            "FROM UsuarioGerenciaModel usuar_ger INNER JOIN UsuarioModel usuar " +
            "ON usuar_ger.usuarioId = usuar.usuarioId AND usuar.usuario = :usuario)")
    ArrayList<GerenciaModel> findByUsuario(String usuario);
}

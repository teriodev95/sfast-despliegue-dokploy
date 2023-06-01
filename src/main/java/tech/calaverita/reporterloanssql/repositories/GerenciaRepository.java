package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.GerenciaModel;

import java.util.ArrayList;

@Repository
public interface GerenciaRepository extends CrudRepository<GerenciaModel, String> {
    @Query("SELECT ger FROM GerenciaModel ger WHERE ger.gerenciaId = :gerenciaId")
    GerenciaModel getGerenciaModelByGerenciaId(String gerenciaId);

    @Query(value = "SELECT ger.gerenciaId " +
            "FROM gerencias ger " +
            "WHERE ger.seguridadId = (SELECT usu.usuarioId " +
            "FROM usuarios usu " +
            "WHERE usu.usuario = :seguridad)", nativeQuery = true)
    ArrayList<String> getGerenciasBySeguridad(String seguridad);

    @Query("SELECT ger.gerenciaId " +
            "FROM GerenciaModel ger " +
            "ORDER BY ger.gerenciaId")
    ArrayList<String> getGerencias();
}

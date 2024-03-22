package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.mariaDB.GerenciaModel;

import java.util.ArrayList;

@Repository
public interface GerenciaRepository extends CrudRepository<GerenciaModel, String> {
    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @Query("SELECT ger " +
            "FROM GerenciaModel ger")
    ArrayList<GerenciaModel> darrGerEntFindAll();

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT ger " +
            "FROM GerenciaModel ger " +
            "WHERE ger.gerenciaId = :gerenciaId")
    GerenciaModel gerEntFindByGerenciaId(
            String gerenciaId
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query(value = "SELECT ger.gerenciaId " +
            "FROM gerencias ger " +
            "WHERE ger.seguridadId = (SELECT usu.usuarioId " +
            "FROM usuarios usu " +
            "WHERE usu.usuario = :seguridad)", nativeQuery = true)
    ArrayList<String> darrstrGerenciaIdFindBySeguridad(
            String seguridad
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query(value = "SELECT ger.gerenciaId " +
            "FROM gerencias ger " +
            "WHERE ger.sucursalId = (SELECT suc.sucursalId " +
            "FROM sucursales suc WHERE suc.regionalId = (SELECT usu.usuarioId " +
            "FROM usuarios usu " +
            "WHERE usu.usuario = :regional))", nativeQuery = true)
    ArrayList<String> darrstrGerenciaIdByRegional(
            String regional
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT ger.gerenciaId " +
            "FROM GerenciaModel ger " +
            "ORDER BY ger.gerenciaId")
    ArrayList<String> darrstrGerenciaIdFindAll();

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT ger.gerenciaId " +
            "FROM GerenciaModel ger " +
            "WHERE ger.sucursalId = :sucursalId")
    ArrayList<String> darrstrGerenciaIdFindBySucursalId(
            int sucursalId
    );
}

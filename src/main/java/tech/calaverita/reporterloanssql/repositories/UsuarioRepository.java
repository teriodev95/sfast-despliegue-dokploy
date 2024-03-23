package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.mariaDB.UsuarioModel;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends CrudRepository<UsuarioModel, Integer> {
    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @Query("SELECT usu " +
            "FROM UsuarioModel usu " +
            "WHERE usu.usuario = :usuario " +
            "AND usu.pin = :pin")
    UsuarioModel usuarEntFindByUsuarioAndPin(
            String usuario,
            String pin
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT us " +
            "FROM UsuarioModel us " +
            "WHERE us.usuario = :usuario")
    Optional<UsuarioModel> optusuarEntFindByUsuario(
            String usuario
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT us " +
            "FROM UsuarioModel us " +
            "WHERE us.tipo = :tipo")
    ArrayList<UsuarioModel> darrusuarEntFindByTipo(
            String tipo
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT us " +
            "FROM UsuarioModel us " +
            "WHERE us.usuarioId = :usuarioId")
    UsuarioModel usuarEntFindByUsuarioId(
            int usuarioId
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query(value = "SELECT us.* " +
            "FROM usuarios us " +
            "WHERE us.usuarioId = (SELECT suc.regionalId " +
            "FROM sucursales suc " +
            "WHERE suc.sucursalId = (SELECT ger.sucursalId " +
            "FROM gerencias ger " +
            "WHERE ger.gerenciaId = :gerenciaId))", nativeQuery = true)
    UsuarioModel usuarEntFindByUsuarioIdFromGerenciaIdOfGerenciaModel(
            String gerenciaId
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT usu " +
            "FROM UsuarioModel usu " +
            "JOIN UsuarioGerenciaModel ugm " +
            "ON usu.usuarioId = ugm.usuarioId " +
            "AND ugm.gerenciaId = :gerencia")
    ArrayList<UsuarioModel> darrusuarEntFindByGerencia(
            String gerencia
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    Optional<UsuarioModel> findByUsuario(String usuario);

    Optional<UsuarioModel> findByAgencia(String agencia);

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    boolean existsByUsuarioAndTipo(String usuario, String tipo);

    Optional<UsuarioModel> findByGerenciaAndTipo(String usuario, String tipo);

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    boolean existsByUsuarioAndTipoAndStatus(String usuario, String tipo, boolean status);

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    boolean existsByUsuario(String usuario);

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    boolean existsByUsuarioAndStatus(String usuario, boolean status);

    boolean existsByAgencia(String agencia);
}

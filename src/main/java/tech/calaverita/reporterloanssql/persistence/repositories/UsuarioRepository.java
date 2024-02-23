package tech.calaverita.reporterloanssql.persistence.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.persistence.entities.UsuarioEntity;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends CrudRepository<UsuarioEntity, Integer> {
    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @Query("SELECT usu " +
            "FROM UsuarioEntity usu " +
            "WHERE usu.usuario = :usuario " +
            "AND usu.pin = :pin")
    UsuarioEntity usuarEntFindByUsuarioAndPin(
            String usuario,
            String pin
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT us " +
            "FROM UsuarioEntity us " +
            "WHERE us.usuario = :usuario")
    Optional<UsuarioEntity> optusuarEntFindByUsuario(
            String usuario
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT us " +
            "FROM UsuarioEntity us " +
            "WHERE us.tipo = :tipo")
    ArrayList<UsuarioEntity> darrusuarEntFindByTipo(
            String tipo
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT us " +
            "FROM UsuarioEntity us " +
            "WHERE us.usuarioId = :usuarioId")
    UsuarioEntity usuarEntFindByUsuarioId(
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
    UsuarioEntity usuarEntFindByUsuarioIdFromGerenciaIdOfGerenciaModel(
            String gerenciaId
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT usu " +
            "FROM UsuarioEntity usu " +
            "JOIN UsuarioGerenciaEntity ugm " +
            "ON usu.usuarioId = ugm.usuarioId " +
            "AND ugm.gerenciaId = :gerencia")
    ArrayList<UsuarioEntity> darrusuarEntFindByGerencia(
            String gerencia
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    Optional<UsuarioEntity> findByUsuario(String usuario);

    Optional<UsuarioEntity> findByAgencia(String agencia);

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    boolean existsByUsuarioAndTipo(String usuario, String tipo);

    Optional<UsuarioEntity> findByGerenciaAndTipo(String usuario, String tipo);

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    boolean existsByUsuarioAndTipoAndStatus(String usuario, String tipo, boolean status);

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    boolean existsByUsuario(String usuario);

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    boolean existsByUsuarioAndStatus(String usuario, boolean status);
}

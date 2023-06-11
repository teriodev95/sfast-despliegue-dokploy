package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.UsuarioModel;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends CrudRepository<UsuarioModel, Integer> {
    @Query("SELECT usu " +
            "FROM UsuarioModel usu " +
            "WHERE usu.usuario = :usuario " +
            "AND usu.pin = :pin")
    UsuarioModel findOneByUsuarioAndPin(String usuario, String pin);

    @Query("SELECT us " +
            "FROM UsuarioModel us " +
            "WHERE us.usuario = :usuario")
    Optional<UsuarioModel> findOneByUsuario(String usuario);

    @Query("SELECT us " +
            "FROM UsuarioModel us " +
            "WHERE us.tipo = :tipo")
    ArrayList<UsuarioModel> findManyByTipo(String tipo);

    @Query("SELECT us " +
            "FROM UsuarioModel us " +
            "WHERE us.usuarioId = :usuarioId")
    UsuarioModel findOneByUsuarioId(int usuarioId);

    @Query(value = "SELECT us.* " +
            "FROM usuarios us " +
            "WHERE us.usuarioId = (SELECT suc.regionalId " +
            "FROM sucursales suc " +
            "WHERE suc.sucursalId = (SELECT ger.sucursalId " +
            "FROM gerencias ger " +
            "WHERE ger.gerenciaId = :gerenciaId))", nativeQuery = true)
    UsuarioModel findOneByUsuarioIdFromGerenciaIdOfGerenciaModel(String gerenciaId);
}

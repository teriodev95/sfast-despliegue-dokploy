package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.mariaDB.UsuarioModel;

import java.util.ArrayList;

@Repository
public interface UsuarioRepository extends CrudRepository<UsuarioModel, Integer> {
    boolean existsByUsuario(String usuario);

    boolean existsByAgencia(String agencia);

    boolean existsByUsuarioAndTipo(String usuario, String tipo);

    boolean existsByUsuarioAndStatus(String usuario, boolean status);

    boolean existsByUsuarioAndTipoAndStatus(String usuario, String tipo, boolean status);

    UsuarioModel findByUsuarioAndPin(String usuario, int pin);

    UsuarioModel findByUsuario(String usuario);

    UsuarioModel findByAgencia(String agencia);

    ArrayList<UsuarioModel> findByGerenciaAndTipo(String gerencia, String tipo);

    @Query("SELECT CONCAT(usuar.nombre, ' ' , usuar.apellidoPaterno, ' ', usuar.apellidoMaterno) FROM UsuarioModel " +
            "usuar WHERE usuar.gerencia = :gerencia AND usuar.tipo = :tipo ORDER BY usuar.agencia")
    ArrayList<String> findAgentesByGerenciaAndTipo(String gerencia, String tipo);

    @Query("SELECT CONCAT(usuar.nombre, ' ' , usuar.apellidoPaterno, ' ', usuar.apellidoMaterno) FROM UsuarioModel " +
            "usuar WHERE usuar.gerencia IN :gerencias AND usuar.tipo = :tipo ORDER BY usuar.gerencia")
    ArrayList<String> findGerentesByGerenciaAndTipo(ArrayList<String> gerencias, String tipo);

    @Query("SELECT us " +
            "FROM UsuarioModel us " +
            "WHERE us.tipo = :tipo")
    ArrayList<UsuarioModel> darrusuarEntFindByTipo(String tipo);

    @Query(value = "SELECT us.* " +
            "FROM usuarios us " +
            "WHERE us.usuarioId = (SELECT suc.regionalId " +
            "FROM sucursales suc " +
            "WHERE suc.sucursalId = (SELECT ger.sucursalId " +
            "FROM gerencias ger " +
            "WHERE ger.gerenciaId = :gerenciaId))", nativeQuery = true)
    UsuarioModel usuarEntFindByUsuarioIdFromGerenciaIdOfGerenciaModel(String gerenciaId);

    @Query("SELECT usu " +
            "FROM UsuarioModel usu " +
            "JOIN UsuarioGerenciaModel ugm " +
            "ON usu.usuarioId = ugm.usuarioId " +
            "AND ugm.gerenciaId = :gerencia")
    ArrayList<UsuarioModel> darrusuarEntFindByGerencia(String gerencia);
}

package tech.calaverita.sfast_xpress.repositories;

import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.sfast_xpress.models.mariaDB.UsuarioModel;

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

    UsuarioModel findByAgenciaAndStatus(String agencia, boolean status);

    ArrayList<UsuarioModel> findByGerenciaAndTipo(String gerencia, String tipo);

    @Query("SELECT CONCAT(usuar.nombre, ' ' , usuar.apellidoPaterno, ' ', usuar.apellidoMaterno) AS agente, " +
            "usuar.agencia AS agencia FROM UsuarioModel usuar WHERE usuar.gerencia = :gerencia " +
            "AND usuar.tipo = :tipo AND usuar.status = :status ORDER BY usuar.agencia")
    ArrayList<Tuple> findByGerenciaAndTipoAndStatus(String gerencia, String tipo, boolean status);

    @Query("SELECT CONCAT(usuar.nombre, ' ' , usuar.apellidoPaterno, ' ', usuar.apellidoMaterno) nombre, usuar.gerencia gerencia " +
            "FROM UsuarioModel usuar WHERE usuar.gerencia IN :gerencias AND usuar.tipo = :tipo AND usuar.status = :status " +
            "ORDER BY usuar.gerencia")
    ArrayList<Tuple> findByGerenciasAndTipoAndStatus(ArrayList<String> gerencias, String tipo, boolean status);

    @Query("SELECT usuar FROM UsuarioModel usuar INNER JOIN UsuarioGerenciaModel ugm ON usuar.usuarioId = ugm.usuarioId " +
            "AND ugm.gerenciaId = :gerencia")
    ArrayList<UsuarioModel> findByGerenciaInnerJoinUsuarioGerenciaModel(String gerencia);
}

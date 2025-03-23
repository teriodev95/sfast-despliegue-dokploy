package tech.calaverita.sfast_xpress.repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jakarta.persistence.Tuple;
import tech.calaverita.sfast_xpress.models.mariaDB.UsuarioModel;

@Repository
public interface UsuarioRepository extends CrudRepository<UsuarioModel, Integer> {
        boolean existsByUsuario(String usuario);

        boolean existsByAgencia(String agencia);

        boolean existsByUsuarioIdAndStatus(int usuarioId, boolean status);

        boolean existsByUsuarioAndTipoIn(String usuario, String[] tipo);

        boolean existsByUsuarioAndTipo(String usuario, String tipo);

        boolean existsByUsuarioAndStatus(String usuario, boolean status);

        boolean existsByUsuarioAndTipoAndStatus(String usuario, String tipo, boolean status);

        boolean existsByUsuarioAndTipoInAndStatus(String usuario, String[] tipo, boolean status);

        UsuarioModel findByUsuarioAndPinAndStatus(String usuario, int pin, boolean status);

        UsuarioModel findByGerenciaAndTipoAndStatus(String gerencia, String tipo, boolean status);

        List<UsuarioModel> findByAgenciaAndTipoOrderByUsuarioIdDesc(String agencia, String tipo);

        UsuarioModel findByAgenciaAndTipoAndStatus(String agencia, String tipo, boolean status);

        UsuarioModel findByUsuario(String usuario);

        UsuarioModel findByPin(Integer pin);

        UsuarioModel findByAgenciaAndStatus(String agencia, boolean status);

        ArrayList<UsuarioModel> findByGerenciaAndStatus(String gerencia, boolean status);

        List<UsuarioModel> findAgentesByGerenciaAndTipoAndStatus(String gerencia, String tipo, Boolean status);

        @Query("SELECT CONCAT(usuar.nombre, ' ' , usuar.apellidoPaterno, ' ', usuar.apellidoMaterno) AS agente, " +
                        "usuar.agencia AS agencia FROM UsuarioModel usuar WHERE usuar.gerencia = :gerencia " +
                        "AND usuar.tipo = :tipo AND usuar.status = :status ORDER BY usuar.agencia")
        ArrayList<Tuple> findNombreCompletoByGerenciaAndTipoAndStatus(String gerencia, String tipo, boolean status);

        @Query("SELECT CONCAT(usuar.nombre, ' ' , usuar.apellidoPaterno, ' ', usuar.apellidoMaterno) nombre, usuar.gerencia gerencia "
                        +
                        "FROM UsuarioModel usuar WHERE usuar.gerencia IN :gerencias AND usuar.tipo = :tipo AND usuar.status = :status "
                        +
                        "ORDER BY usuar.gerencia")
        ArrayList<Tuple> findByGerenciasAndTipoAndStatus(ArrayList<String> gerencias, String tipo, boolean status);

        @Query("SELECT usuar FROM UsuarioModel usuar INNER JOIN UsuarioGerenciaModel ugm ON usuar.usuarioId = ugm.usuarioId "
                        +
                        "AND ugm.gerenciaId = :gerencia")
        ArrayList<UsuarioModel> findByGerenciaInnerJoinUsuarioGerenciaModel(String gerencia);

        @Query("SELECT usuar FROM UsuarioModel usuar INNER JOIN UsuarioGerenciaModel usuarGer ON usuar.usuarioId = usuarGer.usuarioId "
                        + "WHERE usuarGer.gerenciaId = :gerencia AND usuar.tipo = :tipo AND usuar.status = :status")
        ArrayList<UsuarioModel> findByGerenciaInnerJoinUsuarioGerenciaModel(String gerencia, String tipo,
                        boolean status);
}

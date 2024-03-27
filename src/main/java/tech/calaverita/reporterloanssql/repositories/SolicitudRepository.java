package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.mariaDB.SolicitudModel;

import java.util.ArrayList;

@Repository
public interface SolicitudRepository extends CrudRepository<SolicitudModel, String> {
    boolean existsById(String id);

    ArrayList<SolicitudModel> findByGerencia(String gerencia);

    ArrayList<SolicitudModel> findByAgencia(String agencia);
}

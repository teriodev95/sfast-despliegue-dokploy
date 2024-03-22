package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.mariaDB.SolicitudModel;

import java.util.ArrayList;

@Repository
public interface SolicitudRepository extends CrudRepository<SolicitudModel, Integer> {
    Boolean existsById(String id);

    ArrayList<SolicitudModel> findByGerente(String gerencia);

    ArrayList<SolicitudModel> findByAgente(String agencia);

    @Query("UPDATE SolicitudModel " +
            "SET status = :status WHERE id = :id")
    void updateStatusById(String id, String status);
}

package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.mariaDB.AgenciaModel;

import java.util.ArrayList;

@Repository
public interface AgenciaRepository extends CrudRepository<AgenciaModel, String> {
    ArrayList<AgenciaModel> findByGerenciaId(String gerenciaId);

    @Query("SELECT agenc.status FROM AgenciaModel agenc WHERE agenc.id = :id")
    String findStatusById(String id);

    @Query("SELECT agenc.id FROM AgenciaModel agenc WHERE agenc.gerenciaId = :gerenciaId AND agenc.status = :status " +
            "ORDER BY agenc.id")
    ArrayList<String> findIdsByGerenciaIdAndStatus(String gerenciaId, String status);
}

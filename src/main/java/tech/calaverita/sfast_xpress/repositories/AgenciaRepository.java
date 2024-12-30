package tech.calaverita.sfast_xpress.repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tech.calaverita.sfast_xpress.models.mariaDB.AgenciaModel;

@Repository
public interface AgenciaRepository extends CrudRepository<AgenciaModel, String> {
    ArrayList<AgenciaModel> findByGerenciaId(String gerenciaId);

    @Query("SELECT agenc.status FROM AgenciaModel agenc WHERE agenc.id = :id")
    String findStatusById(String id);

    @Query("SELECT agenc.id FROM AgenciaModel agenc WHERE agenc.gerenciaId = :gerenciaId AND agenc.status = :status " +
            "ORDER BY agenc.id")
    ArrayList<String> findIdsByGerenciaIdAndStatus(String gerenciaId, String status);

    ArrayList<AgenciaModel> findByGerenciaIdAndStatusOrderById(String gerenciaId, String status);
}

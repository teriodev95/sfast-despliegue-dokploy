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

    @Query("SELECT agenc.id FROM AgenciaModel agenc WHERE agenc.gerenciaId = :gerenciaId ORDER BY agenc.id")
    ArrayList<String> findIdsByGerenciaId(String gerenciaId);

    ArrayList<AgenciaModel> findByGerenciaIdAndStatusOrderById(String gerenciaId, String status);
}

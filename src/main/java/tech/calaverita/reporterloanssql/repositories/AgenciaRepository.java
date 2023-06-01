package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.AgenciaModel;

import java.util.ArrayList;

@Repository
public interface AgenciaRepository extends CrudRepository<AgenciaModel, String> {
    @Query("SELECT ag FROM AgenciaModel ag WHERE ag.agenciaId = :agenciaId")
    AgenciaModel getAgenciaModelByAgenciaId(String agenciaId);

    @Query("SELECT ag.agenciaId " +
            "FROM AgenciaModel ag " +
            "WHERE ag.gerenciaId = :gerencia " +
            "ORDER BY ag.agenciaId")
    ArrayList<String> getAgenciasByGerencia(String gerencia);
}

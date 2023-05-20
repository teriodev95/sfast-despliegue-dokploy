package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.AgenciaModel;

@Repository
public interface AgenciaRepository extends CrudRepository<AgenciaModel, String> {
    @Query("SELECT ag FROM AgenciaModel ag WHERE ag.agenciaId = :agenciaId")
    AgenciaModel getAgenciaModelByAgenciaId(String agenciaId);
}

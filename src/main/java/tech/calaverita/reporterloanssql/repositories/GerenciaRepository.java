package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.AgenciaModel;
import tech.calaverita.reporterloanssql.models.GerenciaModel;

@Repository
public interface GerenciaRepository extends CrudRepository<GerenciaModel, String> {
    @Query("SELECT ger FROM GerenciaModel ger WHERE ger.gerenciaId = :gerenciaId")
    GerenciaModel getGerenciaModelByGerenciaId(String gerenciaId);
}

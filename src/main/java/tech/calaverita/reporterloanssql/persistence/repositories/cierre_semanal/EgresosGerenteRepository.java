package tech.calaverita.reporterloanssql.persistence.repositories.cierre_semanal;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.persistence.entities.cierre_semanal.EgresosGerenteEntity;

@Repository
public interface EgresosGerenteRepository extends CrudRepository<EgresosGerenteEntity, String> {

}

package tech.calaverita.reporterloanssql.persistence.repositories.cierre_semanal;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.persistence.entities.cierre_semanal.IngresosAgenteEntity;

@Repository
public interface IngresosAgenteRepository extends CrudRepository<IngresosAgenteEntity,
        String> {

}

package tech.calaverita.reporterloanssql.repositories.cierre_semanal;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.mariaDB.cierre_semanal.EgresosAgenteModel;

@Repository
public interface EgresosAgenteRepository extends CrudRepository<EgresosAgenteModel, String> {

}

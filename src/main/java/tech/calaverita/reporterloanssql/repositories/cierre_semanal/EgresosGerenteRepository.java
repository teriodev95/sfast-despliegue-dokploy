package tech.calaverita.reporterloanssql.repositories.cierre_semanal;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.mariaDB.cierre_semanal.EgresosGerenteModel;

@Repository
public interface EgresosGerenteRepository extends CrudRepository<EgresosGerenteModel, String> {

}

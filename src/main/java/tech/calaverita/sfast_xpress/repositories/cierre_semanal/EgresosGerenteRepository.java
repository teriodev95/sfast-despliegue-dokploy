package tech.calaverita.sfast_xpress.repositories.cierre_semanal;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal.EgresosGerenteModel;

@Repository
public interface EgresosGerenteRepository extends CrudRepository<EgresosGerenteModel, String> {

}

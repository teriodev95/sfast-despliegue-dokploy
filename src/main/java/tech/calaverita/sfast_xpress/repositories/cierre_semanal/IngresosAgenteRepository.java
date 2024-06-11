package tech.calaverita.sfast_xpress.repositories.cierre_semanal;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal.IngresosAgenteModel;

@Repository
public interface IngresosAgenteRepository extends CrudRepository<IngresosAgenteModel, String> {

}

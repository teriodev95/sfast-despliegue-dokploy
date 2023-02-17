package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.AsignacionModel;

@Repository
public interface AsignacionRepository extends CrudRepository<AsignacionModel, String> {

}

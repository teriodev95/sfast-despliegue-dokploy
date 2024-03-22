package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.mariaDB.PorcentajesDescuentoLiquidacionesModel;

@Repository
public interface PorcentajesDescuentoLiquidacionesRepository extends CrudRepository<PorcentajesDescuentoLiquidacionesModel, String> {

}

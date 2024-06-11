package tech.calaverita.sfast_xpress.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.sfast_xpress.models.mariaDB.PorcentajesDescuentoLiquidacionesModel;

@Repository
public interface PorcentajesDescuentoLiquidacionesRepository extends CrudRepository<PorcentajesDescuentoLiquidacionesModel, String> {

}

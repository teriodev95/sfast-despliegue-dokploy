package tech.calaverita.reporterloanssql.persistence.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.persistence.entities.PorcentajesDescuentoLiquidacionesEntity;

@Repository
public interface PorcentajesDescuentoLiquidacionesRepository extends CrudRepository<PorcentajesDescuentoLiquidacionesEntity, String> {

}

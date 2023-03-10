package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.repository.CrudRepository;
import tech.calaverita.reporterloanssql.models.LiquidacionModel;

public interface LiquidacionRepository extends CrudRepository<LiquidacionModel, Integer> {
}

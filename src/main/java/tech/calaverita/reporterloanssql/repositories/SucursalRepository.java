package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.SucursalModel;

@Repository
public interface SucursalRepository extends CrudRepository<SucursalModel, Integer> {
    @Query("SELECT suc " +
            "FROM SucursalModel suc " +
            "WHERE suc.sucursalId = :sucursalId")
    SucursalModel findOneBySucursalId(int sucursalId);
}

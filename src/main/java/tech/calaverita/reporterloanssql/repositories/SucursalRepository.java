package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.mariaDB.SucursalModel;

@Repository
public interface SucursalRepository extends CrudRepository<SucursalModel, Integer> {
    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    SucursalModel findBySucursalId(
            String sucursalId
    );
}

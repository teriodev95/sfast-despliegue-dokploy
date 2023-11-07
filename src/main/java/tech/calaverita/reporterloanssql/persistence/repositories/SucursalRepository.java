package tech.calaverita.reporterloanssql.persistence.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.persistence.entities.SucursalEntity;

@Repository
public interface SucursalRepository extends CrudRepository<SucursalEntity, Integer> {
    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @Query("SELECT suc " +
            "FROM SucursalEntity suc " +
            "WHERE suc.sucursalId = :sucursalId")
    SucursalEntity sucEntFindBySucursalId(
            String sucursalId
    );
}

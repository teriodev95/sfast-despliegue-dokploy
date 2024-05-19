package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.mariaDB.ReporteCallCenterLiteModel;
import tech.calaverita.reporterloanssql.models.mariaDB.ReporteCallCenterModel;

import java.util.ArrayList;

@Repository
public interface ReporteCallCenterRepository extends CrudRepository<ReporteCallCenterModel, String> {
    @Query("SELECT rcclm FROM ReporteCallCenterLiteModel rcclm WHERE rcclm.prestamoId = :id")
    ReporteCallCenterLiteModel findLiteModelById(String id);

    @Query("SELECT rcclm FROM ReporteCallCenterLiteModel rcclm WHERE rcclm.gerencia = :gerencia " +
            "AND rcclm.sucursalId = :sucursalId")
    ArrayList<ReporteCallCenterLiteModel> findLiteModelByGerenciaAndSucursalId(String gerencia,
                                                                               String sucursalId);
}

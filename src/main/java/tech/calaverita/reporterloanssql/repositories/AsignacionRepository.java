package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.mariaDB.AsignacionModel;

import java.util.ArrayList;

@Repository
public interface AsignacionRepository extends CrudRepository<AsignacionModel, String> {
    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @Query("SELECT am " +
            "FROM AsignacionModel am " +
            "WHERE am.agencia = :agencia " +
            "AND am.anio = :anio " +
            "AND am.semana = :semana")
    ArrayList<AsignacionModel> darrasignModFindByAgenciaAnioAndSemana(
            String agencia,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT asi " +
            "FROM AsignacionModel asi " +
            "WHERE asi.agencia = :agencia " +
            "AND asi.anio = :anio " +
            "AND asi.semana = :semana")
    ArrayList<AsignacionModel> darrasignModFindByAgenciaAnioAndSemanaToDashboard(
            String agencia,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query(value = "SELECT IF(SUM(am.monto) is null, 0, SUM(am.monto)) " +
            "FROM asignaciones am " +
            "WHERE am.agencia = :agencia " +
            "AND am.anio = :anio " +
            "AND am.semana = :semana", nativeQuery = true)
    double getSumaDeAsigancionesByAgenciaAnioAndSemana(
            String agencia,
            int anio,
            int semana
    );
}

package tech.calaverita.reporterloanssql.persistence.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.persistence.entities.AsignacionEntity;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface AsignacionRepository extends CrudRepository<AsignacionEntity, String> {
    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @Query("SELECT am " +
            "FROM AsignacionEntity am " +
            "WHERE am.agencia = :agencia " +
            "AND am.anio = :anio " +
            "AND am.semana = :semana")
    ArrayList<AsignacionEntity> darrasignModFindByAgenciaAnioAndSemana(
            String agencia,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT asi " +
            "FROM AsignacionEntity asi " +
            "WHERE asi.agencia = :agencia " +
            "AND asi.anio = :anio " +
            "AND asi.semana = :semana")
    ArrayList<AsignacionEntity> darrasignModFindByAgenciaAnioAndSemanaToDashboard(
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

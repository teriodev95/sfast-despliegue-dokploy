package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.AsignacionModel;

import java.util.ArrayList;

@Repository
public interface AsignacionRepository extends CrudRepository<AsignacionModel, String> {
    @Query("SELECT am FROM AsignacionModel am WHERE am.agencia = :agencia AND am.anio = :anio AND am.semana = :semana")
    ArrayList<AsignacionModel> findManyByAgenciaAnioAndSemana(String agencia, int anio, int semana);

    @Query("SELECT SUM(am.monto) FROM AsignacionModel am WHERE am.agencia = :agencia AND am.anio = :anio AND am.semana = :semana")
    Double getSumaDeAsigancionesByAgenciaAnioAndSemana(String agencia, int anio, int semana);

    @Query("SELECT asi " +
            "FROM AsignacionModel asi " +
            "WHERE asi.agencia = :agencia " +
            "AND asi.anio = :anio " +
            "AND asi.semana = :semana")
    ArrayList<AsignacionModel> getAsignacionesByAgenciaAnioAndSemanaToDashboard(String agencia, int anio, int semana);
}

package tech.calaverita.reporterloanssql.repositories;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.mariaDB.AsignacionModel;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface AsignacionRepository extends CrudRepository<AsignacionModel, String> {
    ArrayList<AsignacionModel> findByAgenciaAndAnioAndSemana(String agencia, int anio, int semana);

    @Query("SELECT IF((SUM(asign.monto) IS NULL), 0, SUM(asign.monto)) FROM AsignacionModel asign " +
            "WHERE asign.agencia = :agencia AND asign.anio = :anio AND asign.semana = :semana")
    double findSumaAsigancionesByAgenciaAnioAndSemana(String agencia, int anio, int semana);
}

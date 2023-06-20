package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.VisitaModel;

import java.util.ArrayList;

@Repository
public interface VisitaRepository extends CrudRepository<VisitaModel, String> {
    @Query("SELECT vi FROM VisitaModel vi")
    ArrayList<VisitaModel> getVisitaModels();

    @Query("SELECT vi FROM VisitaModel vi WHERE vi.visitaId = :visitaId")
    VisitaModel getVisitaModelByVisitaId(String visitaId);

    @Query("SELECT vi FROM VisitaModel vi WHERE vi.prestamoId = :prestamoId")
    ArrayList<VisitaModel> getVisitaModelsByPrestamoId(String prestamoId);

    @Query("SELECT vis FROM VisitaModel vis WHERE vis.agente = :agencia AND vis.anio = :anio AND vis.semana = :semana")
    ArrayList<VisitaModel> findVisitaModelsByAgenciaAnioAndSemana(String agencia, int anio, int semana);

    @Query("SELECT vis FROM VisitaModel vis WHERE vis.prestamoId = :prestamoId AND vis.anio = :anio AND vis.semana = :semana")
    ArrayList<VisitaModel> findVisitaModelsByPrestamoIdAnioAndSemana(String prestamoId, int anio, int semana);
}

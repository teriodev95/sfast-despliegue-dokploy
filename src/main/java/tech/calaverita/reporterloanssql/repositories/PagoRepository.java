package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.controllers.PagoController;
import tech.calaverita.reporterloanssql.models.PagoModel;

import java.util.ArrayList;

@Repository
public interface PagoRepository extends CrudRepository<PagoModel, String> {
    @Query("SELECT pa FROM PagoModel pa WHERE pa.prestamoId = :prestamoId AND pa.anio = :anio AND pa.semana = :semana")
    PagoModel getPagoModelByPrestamoIdAnioAndSemana(String prestamoId, int anio, int semana);

    @Query("SELECT pa FROM PagoModel pa WHERE pa.agente = :agencia AND pa.anio = :anio AND pa.semana = :semana AND pa.esPrimerPago = false")
    ArrayList<PagoModel> getPagoModelsByAgenciaAnioAndSemana(String agencia, int anio, int semana);

    @Query("SELECT pa FROM PagoModel pa WHERE pa.prestamoId = :prestamoId ORDER BY pa.anio ASC, pa.semana ASC")
    ArrayList<PagoModel> getHistorialDePagos(String prestamoId);
}

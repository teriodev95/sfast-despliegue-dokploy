package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.PagoModel;

import java.util.ArrayList;

@Repository
public interface PagoRepository extends CrudRepository<PagoModel, String> {
    @Query("SELECT pago FROM PagoModel pago WHERE pago.prestamoId = :prestamoId AND pago.anio = :anio AND pago.semana = :semana")
    PagoModel getPagoModelByPrestamoIdAnioAndSemana(String prestamoId, int anio, int semana);

    @Query("SELECT pago FROM PagoModel pago WHERE pago.agente = :agente AND pago.anio = :anio AND pago.semana = :semana AND pago.esPrimerPago = false")
    ArrayList<PagoModel> getPagoModelsByAgenteAnioSemanaAndEsPrimerPago(String agente, int anio, int semana);

    @Query("SELECT pago FROM PagoModel pago WHERE pago.agente = :agente AND pago.anio = :anio AND pago.semana = :semana")
    ArrayList<PagoModel> getPagoModelsByAgenteAnioAndSemana(String agente, int anio, int semana);

    @Query("SELECT pago FROM PagoModel pago WHERE pago.anio = :anio AND pago.semana = :semana")
    ArrayList<PagoModel> getPagoModelsByAnioAndSemana(int anio, int semana);
}

package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import tech.calaverita.reporterloanssql.models.LiquidacionModel;

import java.util.ArrayList;

public interface LiquidacionRepository extends CrudRepository<LiquidacionModel, Integer> {
    @Query("SELECT li " +
            "FROM LiquidacionModel li " +
            "INNER JOIN PagoModel pa " +
            "ON li.pagoId = pa.pagoId " +
            "WHERE pa.agente = :agencia " +
            "AND li.anio = :anio " +
            "AND li.semana = :semana")
    ArrayList<LiquidacionModel> getLiquidacionModelsByAgenciaAnioAndSemanaToDashboard(String agencia, int anio, int semana);

    @Query("SELECT li " +
            "FROM LiquidacionModel li " +
            "INNER JOIN PagoModel pa " +
            "ON li.pagoId = pa.pagoId " +
            "WHERE pa.agente = :agencia " +
            "AND pa.fechaPago like :fechaPago%")
    ArrayList<LiquidacionModel> getLiquidacionModelsByAgenciaAndFechaPagoToDashboard(String agencia, String fechaPago);
}

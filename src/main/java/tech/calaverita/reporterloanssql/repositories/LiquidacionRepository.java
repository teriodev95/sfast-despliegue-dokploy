package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import tech.calaverita.reporterloanssql.models.mariaDB.LiquidacionModel;

import java.util.ArrayList;

public interface LiquidacionRepository extends CrudRepository<LiquidacionModel, Integer> {
    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @Query("SELECT li " +
            "FROM LiquidacionModel li " +
            "INNER JOIN PagoModel pa " +
            "ON li.pagoId = pa.pagoId " +
            "AND pa.agente = :agencia " +
            "AND li.anio = :anio " +
            "AND li.semana = :semana")
    ArrayList<LiquidacionModel> darrliqEntFindByAgenciaAnioAndSemanaToDashboard(
            String agencia,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT li " +
            "FROM LiquidacionModel li " +
            "INNER JOIN PagoModel pa " +
            "ON li.pagoId = pa.pagoId " +
            "AND pa.agente = :agencia " +
            "AND pa.fechaPago like :fechaPago%")
    ArrayList<LiquidacionModel> darrliqEntFindByAgenciaAndFechaPagoToDashboard(
            String agencia,
            String fechaPago
    );
}

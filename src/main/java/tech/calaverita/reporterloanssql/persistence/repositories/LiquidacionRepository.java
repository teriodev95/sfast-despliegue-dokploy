package tech.calaverita.reporterloanssql.persistence.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import tech.calaverita.reporterloanssql.persistence.entities.LiquidacionEntity;

import java.util.ArrayList;

public interface LiquidacionRepository extends CrudRepository<LiquidacionEntity, Integer> {
    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @Query("SELECT li " +
            "FROM LiquidacionEntity li " +
            "INNER JOIN PagoEntity pa " +
            "ON li.pagoId = pa.pagoId " +
            "WHERE pa.agente = :agencia " +
            "AND li.anio = :anio " +
            "AND li.semana = :semana")
    ArrayList<LiquidacionEntity> darrliqEntFindByAgenciaAnioAndSemanaToDashboard(
            String agencia,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT li " +
            "FROM LiquidacionEntity li " +
            "INNER JOIN PagoEntity pa " +
            "ON li.pagoId = pa.pagoId " +
            "WHERE pa.agente = :agencia " +
            "AND pa.fechaPago like :fechaPago%")
    ArrayList<LiquidacionEntity> darrliqEntFindByAgenciaAndFechaPagoToDashboard(
            String agencia,
            String fechaPago
    );
}

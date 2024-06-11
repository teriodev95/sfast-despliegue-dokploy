package tech.calaverita.sfast_xpress.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import tech.calaverita.sfast_xpress.models.mariaDB.LiquidacionModel;

import java.util.ArrayList;

public interface LiquidacionRepository extends CrudRepository<LiquidacionModel, Integer> {
    @Query("SELECT liq FROM LiquidacionModel liq INNER JOIN PagoModel pag ON liq.pagoId = pag.pagoId " +
            "AND pag.agente = :agencia AND liq.anio = :anio AND liq.semana = :semana")
    ArrayList<LiquidacionModel> findByAgenciaAndAnioAndSemana(String agencia, int anio, int semana);

    @Query("SELECT liq FROM LiquidacionModel liq INNER JOIN PagoModel pag ON liq.pagoId = pag.pagoId " +
            "AND pag.agente = :agencia AND pag.fechaPago like :fechaPago%")
    ArrayList<LiquidacionModel> findByAgenciaAndFechaPago(String agencia, String fechaPago);
}

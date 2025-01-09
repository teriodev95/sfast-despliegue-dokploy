package tech.calaverita.sfast_xpress.repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import tech.calaverita.sfast_xpress.models.mariaDB.LiquidacionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.dynamic.PagoDynamicModel;

public interface LiquidacionRepository extends CrudRepository<LiquidacionModel, Integer> {
        @Query("SELECT liq FROM LiquidacionModel liq INNER JOIN PagoModel pag ON liq.prestamoId = pag.prestamoId " +
                        "AND pag.agente = :agencia AND liq.anio = :anio AND liq.semana = :semana")
        ArrayList<LiquidacionModel> findByAgenciaAndAnioAndSemana(String agencia, int anio, int semana);

        @Query("SELECT pag FROM PagoDynamicModel pag WHERE pag.agencia = :agencia AND pag.anio = :anio AND pag.semana = :semana AND pag.tipo = 'Liquidacion'")
        ArrayList<PagoDynamicModel> findPagoModelsByAgenciaAndAnioAndSemana(String agencia, int anio, int semana);

        @Query("SELECT liq FROM LiquidacionModel liq INNER JOIN PagoModel pag ON liq.pagoId = pag.pagoId " +
                        "AND pag.agente IN :agencias AND pag.fechaPago like :fechaPago%")
        ArrayList<LiquidacionModel> findByAgenciaInAndFechaPago(String[] agencias, String fechaPago);
}

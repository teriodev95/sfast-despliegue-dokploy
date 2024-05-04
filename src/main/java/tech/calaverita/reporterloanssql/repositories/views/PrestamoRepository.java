package tech.calaverita.reporterloanssql.repositories.views;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.mariaDB.views.PrestamoModel;

import java.util.ArrayList;

@Repository
public interface PrestamoRepository extends CrudRepository<PrestamoModel, String> {
    @Query("SELECT pr " +
            "FROM PrestamoModel pr " +
            "WHERE pr.prestamoId = :prestamoId")
    PrestamoModel prestEntFindByPrestamoId(
            String prestamoId
    );

    @Query("SELECT pr " +
            "FROM PrestamoModel pr " +
            "INNER JOIN PagoAgrupadoModel pa " +
            "ON pr.prestamoId = pa.prestamoId " +
            "WHERE pa.agente = :agencia " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana " +
            "AND pa.cierraCon > 0")
    ArrayList<PrestamoModel> darrprestEntFindByAgenciaAnioAndSemanaToCobranzaPGS(
            String agencia,
            int anio,
            int semana
    );

    @Query("SELECT prest FROM PrestamoModel prest INNER JOIN PagoModel pag ON prest.prestamoId = pag.prestamoId " +
            "AND pag.agente = :agencia AND pag.anio = :anio AND pag.semana = :semana " +
            "AND prest.saldoAlIniciarSemana <= prest.tarifa")
    ArrayList<PrestamoModel> findPorFinalizarByAgenteAndAnioAndSemana(String agencia, int anio, int semana);

    @Query("SELECT prest FROM PrestamoModel prest INNER JOIN PagoModel pag ON prest.prestamoId = pag.prestamoId " +
            "AND prest.sucursal = :sucursal AND prest.gerencia = :gerencia AND pag.anio = :anio " +
            "AND pag.semana = :semana AND prest.saldoAlIniciarSemana <= prest.tarifa")
    ArrayList<PrestamoModel> findPorFinalizarByGerenciaAndAnioAndSemana(String sucursal, String gerencia, int anio, int semana);
}

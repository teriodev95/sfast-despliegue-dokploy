package tech.calaverita.sfast_xpress.repositories.views;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tech.calaverita.sfast_xpress.models.mariaDB.PrestamoHistorialMigradoModel;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoViewModel;

@Repository
public interface PrestamoViewRepository extends CrudRepository<PrestamoViewModel, String> {
        @Query(value = "SELECT prest.* FROM prestamos_view prest WHERE prest.agencia = :agencia "
                        + "AND prest.saldo_al_iniciar_semana > :saldoAlIniciarSemana AND !(prest.anio = :anio "
                        + "AND prest.semana = :semana) ORDER BY prest.excel_index ASC", nativeQuery = true)
        ArrayList<PrestamoViewModel> findByAgenciaAndSaldoAlIniciarSemanaGreaterThanAndNotAnioAndSemana(String agencia,
                        Double saldoAlIniciarSemana, Integer anio, Integer semana);

        ArrayList<PrestamoViewModel> findByGerenciaAndSucursalAndSaldoAlIniciarSemanaGreaterThan(String gerencia,
                        String sucursal,
                        Double saldoAlIniciarSemana);

        @Query("SELECT prest FROM PrestamoViewModel prest INNER JOIN PagoModel pag ON prest.prestamoId = pag.prestamoId "
                        + "AND pag.agente = :agencia AND pag.anio = :anio AND pag.semana = :semana "
                        + "AND prest.saldoAlIniciarSemana <= prest.tarifa")
        ArrayList<PrestamoViewModel> findPorFinalizarByAgenciaAndAnioAndSemana(String agencia, int anio, int semana);

        @Query("SELECT prest FROM PrestamoViewModel prest INNER JOIN PagoModel pag ON prest.prestamoId = pag.prestamoId "
                        +
                        "AND prest.sucursal = :sucursal AND prest.gerencia = :gerencia AND pag.anio = :anio " +
                        "AND pag.semana = :semana AND prest.saldoAlIniciarSemana <= prest.tarifa")
        ArrayList<PrestamoViewModel> findPorFinalizarByGerenciaAndAnioAndSemana(String sucursal, String gerencia,
                        int anio,
                        int semana);

        @Query("SELECT pr " +
                        "FROM PrestamoViewModel pr " +
                        "INNER JOIN PagoModel pa " +
                        "ON pr.prestamoId = pa.prestamoId " +
                        "WHERE pa.agente = :agencia " +
                        "AND pa.anio = :anio " +
                        "AND pa.semana = :semana - 1 " +
                        "AND pa.cierraCon > 0")
        ArrayList<PrestamoViewModel> darrprestUtilEntFindByAgenciaAnioAndSemanaToCobranza(
                        String agencia,
                        int anio,
                        int semana);

        @Query("SELECT pr " +
                        "FROM PrestamoViewModel pr " +
                        "INNER JOIN PagoModel pa " +
                        "ON pr.prestamoId = pa.prestamoId " +
                        "WHERE pa.agente = :agencia " +
                        "AND pa.anio = :anio " +
                        "AND pa.semana = :semana " +
                        "AND pa.esPrimerPago = false")
        ArrayList<PrestamoViewModel> darrprestUtilEntFindByAgenciaAnioAndSemanaToDashboard(
                        String agencia,
                        int anio,
                        int semana);

        @Query("SELECT distinct(pr)" +
                        "FROM PrestamoViewModel pr " +
                        "INNER JOIN PagoModel pa " +
                        "ON pr.prestamoId = pa.prestamoId " +
                        "WHERE pa.agente = :agencia " +
                        "AND pa.fechaPago <= :fechaPago " +
                        "AND pa.esPrimerPago = false AND pa.anio = :anio AND pa.semana = :semana")
        ArrayList<PrestamoViewModel> darrprestUtilEntByAgenciaAndFechaPagoAndAnioAndSemanaToDashboard(
                        String agencia,
                        String fechaPago, int anio, int semana);

        @Query("SELECT distinct(pr)" +
                        "FROM PrestamoViewModel pr " +
                        "INNER JOIN PagoModel pa " +
                        "ON pr.prestamoId = pa.prestamoId " +
                        "WHERE pa.agente = :agencia " +
                        "AND pa.fechaPago <= :fechaPago " +
                        "AND pa.esPrimerPago = false AND pa.anio = :anio AND pa.semana = :semana")
        ArrayList<PrestamoViewModel> darrprestUtilEntByAgenciaAndFechaPagoLessThanEqualToDashboard(
                        String agencia,
                        String fechaPago, int anio, int semana);

        @Query("SELECT distinct(pr)" +
                        "FROM PrestamoViewModel pr " +
                        "INNER JOIN PagoModel pa " +
                        "ON pr.prestamoId = pa.prestamoId " +
                        "WHERE pr.gerencia = :gerencia " +
                        "AND pr.sucursal = :sucursal " +
                        "AND pa.fechaPago <= :fechaPago " +
                        "AND pa.esPrimerPago = false AND pa.anio = :anio AND pa.semana = :semana")
        ArrayList<PrestamoViewModel> darrprestUtilEntByGerenciaAndSucursalAndFechaPagoLessThanEqualToDashboard(
                        String gerencia, String sucursal,
                        String fechaPago, int anio, int semana);

        PrestamoViewModel findByClienteId(String clienteId);

        ArrayList<PrestamoViewModel> findByAgencia(String agencia);

        @Query("select prest from PrestamoHistorialMigradoModel prest where agencia = :agencia")
        ArrayList<PrestamoHistorialMigradoModel> findHistorialByAgencia(String agencia);

        @Query(value = "SELECT SUM(IF(prest.saldo_al_iniciar_semana < prest.tarifa, prest.saldo_al_iniciar_semana, prest.tarifa)) FROM prestamos_view prest WHERE prest.agencia = :agencia "
                        + "AND prest.saldo_al_iniciar_semana > :saldoAlIniciarSemana AND !(prest.anio = :anio "
                        + "AND prest.semana = :semana)", nativeQuery = true)
        Double findDebitoTotalByAgenciaAndSaldoAlIniciarSemanaGreaterThanAndNotAnioAndSemana(String agencia,
                        Double saldoAlIniciarSemana, Integer anio, Integer semana);

        @Query("SELECT prestView FROM PrestamoViewModel prestView "
                        + "LEFT JOIN PagoDynamicModel pagDyn ON prestView.prestamoId = pagDyn.prestamoId "
                        + "AND pagDyn.anio = :anio AND pagDyn.semana = :semana WHERE prestView.agencia = :agencia "
                        + "AND prestView.saldoAlIniciarSemana > :saldoAlIniciarSemana AND pagDyn.pagoId IS NULL")
        List<PrestamoViewModel> findPrestamosSinPagoByAgenciaAndSaldoAlIniciarSemanaGreaterThanAndAnioAndSemana(
                        String agencia, Double saldoAlIniciarSemana, Integer anio, Integer semana);
}

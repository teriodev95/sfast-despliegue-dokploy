package tech.calaverita.sfast_xpress.repositories.views;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoViewModel;

@Repository
public interface PrestamoViewRepository extends CrudRepository<PrestamoViewModel, String> {
        ArrayList<PrestamoViewModel> findByAgenciaAndSaldoAlIniciarSemanaGreaterThan(String agencia,
                        Double saldoAlIniciarSemana);

        @Query("SELECT prest FROM PrestamoViewModel prest INNER JOIN PagoModel pag ON prest.prestamoId = pag.prestamoId "
                        +
                        "AND pag.agente = :agencia AND pag.anio = :anio AND pag.semana = :semana " +
                        "AND prest.saldoAlIniciarSemana <= prest.tarifa")
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
                        "AND pa.fechaPago like :fechaPago% " +
                        "AND pa.esPrimerPago = false")
        ArrayList<PrestamoViewModel> darrprestUtilEntByAgenciaAndFechaPagoToDashboard(
                        String agencia,
                        String fechaPago);

        PrestamoViewModel findByClienteId(String clienteId);
}

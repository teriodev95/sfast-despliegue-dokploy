package tech.calaverita.sfast_xpress.repositories.dynamic;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tech.calaverita.sfast_xpress.models.mariaDB.dynamic.PagoDynamicModel;

@Repository
public interface PagoDynamicRepository extends CrudRepository<PagoDynamicModel, String> {
        ArrayList<PagoDynamicModel> findByAgenciaAndAnioAndSemanaAndEsPrimerPago(String agencia, int anio, int semana,
                        boolean esPrimerPago);

        @Query("SELECT SUM(pag.tarifa) FROM PagoDynamicModel pag WHERE pag.agencia = :agencia AND pag.anio = :anio " +
                        "AND pag.semana = :semana")
        double findDebitoTotalByAgenciaAndAnioAndSemana(String agencia, int anio, int semana);

        @Query("SELECT SUM(IF((pag.monto > pag.tarifa), pag.monto - pag.tarifa, 0)) FROM PagoDynamicModel pag " +
                        "WHERE pag.agencia = :agencia AND pag.anio = :anio AND pag.semana = :semana")
        double findExcedenteByAgenciaAndAnioAndSemana(String agencia, int anio, int semana);

        @Query("SELECT IFNULL(SUM(pag.monto), 0) FROM PagoDynamicModel pag WHERE pag.agencia = :agencia " +
                        "AND pag.anio = :anio AND pag.semana = :semana")
        double findCobranzaTotalByAgenciaAndAnioAndSemana(String agencia, int anio, int semana);

        @Query("SELECT COUNT(pag) FROM PagoDynamicModel pag WHERE pag.agencia = :agencia AND pag.anio = :anio " +
                        "AND pag.semana = :semana")
        int findClientesCobradosByAgenciaAndAnioAndSemana(String agencia, int anio, int semana);

        @Query(value = "SELECT COUNT(*) FROM pagos_dynamic pag WHERE pag.agencia = :agencia AND pag.anio = :anio " +
                        "AND pag.semana = :semana AND pag.monto >= IF((pag.abre_con > pag.tarifa), pag.tarifa, pag.abre_con)", nativeQuery = true)
        int findClientesPagoCompletoByAgenciaAndAnioAndSemana(String agencia, int anio, int semana);

        @Query("SELECT COUNT(pag) FROM PagoDynamicModel pag WHERE pag.agencia = :agencia AND pag.anio = :anio " +
                        "AND pag.semana = :semana AND pag.esPrimerPago = false")
        int findClientesTotalesByAgenciaAndAnioAndSemana(String agencia, int anio, int semana);

        @Query("SELECT SUM(IF((pag.cierraCon > pag.tarifa), pag.tarifa, pag.cierraCon)) FROM PagoDynamicModel pag " +
                        "INNER JOIN PrestamoViewModel prest ON pag.prestamoId = prest.prestamoId AND prest.gerencia = :gerencia "
                        +
                        "AND prest.sucursal = :sucursal AND pag.anio = :anio AND pag.semana = :semana")
        double findDebitoTotalSemanaByGerenciaAndSucursalAndAnioAndSemanaInnerJoinPrestamoModel(String gerencia,
                        String sucursal,
                        int anio, int semana);

        @Query("SELECT SUM(IF((pag.monto > pag.tarifa), pag.monto - pag.tarifa, 0)) FROM PagoDynamicModel pag " +
                        "INNER JOIN PrestamoViewModel prest ON pag.prestamoId = prest.prestamoId AND prest.gerencia = :gerencia "
                        +
                        "AND prest.sucursal = :sucursal AND pag.anio = :anio AND pag.semana = :semana AND pag.esPrimerPago = false")
        double findExcedenteByGerenciaAndSucursalAndAnioAndSemanaInnerJoinPrestamoModel(String gerencia,
                        String sucursal,
                        int anio, int semana);

        @Query("SELECT SUM(pag.monto) FROM PagoDynamicModel pag INNER JOIN PrestamoViewModel prest " +
                        "ON pag.prestamoId = prest.prestamoId AND prest.gerencia = :gerencia AND prest.sucursal = :sucursal "
                        +
                        "AND pag.anio = :anio AND pag.semana = :semana AND pag.esPrimerPago = false")
        double findCobranzaTotalByGerenciaAndSucursalAndAnioAndSemanaInnerJoinPrestamoModel(String gerencia,
                        String sucursal, int anio, int semana);

        @Query("SELECT SUM(IF((pag.cierraCon > pag.tarifa), pag.tarifa, pag.cierraCon)) FROM PagoDynamicModel pag " +
                        "INNER JOIN PrestamoViewModel prest ON pag.prestamoId = prest.prestamoId AND prest.gerencia = :gerencia "
                        +
                        "AND prest.sucursal = :sucursal AND pag.anio = :anio AND pag.semana = :semana " +
                        "AND prest.diaDePago = 'MIERCOLES'")
        double findDebitoTotalParcialByGerenciaAndSucursalAndAnioAndSemanaInnerJoinPrestamoModel(String gerencia,
                        String sucursal, int anio,
                        int semana);

        @Query("SELECT SUM(IF((pag.monto > pag.tarifa), pag.monto - pag.tarifa, 0)) FROM PagoDynamicModel pag " +
                        "INNER JOIN PrestamoViewModel prest ON pag.prestamoId = prest.prestamoId AND prest.gerencia = :gerencia "
                        +
                        "AND prest.sucursal = :sucursal AND pag.anio = :anio AND pag.semana = :semana AND pag.fechaPago < :fecha "
                        +
                        "AND pag.esPrimerPago = false")
        double findExcedenteByGerenciaAndSucursalAndAnioAndFechaInnerJoinPrestamoModel(String gerencia, String sucursal,
                        int anio, int semana, String fecha);

        @Query("SELECT SUM(pag.monto) FROM PagoDynamicModel pag INNER JOIN PrestamoViewModel prest " +
                        "ON pag.prestamoId = prest.prestamoId AND prest.gerencia = :gerencia AND prest.sucursal = :sucursal "
                        +
                        "AND pag.anio = :anio AND pag.semana = :semana AND pag.fechaPago < :fecha AND pag.esPrimerPago = false")
        double findCobranzaTotalByGerenciaAndSucursalAndAnioAndSemanaAndFechaInnerJoinPrestamoModel(String gerencia,
                        String sucursal, int anio, int semana, String fecha);

        @Query("SELECT COUNT(pag) FROM PagoDynamicModel pag INNER JOIN PrestamoViewModel prest " +
                        "ON pag.prestamoId = prest.prestamoId AND prest.gerencia = :gerencia AND prest.sucursal = :sucursal "
                        +
                        "AND pag.anio = :anio AND pag.semana = :semana AND pag.fechaPago < :fecha AND pag.esPrimerPago = false")
        int findClientesCobradosByGerenciaAndSucursalAndAnioAndSemanaAndFechaInnerJoinPrestamoModel(String gerencia,
                        String sucursal,
                        int anio, int semana,
                        String fecha);

        ArrayList<PagoDynamicModel> findByAgenciaAndAnioAndSemanaAndCierraConGreaterThan(String agencia, int anio,
                        int semana,
                        double cierraConGreaterThan);

        PagoDynamicModel findByPrestamoIdAndAnioAndSemana(String prestamoId, int anio, int semana);
}

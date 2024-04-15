package tech.calaverita.reporterloanssql.repositories.views;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.mariaDB.views.PagoAgrupadoModel;

import java.util.ArrayList;

@Repository
public interface PagoAgrupadoRepository extends CrudRepository<PagoAgrupadoModel, String> {
    ArrayList<PagoAgrupadoModel> findByPrestamoIdOrderByAnioAscSemanaAsc(String prestamoId);

    @Query("SELECT SUM(pag.tarifa) FROM PagoAgrupadoModel pag WHERE pag.agente = :agencia AND pag.anio = :anio " +
            "AND pag.semana = :semana")
    double findDebitoTotalByAgenteAndAnioAndSemana(String agencia, int anio, int semana);

    @Query("SELECT SUM(IF((pag.monto > pag.tarifa), pag.monto - pag.tarifa, 0)) FROM PagoAgrupadoModel pag " +
            "WHERE pag.agente = :agencia AND pag.anio = :anio AND pag.semana = :semana")
    double findExcedenteByAgenteAndAnioAndSemana(String agencia, int anio, int semana);

    @Query("SELECT SUM(pag.monto) FROM PagoAgrupadoModel pag WHERE pag.agente = :agencia AND pag.anio = :anio " +
            "AND pag.semana = :semana")
    double findCobranzaTotalByAgenteAndAnioAndSemana(String agencia, int anio, int semana);

    @Query("SELECT COUNT(pag) FROM PagoAgrupadoModel pag WHERE pag.agente = :agencia AND pag.anio = :anio " +
            "AND pag.semana = :semana")
    int findClientesCobradosByAgenteAndAnioAndSemana(String agencia, int anio, int semana);

    @Query(value = "SELECT COUNT(*) FROM pagos_agrupados pag WHERE pag.agente = :agencia AND pag.anio = :anio " +
            "AND pag.semana = :semana AND pag.monto >= IF((pag.abreCon > pag.tarifa), pag.tarifa, pag.abreCon)",
            nativeQuery = true)
    int findClientesPagoCompletoByAgenteAndAnioAndSemana(String agencia, int anio, int semana);

    @Query("SELECT COUNT(pag) FROM PagoAgrupadoModel pag WHERE pag.agente = :agencia AND pag.anio = :anio " +
            "AND pag.semana = :semana AND pag.esPrimerPago = false")
    int findClientesTotalesByAgenteAndAnioAndSemana(String agencia, int anio, int semana);

    @Query("SELECT SUM(IF((pag.cierraCon > pag.tarifa), pag.tarifa, pag.cierraCon)) FROM PagoAgrupadoModel pag " +
            "INNER JOIN PrestamoModel prest ON pag.prestamoId = prest.prestamoId AND prest.gerencia = :gerencia " +
            "AND prest.sucursal = :sucursal AND pag.anio = :anio AND pag.semana = :semana")
    double findDebitoTotalSemanaByGerenciaAndSucursalAndAnioAndSemanaInnerJoinPrestamoModel(String gerencia,
                                                                                            String sucursal,
                                                                                            int anio, int semana);

    @Query("SELECT SUM(IF((pag.monto > pag.tarifa), pag.monto - pag.tarifa, 0)) FROM PagoAgrupadoModel pag " +
            "INNER JOIN PrestamoModel prest ON pag.prestamoId = prest.prestamoId AND prest.gerencia = :gerencia " +
            "AND prest.sucursal = :sucursal AND pag.anio = :anio AND pag.semana = :semana AND pag.esPrimerPago = false")
    double findExcedenteByGerenciaAndSucursalAndAnioAndSemanaInnerJoinPrestamoModel(String gerencia, String sucursal,
                                                                                    int anio, int semana);

    @Query("SELECT SUM(pag.monto) FROM PagoAgrupadoModel pag INNER JOIN PrestamoModel prest " +
            "ON pag.prestamoId = prest.prestamoId AND prest.gerencia = :gerencia AND prest.sucursal = :sucursal " +
            "AND pag.anio = :anio AND pag.semana = :semana AND pag.esPrimerPago = false")
    double findCobranzaTotalByGerenciaAndSucursalAndAnioAndSemanaInnerJoinPrestamoModel(String gerencia, String sucursal, int anio, int semana);

    @Query("SELECT SUM(IF((pag.cierraCon > pag.tarifa), pag.tarifa, pag.cierraCon)) FROM PagoAgrupadoModel pag " +
            "INNER JOIN PrestamoModel prest ON pag.prestamoId = prest.prestamoId AND prest.gerencia = :gerencia " +
            "AND prest.sucursal = :sucursal AND pag.anio = :anio AND pag.semana = :semana " +
            "AND prest.diaDePago = 'MIERCOLES'")
    double findDebitoTotalParcialByGerenciaAndSucursalAndAnioAndSemanaInnerJoinPrestamoModel(String gerencia,
                                                                                             String sucursal, int anio,
                                                                                             int semana);

    @Query("SELECT SUM(IF((pag.monto > pag.tarifa), pag.monto - pag.tarifa, 0)) FROM PagoAgrupadoModel pag " +
            "INNER JOIN PrestamoModel prest ON pag.prestamoId = prest.prestamoId AND prest.gerencia = :gerencia " +
            "AND prest.sucursal = :sucursal AND pag.anio = :anio AND pag.semana = :semana AND pag.fechaPago < :fecha " +
            "AND pag.esPrimerPago = false")
    double findExcedenteByGerenciaAndSucursalAndAnioAndFechaInnerJoinPrestamoModel(String gerencia, String sucursal,
                                                                                   int anio, int semana, String fecha);

    @Query("SELECT SUM(pag.monto) FROM PagoAgrupadoModel pag INNER JOIN PrestamoModel prest " +
            "ON pag.prestamoId = prest.prestamoId AND prest.gerencia = :gerencia AND prest.sucursal = :sucursal " +
            "AND pag.anio = :anio AND pag.semana = :semana AND pag.fechaPago < :fecha AND pag.esPrimerPago = false")
    double findCobranzaTotalByGerenciaAndSucursalAndAnioAndSemanaAndFechaInnerJoinPrestamoModel(String gerencia, String sucursal, int anio, int semana, String fecha);

    @Query("SELECT COUNT(pag) FROM PagoAgrupadoModel pag INNER JOIN PrestamoModel prest " +
            "ON pag.prestamoId = prest.prestamoId AND prest.gerencia = :gerencia AND prest.sucursal = :sucursal " +
            "AND pag.anio = :anio AND pag.semana = :semana AND pag.fechaPago < :fecha AND pag.esPrimerPago = false")
    int findClientesCobradosByGerenciaAndSucursalAndAnioAndSemanaAndFechaInnerJoinPrestamoModel(String gerencia,
                                                                                                String sucursal,
                                                                                                int anio, int semana,
                                                                                                String fecha);
}

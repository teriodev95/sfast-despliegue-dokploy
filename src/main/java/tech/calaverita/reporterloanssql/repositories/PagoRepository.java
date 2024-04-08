package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.mariaDB.PagoModel;

import java.util.ArrayList;

@Repository
public interface PagoRepository extends CrudRepository<PagoModel, String> {
    ArrayList<PagoModel> findByPagoIdAndAnioAndSemanaOrderByFechaPagoDesc(String prestamoId, int anio, int semana);

    ArrayList<PagoModel> findByAgenteAndAnioAndSemanaAndEsPrimerPago(String agencia, int anio, int semana,
                                                                     boolean esPrimerPago);

    ArrayList<PagoModel> findByPrestamoIdAndAnioAndSemanaAndEsPrimerPago(String prestamoId, int anio, int semana, boolean esPrimerPago
    );

    @Query("SELECT SUM(pa.monto) FROM PagoModel pa WHERE pa.prestamoId = :prestamoId")
    double findCobradoByPrestamoId(String prestamoId);

    @Query("SELECT pag FROM PagoModel pag INNER JOIN LiquidacionModel liq ON liq.pagoId = pag.pagoId " +
            "AND pag.agente = :agencia AND pag.fechaPago like :fechaPago%")
    ArrayList<PagoModel> darrpagEntFindByAgenciaAndFechaPagoToDashboard(String agencia, String fechaPago);

    @Query("SELECT pa FROM PrestamoModel pr INNER JOIN PagoModel pa ON pr.prestamoId = pa.prestamoId " +
            "AND pa.agente = :agencia AND pa.fechaPago like :fechaPago% AND pa.esPrimerPago = false")
    ArrayList<PagoModel> darrpagEntFindByAgenciaFechaPagoAndNoPrimerPagoToDashboard(String agencia, String fechaPago);

    @Query("SELECT pag FROM PagoModel pag INNER JOIN LiquidacionModel liq ON liq.pagoId = pag.pagoId " +
            "AND pag.agente = :agencia AND liq.anio = :anio AND liq.semana = :semana")
    ArrayList<PagoModel> darrpagEntFindByAgenciaAnioAndSemanaToDashboard(String agencia, int anio, int semana);

    @Query("SELECT pag FROM PagoModel pag WHERE pag.prestamoId = :prestamoId AND pag.anio = :anio " +
            "AND pag.semana = :semana AND pag.creadoDesde = 'Migracion'")
    PagoModel pagEntFindByPrestamoIdAnioAndSemana(String prestamoId, int anio, int semana);

    @Query("SELECT SUM(pag.tarifa) FROM PagoAgrupadoModel pag WHERE pag.agente = :agencia AND pag.anio = :anio " +
            "AND pag.semana = :semana")
    double getDebitoTotalByAgenciaAnioAndSemana(String agencia, int anio, int semana);

    @Query("SELECT SUM(IF((pag.monto > pag.tarifa), pag.monto - pag.tarifa, 0)) FROM PagoAgrupadoModel pag " +
            "WHERE pag.agente = :agencia AND pag.anio = :anio AND pag.semana = :semana")
    double getExcedenteByAgenciaAnioAndSemana(String agencia, int anio, int semana);

    @Query("SELECT SUM(pag.tarifa) FROM PagoAgrupadoModel pag WHERE pag.agente = :agencia AND pag.anio = :anio " +
            "AND pag.semana = :semana")
    double getCobranzaTotalByAgenciaAnioAndSemana(String agencia, int anio, int semana);

    @Query("SELECT COUNT(pag) FROM PagoAgrupadoModel pag WHERE pag.agente = :agencia AND pag.anio = :anio " +
            "AND pag.semana = :semana")
    int getClientesCobradosAgenciaAnioAndSemana(String agencia, int anio, int semana);

    @Query(value = "SELECT COUNT(pag.*) FROM pagos_agrupados pag WHERE pag.agente = :agencia AND pag.anio = :anio " +
            "AND pag.semana = :semana AND pag.monto >= IF((pag.abreCon > pag.tarifa), pag.tarifa, pag.abreCon)", nativeQuery = true)
    int getClientesPagoCompletoByAgenciaAnioAndSemana(String agencia, int anio, int semana);

    @Query("SELECT COUNT(pag) FROM PagoAgrupadoModel pag WHERE pag.agente = :agencia AND pag.anio = :anio " +
            "AND pag.semana = :semana AND pag.esPrimerPago = false")
    int getClientesTotalesByAgenciaAnioAndSemana(String agencia, int anio, int semana);

    @Query("SELECT SUM(IF((pag.cierraCon > pag.tarifa), pag.tarifa, pag.cierraCon)) FROM PagoAgrupadoModel pag " +
            "INNER JOIN PrestamoModel prest ON pag.prestamoId = prest.prestamoId AND prest.gerencia = :gerencia " +
            "AND prest.sucursal = :sucursal AND pag.anio = :anio AND pag.semana = :semana")
    double getDebitoTotalSemanaByGerenciaAnioAndSemana(String gerencia, String sucursal, int anio, int semana);

    @Query("SELECT SUM(IF((pag.monto > pag.tarifa), pag.monto - pag.tarifa, 0)) FROM PagoAgrupadoModel pag " +
            "INNER JOIN PrestamoModel prest ON pag.prestamoId = prest.prestamoId AND prest.gerencia = :gerencia " +
            "AND prest.sucursal = :sucursal AND pag.anio = :anio AND pag.semana = :semana AND pag.esPrimerPago = false")
    double getExcedenteByGerenciaAnioAndSemana(String gerencia, String sucursal, int anio, int semana);

    @Query("SELECT SUM(pag.monto) FROM PagoAgrupadoModel pag INNER JOIN PrestamoModel prest " +
            "ON pag.prestamoId = prest.prestamoId AND prest.gerencia = :gerencia AND prest.sucursal = :sucursal " +
            "AND pag.anio = :anio AND pag.semana = :semana AND pag.esPrimerPago = false")
    double getCobranzaTotalByGerenciaAnioAndSemana(String gerencia, String sucursal, int anio, int semana);

    @Query("SELECT SUM(IF((pag.cierraCon > pag.tarifa), pag.tarifa, pag.cierraCon)) FROM PagoAgrupadoModel pag " +
            "INNER JOIN PrestamoModel prest ON pag.prestamoId = prest.prestamoId AND prest.gerencia = :gerencia " +
            "AND prest.sucursal = :sucursal AND pag.anio = :anio AND pag.semana = :semana " +
            "AND prest.diaDePago = 'MIERCOLES'")
    double getDebitoTotalParcialByGerenciaAnioSemana(String gerencia, String sucursal, int anio, int semana);

    @Query("SELECT SUM(IF((pag.cierraCon > pag.tarifa), pag.tarifa, pag.cierraCon)) FROM PagoAgrupadoModel pag " +
            "INNER JOIN PrestamoModel prest ON pag.prestamoId = prest.prestamoId AND prest.gerencia = :gerencia " +
            "AND prest.sucursal = :sucursal AND pag.anio = :anio AND pag.semana = :semana")
    double getDebitoTotalSemanaByGerenciaAnioSemana(String gerencia, String sucursal, int anio, int semana);

    @Query("SELECT SUM(IF((pag.monto > pag.tarifa), pag.monto - pag.tarifa, 0)) FROM PagoAgrupadoModel pag " +
            "INNER JOIN PrestamoModel prest ON pag.prestamoId = prest.prestamoId AND prest.gerencia = :gerencia " +
            "AND prest.sucursal = :sucursal AND pag.anio = :anio AND pag.semana = :semana AND pag.fechaPago < :fecha " +
            "AND pag.esPrimerPago = false")
    double getExcedenteByGerenciaAnioSemanaAndFecha(String gerencia, String sucursal, int anio, int semana, String fecha);

    @Query("SELECT SUM(pag.monto) FROM PagoAgrupadoModel pag INNER JOIN PrestamoModel prest " +
            "ON pag.prestamoId = prest.prestamoId AND prest.gerencia = :gerencia AND prest.sucursal = :sucursal " +
            "AND pag.anio = :anio AND pag.semana = :semana AND pag.fechaPago < :fecha AND pag.esPrimerPago = false")
    double getCobranzaTotalByGerenciaAnioSemanaAndFecha(String gerencia, String sucursal, int anio, int semana, String fecha);

    @Query("SELECT COUNT(pag) FROM PagoAgrupadoModel pag INNER JOIN PrestamoModel prest " +
            "ON pag.prestamoId = prest.prestamoId AND prest.gerencia = :gerencia AND prest.sucursal = :sucursal " +
            "AND pag.anio = :anio AND pag.semana = :semana AND pag.fechaPago < :fecha AND pag.esPrimerPago = false")
    int getClientesCobradosGerenciaAnioSemanaAndFecha(String gerencia, String sucursal, int anio, int semana, String fecha);

    @Query("SELECT pag FROM PagoModel pag INNER JOIN AgenciaModel agenc ON agenc.id = pag.agente AND pag.tipo = :tipo " +
            "AND pag.anio = :anio AND pag.semana = :semana AND agenc.gerenciaId in :gerencias GROUP BY pag.prestamoId")
    ArrayList<PagoModel> findByGerenciasAndAnioAndSemanaAndTipo(ArrayList<String> gerencias, int anio, int semana, String tipo);
}

package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.mariaDB.PagoModel;
import tech.calaverita.reporterloanssql.models.view.PagoAgrupadoModel;
import tech.calaverita.reporterloanssql.models.view.PagoUtilModel;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface PagoRepository extends CrudRepository<PagoModel, String> {
    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @Query("SELECT pa " +
            "FROM PagoModel pa " +
            "WHERE pa.pagoId = :id")
    Optional<PagoModel> optpagEntFindById(
            String id
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT pa " +
            "FROM PagoModel pa " +
            "WHERE pa.prestamoId = :prestamoId " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana " +
            "ORDER BY pa.fechaPago desc")
    ArrayList<PagoModel> darrpagEntFindByPrestamoIdAnioAndSemana(
            String prestamoId,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT pa " +
            "FROM PagoModel pa " +
            "WHERE pa.agente = :agencia " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana " +
            "AND pa.esPrimerPago = false")
    ArrayList<PagoModel> darrpagEntFindByAgenciaAnioAndSemana(
            String agencia,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT pa " +
            "FROM PagoModel pa " +
            "WHERE pa.prestamoId = :prestamoId " +
            "AND pa.esPrimerPago = false")
    ArrayList<PagoModel> darrpagEntFindByPrestamoId(
            String prestamoId
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT pa " +
            "FROM PagoModel pa " +
            "WHERE pa.prestamoId = :prestamoId " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana " +
            "AND pa.esPrimerPago = false")
    ArrayList<PagoModel> darrpagEntFindByPrestamoIdAnioSemanaAndNoPrimerPago(
            String prestamoId,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT pa " +
            "FROM PagoAgrupadoModel pa " +
            "WHERE pa.agente = :agencia " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana " +
            "AND pa.esPrimerPago = false")
    ArrayList<PagoAgrupadoModel> darrpagAgrEntFindByAgenciaAnioAndSemana(
            String agencia,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT pa " +
            "FROM PagoAgrupadoModel pa " +
            "WHERE pa.prestamoId = :prestamoId " +
            "AND pa.esPrimerPago = false " +
            "ORDER BY pa.anio DESC, pa.semana DESC")
    ArrayList<PagoAgrupadoModel> darrpagAgrEntFindByPrestamoId(
            String prestamoId
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT pa " +
            "FROM PagoAgrupadoModel pa " +
            "WHERE pa.prestamoId = :prestamoId " +
            "ORDER BY pa.anio ASC, pa.semana ASC")
    ArrayList<PagoAgrupadoModel> darrpagAgrEntGetHistorialDePagosToApp(
            String prestamoId
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT pa " +
            "FROM PagoAgrupadoModel pa " +
            "WHERE pa.prestamoId = :prestamoId " +
            "ORDER BY pa.anio DESC, pa.semana DESC")
    ArrayList<PagoAgrupadoModel> darrpagAgrEntGetHistorialDePagosToPGS(
            String prestamoId
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT SUM(pa.monto) " +
            "FROM PagoModel pa " +
            "WHERE pa.prestamoId = :prestamoId")
    double numGetSaldoFromPrestamoByPrestamoId(
            String prestamoId
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT pa " +
            "FROM PrestamoModel pr " +
            "INNER JOIN PagoUtilModel pa " +
            "ON pr.prestamoId = pa.prestamoId " +
            "WHERE pa.agente = :agencia " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana " +
            "AND pa.cierraCon > 0")
    ArrayList<PagoUtilModel> darrpagUtilEntFindByAgenciaAnioAndSemanaToCobranza(
            String agencia,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT pa " +
            "FROM PrestamoModel pr " +
            "INNER JOIN PagoUtilModel pa " +
            "ON pr.prestamoId = pa.prestamoId " +
            "WHERE pa.agente = :agencia " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana " +
            "AND pa.esPrimerPago = false")
    ArrayList<PagoUtilModel> darrpagUtilEntFindByAgenciaAnioAndSemanaToDashboard(
            String agencia,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT pa " +
            "FROM PagoModel pa " +
            "INNER JOIN LiquidacionModel li " +
            "ON li.pagoId = pa.pagoId " +
            "AND pa.agente = :agencia " +
            "AND pa.fechaPago like :fechaPago%")
    ArrayList<PagoModel> darrpagEntFindByAgenciaAndFechaPagoToDashboard(
            String agencia,
            String fechaPago
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT pa " +
            "FROM PrestamoModel pr " +
            "INNER JOIN PagoModel pa " +
            "ON pr.prestamoId = pa.prestamoId " +
            "WHERE pa.agente = :agencia " +
            "AND pa.fechaPago like :fechaPago% " +
            "AND pa.esPrimerPago = false")
    ArrayList<PagoModel> darrpagEntFindByAgenciaFechaPagoAndNoPrimerPagoToDashboard(
            String agencia,
            String fechaPago
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT pa " +
            "FROM PagoModel pa " +
            "INNER JOIN LiquidacionModel li " +
            "ON li.pagoId = pa.pagoId " +
            "AND pa.agente = :agencia " +
            "AND li.anio = :anio " +
            "AND li.semana = :semana")
    ArrayList<PagoModel> darrpagEntFindByAgenciaAnioAndSemanaToDashboard(
            String agencia,
            int anio,
            int semana);

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT pa " +
            "FROM PagoModel pa " +
            "WHERE pa.prestamoId = :prestamoId " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana " +
            "AND pa.creadoDesde = 'Migracion'")
    PagoModel pagEntFindByPrestamoIdAnioAndSemana(
            String prestamoId,
            int anio,
            int semana);

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT SUM(pag.tarifa) " +
            "FROM PagoAgrupadoModel pag " +
            "WHERE pag.agente = :agencia " +
            "AND pag.anio = :anio " +
            "AND pag.semana = :semana")
    Double getDebitoTotalByAgenciaAnioAndSemana(
            String agencia,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT SUM(IF((pag.monto > pag.tarifa), pag.monto - pag.tarifa, 0)) " +
            "FROM PagoAgrupadoModel pag " +
            "WHERE pag.agente = :agencia " +
            "AND pag.anio = :anio " +
            "AND pag.semana = :semana")
    Double getExcedenteByAgenciaAnioAndSemana(
            String agencia,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT SUM(pag.tarifa) " +
            "FROM PagoAgrupadoModel pag " +
            "WHERE pag.agente = :agencia " +
            "AND pag.anio = :anio " +
            "AND pag.semana = :semana")
    Double getCobranzaTotalByAgenciaAnioAndSemana(
            String agencia,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT COUNT(pag) " +
            "FROM PagoAgrupadoModel pag " +
            "WHERE pag.agente = :agencia " +
            "AND pag.anio = :anio " +
            "AND pag.semana = :semana")
    Integer getClientesCobradosAgenciaAnioAndSemana(
            String agencia,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query(value = "SELECT COUNT(*) " +
            "FROM pagos_agrupados pag " +
            "WHERE pag.agente = :agencia " +
            "AND pag.anio = :anio " +
            "AND pag.semana = :semana " +
            "AND pag.monto >= IF((pag.abreCon > pag.tarifa), pag.tarifa, pag.abreCon)", nativeQuery = true)
    Integer getClientesPagoCompletoByAgenciaAnioAndSemana(
            String agencia,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT COUNT(pag) " +
            "FROM PagoAgrupadoModel pag " +
            "WHERE pag.agente = :agencia " +
            "AND pag.anio = :anio " +
            "AND pag.semana = :semana " +
            "AND pag.esPrimerPago = false")
    Integer getClientesTotalesByAgenciaAnioAndSemana(
            String agencia,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT SUM(IF((pag.abreCon > pag.tarifa), pag.tarifa, pag.abreCon)) " +
            "FROM PagoAgrupadoModel pag " +
            "INNER JOIN PrestamoModel prest " +
            "ON pag.prestamoId = prest.prestamoId " +
            "AND prest.gerencia = :gerencia " +
            "AND prest.sucursal = :sucursal " +
            "AND pag.anio = :anio " +
            "AND pag.semana = :semana " +
            "AND pag.esPrimerPago = false")
    Double getDebitoTotalParcialByGerenciaAnioAndSemana(
            String gerencia,
            String sucursal,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT SUM(IF((pag.cierraCon > pag.tarifa), pag.tarifa, pag.cierraCon)) " +
            "FROM PagoAgrupadoModel pag " +
            "INNER JOIN PrestamoModel prest " +
            "ON pag.prestamoId = prest.prestamoId " +
            "AND prest.gerencia = :gerencia " +
            "AND prest.sucursal = :sucursal " +
            "AND pag.anio = :anio " +
            "AND pag.semana = :semana")
    Double getDebitoTotalSemanaByGerenciaAnioAndSemana(
            String gerencia,
            String sucursal,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT SUM(IF((pag.monto > pag.tarifa), pag.monto - pag.tarifa, 0)) " +
            "FROM PagoAgrupadoModel pag " +
            "INNER JOIN PrestamoModel prest " +
            "ON pag.prestamoId = prest.prestamoId " +
            "AND prest.gerencia = :gerencia " +
            "AND prest.sucursal = :sucursal " +
            "AND pag.anio = :anio " +
            "AND pag.semana = :semana " +
            "AND pag.esPrimerPago = false")
    Double getExcedenteByGerenciaAnioAndSemana(
            String gerencia,
            String sucursal,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT SUM(pag.monto) " +
            "FROM PagoAgrupadoModel pag " +
            "INNER JOIN PrestamoModel prest " +
            "ON pag.prestamoId = prest.prestamoId " +
            "AND prest.gerencia = :gerencia " +
            "AND prest.sucursal = :sucursal " +
            "AND pag.anio = :anio " +
            "AND pag.semana = :semana " +
            "AND pag.esPrimerPago = false")
    Double getCobranzaTotalByGerenciaAnioAndSemana(
            String gerencia,
            String sucursal,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT COUNT(pag) " +
            "FROM PagoAgrupadoModel pag " +
            "INNER JOIN PrestamoModel prest " +
            "ON pag.prestamoId = prest.prestamoId " +
            "AND prest.gerencia = :gerencia " +
            "AND prest.sucursal = :sucursal " +
            "AND pag.anio = :anio " +
            "AND pag.semana = :semana " +
            "AND pag.esPrimerPago = false")
    Integer getClientesCobradosGerenciaAnioAndSemana(
            String gerencia,
            String sucursal,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT COUNT(pag) " +
            "FROM PagoAgrupadoModel pag " +
            "INNER JOIN PrestamoModel prest " +
            "ON pag.prestamoId = prest.prestamoId " +
            "AND prest.gerencia = :gerencia " +
            "AND prest.sucursal = :sucursal " +
            "AND pag.anio = :anio " +
            "AND pag.semana = :semana " +
            "AND pag.esPrimerPago = false")
    Integer getClientesTotalesGerenciaAnioAndSemana(
            String gerencia,
            String sucursal,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT SUM(IF((pag.cierraCon > pag.tarifa), pag.tarifa, pag.cierraCon)) " +
            "FROM PagoAgrupadoModel pag " +
            "INNER JOIN PrestamoModel prest " +
            "ON pag.prestamoId = prest.prestamoId " +
            "AND prest.gerencia = :gerencia " +
            "AND prest.sucursal = :sucursal " +
            "AND pag.anio = :anio " +
            "AND pag.semana = :semana " +
            "AND prest.diaDePago = 'MIERCOLES'")
    Double getDebitoTotalParcialByGerenciaAnioSemana(
            String gerencia,
            String sucursal,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT SUM(IF((pag.cierraCon > pag.tarifa), pag.tarifa, pag.cierraCon)) " +
            "FROM PagoAgrupadoModel pag " +
            "INNER JOIN PrestamoModel prest " +
            "ON pag.prestamoId = prest.prestamoId " +
            "AND prest.gerencia = :gerencia " +
            "AND prest.sucursal = :sucursal " +
            "AND pag.anio = :anio " +
            "AND pag.semana = :semana")
    Double getDebitoTotalSemanaByGerenciaAnioSemana(
            String gerencia,
            String sucursal,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT SUM(IF((pag.monto > pag.tarifa), pag.monto - pag.tarifa, 0)) " +
            "FROM PagoAgrupadoModel pag " +
            "INNER JOIN PrestamoModel prest " +
            "ON pag.prestamoId = prest.prestamoId " +
            "AND prest.gerencia = :gerencia " +
            "AND prest.sucursal = :sucursal " +
            "AND pag.anio = :anio " +
            "AND pag.semana = :semana " +
            "AND pag.fechaPago < :fecha " +
            "AND pag.esPrimerPago = false")
    Double getExcedenteByGerenciaAnioSemanaAndFecha(
            String gerencia,
            String sucursal,
            int anio,
            int semana,
            String fecha
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT SUM(pag.monto) " +
            "FROM PagoAgrupadoModel pag " +
            "INNER JOIN PrestamoModel prest " +
            "ON pag.prestamoId = prest.prestamoId " +
            "AND prest.gerencia = :gerencia " +
            "AND prest.sucursal = :sucursal " +
            "AND pag.anio = :anio " +
            "AND pag.semana = :semana " +
            "AND pag.fechaPago < :fecha " +
            "AND pag.esPrimerPago = false")
    Double getCobranzaTotalByGerenciaAnioSemanaAndFecha(
            String gerencia,
            String sucursal,
            int anio,
            int semana,
            String fecha
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT COUNT(pag) " +
            "FROM PagoAgrupadoModel pag " +
            "INNER JOIN PrestamoModel prest " +
            "ON pag.prestamoId = prest.prestamoId " +
            "AND prest.gerencia = :gerencia " +
            "AND prest.sucursal = :sucursal " +
            "AND pag.anio = :anio " +
            "AND pag.semana = :semana " +
            "AND pag.fechaPago < :fecha " +
            "AND pag.esPrimerPago = false")
    Integer getClientesCobradosGerenciaAnioSemanaAndFecha(
            String gerencia,
            String sucursal,
            int anio,
            int semana,
            String fecha
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT COUNT(pag) " +
            "FROM PagoAgrupadoModel pag " +
            "INNER JOIN PrestamoModel prest " +
            "ON pag.prestamoId = prest.prestamoId " +
            "AND prest.gerencia = :gerencia " +
            "AND prest.sucursal = :sucursal " +
            "AND pag.anio = :anio " +
            "AND pag.semana = :semana " +
            "AND pag.fechaPago < :fecha " +
            "AND pag.esPrimerPago = false")
    Integer getClientesTotalesGerenciaAnioSemanaAndFecha(
            String gerencia,
            String sucursal,
            int anio,
            int semana,
            String fecha
    );

    @Query(value = "select * from pagos_v3 pag " +
            "inner join agencias agenc " +
            "on agenc.AgenciaID = pag.Agente " +
            "and pag.tipo = :tipo " +
            "and pag.anio = :anio " +
            "and pag.semana = :semana " +
            "and agenc.GerenciaID in :gerencias " +
            "group by pag.prestamoId", nativeQuery = true)
    ArrayList<PagoModel> findByGerenciasAndAnioAndSemanaAndTipo(ArrayList<String> gerencias, int anio, int semana, String tipo);
}

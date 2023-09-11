package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.PagoAgrupadoModel;
import tech.calaverita.reporterloanssql.models.PagoModel;
import tech.calaverita.reporterloanssql.models.PagoUtilModel;

import java.util.ArrayList;

@Repository
public interface PagoRepository extends CrudRepository<PagoModel, String> {
    @Query("SELECT pa FROM PagoModel pa WHERE pa.prestamoId = :prestamoId AND pa.anio = :anio AND pa.semana = :semana order by pa.fechaPago desc")
    ArrayList<PagoModel> getPagoModelByPrestamoIdAnioAndSemana(String prestamoId, int anio, int semana);

    @Query("SELECT pa FROM PagoModel pa WHERE pa.agente = :agencia AND pa.anio = :anio AND pa.semana = :semana AND pa.esPrimerPago = false")
    ArrayList<PagoModel> findPagoModelsByAgenciaAnioAndSemana(String agencia, int anio, int semana);

    @Query("SELECT pa FROM PagoModel pa WHERE pa.prestamoId = :prestamoId AND pa.esPrimerPago = false")
    ArrayList<PagoModel> findPagoModelsByPrestamoId(String prestamoId);

    @Query("SELECT pa FROM PagoModel pa WHERE pa.prestamoId = :prestamoId AND pa.anio = :anio AND pa.semana = :semana AND pa.esPrimerPago = false")
    ArrayList<PagoModel> findPagoModelsByPrestamoIdAnioAndSemana(String prestamoId, int anio, int semana);

    @Query("SELECT pa FROM PagoAgrupadoModel pa WHERE pa.agente = :agencia AND pa.anio = :anio AND pa.semana = :semana AND pa.esPrimerPago = false")
    ArrayList<PagoAgrupadoModel> findPagoVistaModelsByAgenciaAnioAndSemana(String agencia, int anio, int semana);

    @Query("SELECT pa FROM PagoAgrupadoModel pa WHERE pa.prestamoId = :prestamoId AND pa.esPrimerPago = false ORDER BY pa.anio DESC, pa.semana DESC")
    ArrayList<PagoAgrupadoModel> findPagoVistaModelsByPrestamoId(String prestamoId);

    @Query("SELECT pa FROM PagoAgrupadoModel pa WHERE pa.prestamoId = :prestamoId ORDER BY pa.anio ASC, pa.semana ASC")
    ArrayList<PagoAgrupadoModel> getHistorialDePagosToApp(String prestamoId);

    @Query("SELECT pa FROM PagoAgrupadoModel pa WHERE pa.prestamoId = :prestamoId ORDER BY pa.anio DESC, pa.semana DESC")
    ArrayList<PagoAgrupadoModel> getHistorialDePagosToPGS(String prestamoId);

    @Query("SELECT SUM(pa.monto) FROM PagoModel pa WHERE pa.prestamoId = :prestamoId")
    Double getSaldoFromPrestamoByPrestamoId(String prestamoId);

    @Query("SELECT pa " +
            "FROM PrestamoModel pr " +
            "INNER JOIN PagoUtilModel pa " +
            "ON pr.prestamoId = pa.prestamoId " +
            "WHERE pa.agente = :agencia " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana - 1 " +
            "AND pa.cierraCon > 0")
    ArrayList<PagoUtilModel> getPagoUtilModelsByAgenciaAnioAndSemanaToCobranza(String agencia, int anio, int semana);

    @Query("SELECT pa " +
            "FROM PrestamoModel pr " +
            "INNER JOIN PagoUtilModel pa " +
            "ON pr.prestamoId = pa.prestamoId " +
            "WHERE pa.agente = :agencia " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana " +
            "AND pa.esPrimerPago = false")
    ArrayList<PagoUtilModel> getPagoUtilModelsByAgenciaAnioAndSemanaToDashboard(String agencia, int anio, int semana);

    @Query("SELECT pa " +
            "FROM PrestamoModel pr " +
            "INNER JOIN PagoModel pa " +
            "ON pr.prestamoId = pa.prestamoId " +
            "WHERE pa.agente = :agencia " +
            "AND pa.fechaPago like :fechaPago% " +
            "AND pa.esPrimerPago = false")
    ArrayList<PagoModel> getPagoModelsByAgenciaAndFechaPagoToDashboard(String agencia, String fechaPago);

    @Query("SELECT pa " +
            "FROM PagoModel pa " +
            "INNER JOIN LiquidacionModel li " +
            "ON li.pagoId = pa.pagoId " +
            "AND pa.agente = :agencia " +
            "AND li.anio = :anio " +
            "AND li.semana = :semana")
    ArrayList<PagoModel> getPagoModelsOfLiquidacionesByAgenciaAnioAndSemanaToDashboard(String agencia, int anio, int semana);

    @Query("SELECT pa " +
            "FROM PagoModel pa " +
            "INNER JOIN LiquidacionModel li " +
            "ON li.pagoId = pa.pagoId " +
            "AND pa.agente = :agencia " +
            "AND pa.fechaPago like :fechaPago%")
    ArrayList<PagoModel> getPagosOfLiquidacionesByAgenciaAndFechaPagoToDashboard(String agencia, String fechaPago);

    @Query("SELECT pa " +
            "FROM PagoModel pa " +
            "WHERE pa.prestamoId = :prestamoId " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana " +
            "AND pa.creadoDesde = 'Migracion'")
    PagoModel getPagoMigracionByAgenteAnioAndSemana(String prestamoId, int anio, int semana);
}

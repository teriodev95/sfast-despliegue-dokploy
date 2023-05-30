package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.PagoModel;
import tech.calaverita.reporterloanssql.models.PagoVistaModel;

import java.util.ArrayList;

@Repository
public interface PagoRepository extends CrudRepository<PagoModel, String> {
    @Query("SELECT pa FROM PagoModel pa WHERE pa.prestamoId = :prestamoId AND pa.anio = :anio AND pa.semana = :semana GROUP BY pa.prestamoId, pa.anio, pa.semana")
    PagoModel getPagoModelByPrestamoIdAnioAndSemana(String prestamoId, int anio, int semana);

    @Query("SELECT pa FROM PagoModel pa WHERE pa.agente = :agencia AND pa.anio = :anio AND pa.semana = :semana AND pa.esPrimerPago = false")
    ArrayList<PagoModel> getPagoModelsByAgenciaAnioAndSemana(String agencia, int anio, int semana);

    @Query("SELECT pa FROM PagoModel pa WHERE pa.prestamoId = :prestamoId ORDER BY pa.anio ASC, pa.semana ASC")
    ArrayList<PagoModel> getHistorialDePagos(String prestamoId);

    @Query("SELECT SUM(pa.monto) FROM PagoModel pa WHERE pa.prestamoId = :prestamoId")
    Double getSaldoFromPrestamoByPrestamoId(String prestamoId);

    @Query("SELECT pa " +
            "FROM PrestamoModel pr " +
            "INNER JOIN PagoVistaModel pa " +
            "ON pr.prestamoId = pa.prestamoId " +
            "WHERE pa.agente = :agencia " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana - 1 " +
            "AND pa.cierraCon > 0")
    ArrayList<PagoVistaModel> getPagosToCobranza(String agencia, int anio, int semana);

    @Query("SELECT pa " +
            "FROM PrestamoModel pr " +
            "INNER JOIN PagoVistaModel pa " +
            "ON pr.prestamoId = pa.prestamoId " +
            "WHERE pa.agente = :agencia " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana " +
            "AND pa.esPrimerPago = false")
    ArrayList<PagoVistaModel> getPagosToDashboard(String agencia, int anio, int semana);

    @Query("SELECT pa " +
            "FROM PagoModel pa " +
            "INNER JOIN LiquidacionModel li " +
            "ON li.pagoId = pa.pagoId " +
            "WHERE pa.agente = :agencia " +
            "AND li.anio = :anio " +
            "AND li.semana = :semana")
    ArrayList<PagoModel> getPagosOfLiquidacionesToDashboard(String agencia, int anio, int semana);

    @Query("SELECT pa " +
            "FROM PagoModel pa " +
            "WHERE pa.prestamoId = :prestamoId " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana " +
            "AND pa.creadoDesde = 'Migracion'")
    PagoModel getPagoMigracionByAgenteAnioAndSemana(String prestamoId, int anio, int semana);
}

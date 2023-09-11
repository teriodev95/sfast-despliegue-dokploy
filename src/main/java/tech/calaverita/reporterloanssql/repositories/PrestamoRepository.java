package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.PrestamoModel;
import tech.calaverita.reporterloanssql.models.PrestamoUtilModel;

import java.util.ArrayList;

@Repository
public interface PrestamoRepository extends CrudRepository<PrestamoModel, String> {
    @Query("SELECT pr " +
            "FROM PrestamoModel pr " +
            "WHERE pr.prestamoId = :prestamoId")
    PrestamoModel getPrestamoModelByPrestamoId(String prestamoId);

    @Query("SELECT pr " +
            "FROM PrestamoModel pr " +
            "INNER JOIN PagoAgrupadoModel pa " +
            "ON pr.prestamoId = pa.prestamoId " +
            "WHERE pa.agente = :agencia " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana - 1 " +
            "AND pa.cierraCon > 0")
    ArrayList<PrestamoModel> getPrestamoModelsByAgenciaAnioAndSemanaToCobranzaPGS(String agencia, int anio, int semana);

    @Query("SELECT pr " +
            "FROM PrestamoUtilModel pr " +
            "INNER JOIN PagoAgrupadoModel pa " +
            "ON pr.prestamoId = pa.prestamoId " +
            "WHERE pa.agente = :agencia " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana - 1 " +
            "AND pa.cierraCon > 0")
    ArrayList<PrestamoUtilModel> getPrestamoUtilModelByAgenciaAnioAndSemanaToCobranza(String agencia, int anio, int semana);

    @Query("SELECT pr " +
            "FROM PrestamoUtilModel pr " +
            "INNER JOIN PagoAgrupadoModel pa " +
            "ON pr.prestamoId = pa.prestamoId " +
            "WHERE pa.agente = :agencia " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana " +
            "AND pa.esPrimerPago = false")
    ArrayList<PrestamoUtilModel> getPrestamoUtilModelByAgenciaAnioAndSemanaToDashboard(String agencia, int anio, int semana);

    @Query("SELECT distinct(pr)" +
            "FROM PrestamoUtilModel pr " +
            "INNER JOIN PagoModel pa " +
            "ON pr.prestamoId = pa.prestamoId " +
            "WHERE pa.agente = :agencia " +
            "AND pa.fechaPago like :fechaPago% " +
            "AND pa.esPrimerPago = false")
    ArrayList<PrestamoUtilModel> getPrestamoUtilModelByAgenciaAndFechaPagoToDashboard(String agencia, String fechaPago);
}

package tech.calaverita.sfast_xpress.repositories.views;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoUtilModel;

import java.util.ArrayList;

@Repository
public interface PrestamoUtilRepository extends CrudRepository<PrestamoUtilModel, String> {
    @Query("SELECT pr " +
            "FROM PrestamoUtilModel pr " +
            "INNER JOIN PagoAgrupadoModel pa " +
            "ON pr.prestamoId = pa.prestamoId " +
            "WHERE pa.agente = :agencia " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana - 1 " +
            "AND pa.cierraCon > 0")
    ArrayList<PrestamoUtilModel> darrprestUtilEntFindByAgenciaAnioAndSemanaToCobranza(
            String agencia,
            int anio,
            int semana
    );

    @Query("SELECT pr " +
            "FROM PrestamoUtilModel pr " +
            "INNER JOIN PagoAgrupadoModel pa " +
            "ON pr.prestamoId = pa.prestamoId " +
            "WHERE pa.agente = :agencia " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana " +
            "AND pa.esPrimerPago = false")
    ArrayList<PrestamoUtilModel> darrprestUtilEntFindByAgenciaAnioAndSemanaToDashboard(
            String agencia,
            int anio,
            int semana);

    @Query("SELECT distinct(pr)" +
            "FROM PrestamoUtilModel pr " +
            "INNER JOIN PagoModel pa " +
            "ON pr.prestamoId = pa.prestamoId " +
            "WHERE pa.agente = :agencia " +
            "AND pa.fechaPago like :fechaPago% " +
            "AND pa.esPrimerPago = false")
    ArrayList<PrestamoUtilModel> darrprestUtilEntByAgenciaAndFechaPagoToDashboard(
            String agencia,
            String fechaPago);
}

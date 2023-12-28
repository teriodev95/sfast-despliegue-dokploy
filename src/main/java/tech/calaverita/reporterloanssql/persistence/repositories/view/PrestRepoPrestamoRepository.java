package tech.calaverita.reporterloanssql.persistence.repositories.view;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.persistence.entities.view.PrestamoEntity;
import tech.calaverita.reporterloanssql.persistence.entities.view.PrestamoUtilEntity;

import java.util.ArrayList;

@Repository
public interface PrestRepoPrestamoRepository extends CrudRepository<PrestamoEntity, String> {
    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @Query("SELECT pr " +
            "FROM PrestamoEntity pr " +
            "WHERE pr.prestamoId = :prestamoId")
    PrestamoEntity prestEntFindByPrestamoId(
            String prestamoId
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT pr " +
            "FROM PrestamoEntity pr " +
            "INNER JOIN PagoAgrupadoEntity pa " +
            "ON pr.prestamoId = pa.prestamoId " +
            "WHERE pa.agente = :agencia " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana " +
            "AND pa.cierraCon > 0")
    ArrayList<PrestamoEntity> darrprestEntFindByAgenciaAnioAndSemanaToCobranzaPGS(
            String agencia,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT pr " +
            "FROM PrestamoUtilEntity pr " +
            "INNER JOIN PagoAgrupadoEntity pa " +
            "ON pr.prestamoId = pa.prestamoId " +
            "WHERE pa.agente = :agencia " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana - 1 " +
            "AND pa.cierraCon > 0")
    ArrayList<PrestamoUtilEntity> darrprestUtilEntFindByAgenciaAnioAndSemanaToCobranza(
            String agencia,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT pr " +
            "FROM PrestamoUtilEntity pr " +
            "INNER JOIN PagoAgrupadoEntity pa " +
            "ON pr.prestamoId = pa.prestamoId " +
            "WHERE pa.agente = :agencia " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana " +
            "AND pa.esPrimerPago = false")
    ArrayList<PrestamoUtilEntity> darrprestUtilEntFindByAgenciaAnioAndSemanaToDashboard(
            String agencia,
            int anio,
            int semana);

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT distinct(pr)" +
            "FROM PrestamoUtilEntity pr " +
            "INNER JOIN PagoEntity pa " +
            "ON pr.prestamoId = pa.prestamoId " +
            "WHERE pa.agente = :agencia " +
            "AND pa.fechaPago like :fechaPago% " +
            "AND pa.esPrimerPago = false")
    ArrayList<PrestamoUtilEntity> darrprestUtilEntByAgenciaAndFechaPagoToDashboard(
            String agencia,
            String fechaPago);
}

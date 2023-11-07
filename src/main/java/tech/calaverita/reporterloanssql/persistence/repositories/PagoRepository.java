package tech.calaverita.reporterloanssql.persistence.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.persistence.entities.view.PagoAgrupadoEntity;
import tech.calaverita.reporterloanssql.persistence.entities.PagoEntity;
import tech.calaverita.reporterloanssql.persistence.entities.view.PagoUtilEntity;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface PagoRepository extends CrudRepository<PagoEntity, String> {
    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @Query("SELECT pa " +
            "FROM PagoEntity pa " +
            "WHERE pa.pagoId = :id")
    Optional<PagoEntity> optpagEntFindById(
            String id
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT pa " +
            "FROM PagoEntity pa " +
            "WHERE pa.prestamoId = :prestamoId " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana " +
            "ORDER BY pa.fechaPago desc")
    ArrayList<PagoEntity> darrpagEntFindByPrestamoIdAnioAndSemana(
            String prestamoId,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT pa " +
            "FROM PagoEntity pa " +
            "WHERE pa.agente = :agencia " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana " +
            "AND pa.esPrimerPago = false")
    ArrayList<PagoEntity> darrpagEntFindByAgenciaAnioAndSemana(
            String agencia,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT pa " +
            "FROM PagoEntity pa " +
            "WHERE pa.prestamoId = :prestamoId " +
            "AND pa.esPrimerPago = false")
    ArrayList<PagoEntity> darrpagEntFindByPrestamoId(
            String prestamoId
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT pa " +
            "FROM PagoEntity pa " +
            "WHERE pa.prestamoId = :prestamoId " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana " +
            "AND pa.esPrimerPago = false")
    ArrayList<PagoEntity> darrpagEntFindByPrestamoIdAnioSemanaAndNoPrimerPago(
            String prestamoId,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT pa " +
            "FROM PagoAgrupadoEntity pa " +
            "WHERE pa.agente = :agencia " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana " +
            "AND pa.esPrimerPago = false")
    ArrayList<PagoAgrupadoEntity> darrpagAgrEntFindByAgenciaAnioAndSemana(
            String agencia,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT pa " +
            "FROM PagoAgrupadoEntity pa " +
            "WHERE pa.prestamoId = :prestamoId " +
            "AND pa.esPrimerPago = false " +
            "ORDER BY pa.anio DESC, pa.semana DESC")
    ArrayList<PagoAgrupadoEntity> darrpagAgrEntFindByPrestamoId(
            String prestamoId
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT pa " +
            "FROM PagoAgrupadoEntity pa " +
            "WHERE pa.prestamoId = :prestamoId " +
            "ORDER BY pa.anio ASC, pa.semana ASC")
    ArrayList<PagoAgrupadoEntity> darrpagAgrEntGetHistorialDePagosToApp(
            String prestamoId
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT pa " +
            "FROM PagoAgrupadoEntity pa " +
            "WHERE pa.prestamoId = :prestamoId " +
            "ORDER BY pa.anio DESC, pa.semana DESC")
    ArrayList<PagoAgrupadoEntity> darrpagAgrEntGetHistorialDePagosToPGS(
            String prestamoId
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT SUM(pa.monto) " +
            "FROM PagoEntity pa " +
            "WHERE pa.prestamoId = :prestamoId")
    double numGetSaldoFromPrestamoByPrestamoId(
            String prestamoId
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT pa " +
            "FROM PrestamoEntity pr " +
            "INNER JOIN PagoUtilEntity pa " +
            "ON pr.prestamoId = pa.prestamoId " +
            "WHERE pa.agente = :agencia " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana - 1 " +
            "AND pa.cierraCon > 0")
    ArrayList<PagoUtilEntity> darrpagUtilEntFindByAgenciaAnioAndSemanaToCobranza(
            String agencia,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT pa " +
            "FROM PrestamoEntity pr " +
            "INNER JOIN PagoUtilEntity pa " +
            "ON pr.prestamoId = pa.prestamoId " +
            "WHERE pa.agente = :agencia " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana " +
            "AND pa.esPrimerPago = false")
    ArrayList<PagoUtilEntity> darrpagUtilEntFindByAgenciaAnioAndSemanaToDashboard(
            String agencia,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT pa " +
            "FROM PagoEntity pa " +
            "INNER JOIN LiquidacionEntity li " +
            "ON li.pagoId = pa.pagoId " +
            "AND pa.agente = :agencia " +
            "AND pa.fechaPago like :fechaPago%")
    ArrayList<PagoEntity> darrpagEntFindByAgenciaAndFechaPagoToDashboard(
            String agencia,
            String fechaPago
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT pa " +
            "FROM PrestamoEntity pr " +
            "INNER JOIN PagoEntity pa " +
            "ON pr.prestamoId = pa.prestamoId " +
            "WHERE pa.agente = :agencia " +
            "AND pa.fechaPago like :fechaPago% " +
            "AND pa.esPrimerPago = false")
    ArrayList<PagoEntity> darrpagEntFindByAgenciaFechaPagoAndNoPrimerPagoToDashboard(
            String agencia,
            String fechaPago
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT pa " +
            "FROM PagoEntity pa " +
            "INNER JOIN LiquidacionEntity li " +
            "ON li.pagoId = pa.pagoId " +
            "AND pa.agente = :agencia " +
            "AND li.anio = :anio " +
            "AND li.semana = :semana")
    ArrayList<PagoEntity> darrpagEntFindByAgenciaAnioAndSemanaToDashboard(
            String agencia,
            int anio,
            int semana);

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT pa " +
            "FROM PagoEntity pa " +
            "WHERE pa.prestamoId = :prestamoId " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana " +
            "AND pa.creadoDesde = 'Migracion'")
    PagoEntity pagEntFindByPrestamoIdAnioAndSemana(
            String prestamoId,
            int anio,
            int semana);
}

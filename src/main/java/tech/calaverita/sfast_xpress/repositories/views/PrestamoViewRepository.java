package tech.calaverita.sfast_xpress.repositories.views;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoViewModel;

@Repository
public interface PrestamoViewRepository extends CrudRepository<PrestamoViewModel, String> {
        ArrayList<PrestamoViewModel> findByAgenciaAndSaldoAlIniciarSemanaGreaterThan(String agencia,
                        Double saldoAlIniciarSemana);

        @Query("SELECT prest FROM PrestamoViewModel prest INNER JOIN PagoModel pag ON prest.prestamoId = pag.prestamoId "
                        +
                        "AND pag.agente = :agencia AND pag.anio = :anio AND pag.semana = :semana " +
                        "AND prest.saldoAlIniciarSemana <= prest.tarifa")
        ArrayList<PrestamoViewModel> findPorFinalizarByAgenciaAndAnioAndSemana(String agencia, int anio, int semana);

        @Query("SELECT prest FROM PrestamoViewModel prest INNER JOIN PagoModel pag ON prest.prestamoId = pag.prestamoId "
                        +
                        "AND prest.sucursal = :sucursal AND prest.gerencia = :gerencia AND pag.anio = :anio " +
                        "AND pag.semana = :semana AND prest.saldoAlIniciarSemana <= prest.tarifa")
        ArrayList<PrestamoViewModel> findPorFinalizarByGerenciaAndAnioAndSemana(String sucursal, String gerencia,
                        int anio,
                        int semana);

        @Query("SELECT pr " +
                        "FROM PrestamoViewModel pr " +
                        "INNER JOIN PagoModel pa " +
                        "ON pr.prestamoId = pa.prestamoId " +
                        "WHERE pa.agente = :agencia " +
                        "AND pa.anio = :anio " +
                        "AND pa.semana = :semana - 1 " +
                        "AND pa.cierraCon > 0")
        ArrayList<PrestamoViewModel> darrprestUtilEntFindByAgenciaAnioAndSemanaToCobranza(
                        String agencia,
                        int anio,
                        int semana);

        @Query("SELECT pr " +
                        "FROM PrestamoViewModel pr " +
                        "INNER JOIN PagoModel pa " +
                        "ON pr.prestamoId = pa.prestamoId " +
                        "WHERE pa.agente = :agencia " +
                        "AND pa.anio = :anio " +
                        "AND pa.semana = :semana " +
                        "AND pa.esPrimerPago = false")
        ArrayList<PrestamoViewModel> darrprestUtilEntFindByAgenciaAnioAndSemanaToDashboard(
                        String agencia,
                        int anio,
                        int semana);

        @Query("SELECT distinct(pr)" +
                        "FROM PrestamoViewModel pr " +
                        "INNER JOIN PagoModel pa " +
                        "ON pr.prestamoId = pa.prestamoId " +
                        "WHERE pa.agente = :agencia " +
                        "AND pa.fechaPago like :fechaPago% " +
                        "AND pa.esPrimerPago = false")
        ArrayList<PrestamoViewModel> darrprestUtilEntByAgenciaAndFechaPagoToDashboard(
                        String agencia,
                        String fechaPago);

        @Query(value = "SELECT prest.*, " +
                        "IF(trim(nombres) like :inicioNombres% or trim(nombres) like %:finalNombres%, 1, 0) + " +
                        "IF(trim(apellido_paterno) like :inicioApellidoPaterno% or trim(apellido_paterno) like %:finalApellidoPaterno, 1, 0) + "
                        +
                        "IF(trim(apellido_materno) like :inicioApellidoMaterno% or trim(apellido_materno) like %:finalApellidoMaterno, 1, 0) "
                        +
                        "coincidencias FROM prestamos_view prest " +
                        "WHERE (TRIM(prest.nombres) like :inicioNombres% or TRIM(prest.nombres) like %:finalNombres%) "
                        +
                        "OR (TRIM(prest.apellido_paterno) like :inicioApellidoPaterno% or TRIM(prest.apellido_paterno) like %:finalApellidoPaterno) "
                        +
                        "OR (TRIM(prest.apellido_materno) like :inicioApellidoMaterno% or TRIM(prest.apellido_materno) like %:finalApellidoMaterno) "
                        +
                        "HAVING coincidencias >= 3 order by coincidencias desc", nativeQuery = true)
        ArrayList<PrestamoViewModel> findByNombresOrApellidoPaternoOrApellidoMaterno(String inicioNombres,
                        String finalNombres,
                        String inicioApellidoPaterno, String finalApellidoPaterno, String inicioApellidoMaterno,
                        String finalApellidoMaterno);
}

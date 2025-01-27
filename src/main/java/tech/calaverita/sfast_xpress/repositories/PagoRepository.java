package tech.calaverita.sfast_xpress.repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tech.calaverita.sfast_xpress.models.mariaDB.PagoModel;
import tech.calaverita.sfast_xpress.models.mariaDB.dynamic.PagoDynamicModel;

@Repository
public interface PagoRepository extends CrudRepository<PagoModel, String> {
        PagoModel findByPrestamoIdAndAnioAndSemanaAndCreadoDesde(String prestamoId, int anio, int semana,
                        String creadoDesde);

        ArrayList<PagoModel> findByPrestamoIdAndAnioAndSemanaAndTipoNotInOrderByFechaPagoDesc(String prestamoId,
                        int anio,
                        int semana, String[] tipos);

        ArrayList<PagoModel> findByAgenteAndAnioAndSemanaAndEsPrimerPago(String agencia, int anio, int semana,
                        boolean esPrimerPago);

        ArrayList<PagoModel> findByPrestamoIdAndAnioAndSemanaAndEsPrimerPago(String prestamoId, int anio, int semana,
                        boolean esPrimerPago);

        @Query("SELECT SUM(pa.monto) FROM PagoModel pa WHERE pa.prestamoId = :prestamoId")
        double findCobradoByPrestamoId(String prestamoId);

        @Query("SELECT pag FROM PagoModel pag INNER JOIN LiquidacionModel liq ON liq.pagoId = pag.pagoId " +
                        "AND pag.agente = :agencia AND pag.fechaPago like :fechaPago%")
        ArrayList<PagoModel> findByAgenteAndFechaPagoInnerJoinLiquidacionModel(String agencia, String fechaPago);

        @Query("SELECT pag FROM PagoDynamicModel pag WHERE pag.agencia = :agencia AND pag.fechaPago <= :fechaPago "
                        + " AND pag.esPrimerPago = :esPrimerPago AND pag.anio = :anio AND pag.semana = :semana")
        ArrayList<PagoDynamicModel> findByAgenteAndFechaPagoAndEsPrimerPagoAndAnioAndSemanaInnerJoinPagoModel(
                        String agencia, String fechaPago,
                        boolean esPrimerPago, int anio, int semana);

        @Query("SELECT pag FROM PagoModel pag INNER JOIN PrestamoViewModel prest ON prest.prestamoId = pag.prestamoId "
                        + "WHERE pag.agente = :agencia AND pag.fechaPago <= :fechaPago AND pag.esPrimerPago = :esPrimerPago "
                        + "AND pag.anio = :anio AND pag.semana = :semana")
        ArrayList<PagoModel> findByAgenteAndFechaPagoLessThanEqualAndEsPrimerPagoInnerJoinPagoModel(String agencia,
                        String fechaPago, boolean esPrimerPago, int anio, int semana);

        @Query("SELECT pag FROM PagoDynamicModel pag INNER JOIN PrestamoViewModel prest ON prest.prestamoId = pag.prestamoId "
                        + "WHERE prest.gerencia = :gerencia AND prest.sucursal = :sucursal AND pag.fechaPago <= :fechaPago AND pag.esPrimerPago = :esPrimerPago "
                        + "AND pag.anio = :anio AND pag.semana = :semana")
        ArrayList<PagoDynamicModel> findByGerenciaAndSucursalAndFechaPagoLessThanEqualAndEsPrimerPagoInnerJoinPagoModel(
                        String gerencia, String sucursal,
                        String fechaPago, boolean esPrimerPago, int anio, int semana);

        @Query("SELECT pag FROM PagoModel pag INNER JOIN LiquidacionModel liq ON liq.pagoId = pag.pagoId " +
                        "AND pag.agente = :agencia AND liq.anio = :anio AND liq.semana = :semana")
        ArrayList<PagoModel> findByAgenteAndAnioAndSemanaInnerJoinLiquidacionModel(String agencia, int anio,
                        int semana);

        @Query("SELECT pag FROM PagoModel pag INNER JOIN AgenciaModel agenc ON agenc.id = pag.agente AND pag.tipo = :tipo "
                        +
                        "AND pag.anio = :anio AND pag.semana = :semana AND agenc.gerenciaId in :gerencias GROUP BY pag.prestamoId")
        ArrayList<PagoModel> findByGerenciasAndAnioAndSemanaAndTipo(ArrayList<String> gerencias, int anio, int semana,
                        String tipo);

        @Query(value = "SELECT pag.* FROM pagos_v3 pag WHERE pag.prestamoId = :prestamoId and !(pag.anio = :anio and pag.semana = :semana) order by anio asc, semana asc", nativeQuery = true)
        ArrayList<PagoModel> findByPrestamoIdAndAnioNotAndSemanaNotOrderByAnioAscSemanaAsc(String prestamoId, int anio,
                        int semana);

        @Query("SELECT pag FROM PagoModel pag INNER JOIN PrestamoViewModel prest ON prest.prestamoId = pag.prestamoId "
                        + "WHERE prest.gerencia = :gerencia AND prest.sucursal = :sucursal AND pag.anio = :anio AND pag.semana = :semana "
                        + "AND pag.esPrimerPago = :esPrimerPago")
        ArrayList<PagoModel> findByGerenciaAndSucursalAndAnioAndSemanaAndESPrimerPagoInnerJoinPrestamoViewModel(
                        String gerencia, String sucursal,
                        int anio,
                        int semana, boolean esPrimerPago);
}

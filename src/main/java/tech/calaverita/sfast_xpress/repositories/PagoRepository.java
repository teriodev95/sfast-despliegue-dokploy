package tech.calaverita.sfast_xpress.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.sfast_xpress.models.mariaDB.PagoModel;

import java.util.ArrayList;

@Repository
public interface PagoRepository extends CrudRepository<PagoModel, String> {
    PagoModel findByPrestamoIdAndAnioAndSemanaAndCreadoDesde(String prestamoId, int anio, int semana,
                                                             String creadoDesde);

    ArrayList<PagoModel> findByPrestamoIdAndAnioAndSemanaOrderByFechaPagoDesc(String prestamoId, int anio, int semana);

    ArrayList<PagoModel> findByAgenteAndAnioAndSemanaAndEsPrimerPago(String agencia, int anio, int semana,
                                                                     boolean esPrimerPago);

    ArrayList<PagoModel> findByPrestamoIdAndAnioAndSemanaAndEsPrimerPago(String prestamoId, int anio, int semana,
                                                                         boolean esPrimerPago);

    @Query("SELECT SUM(pa.monto) FROM PagoModel pa WHERE pa.prestamoId = :prestamoId")
    double findCobradoByPrestamoId(String prestamoId);

    @Query("SELECT pag FROM PagoModel pag INNER JOIN LiquidacionModel liq ON liq.pagoId = pag.pagoId " +
            "AND pag.agente = :agencia AND pag.fechaPago like :fechaPago%")
    ArrayList<PagoModel> findByAgenteAndFechaPagoInnerJoinLiquidacionModel(String agencia, String fechaPago);

    @Query("SELECT pag FROM PagoModel pag INNER JOIN PrestamoModel prest ON prest.prestamoId = pag.prestamoId " +
            "AND pag.agente = :agencia AND pag.fechaPago like :fechaPago% AND pag.esPrimerPago = :esPrimerPago")
    ArrayList<PagoModel> findByAgenteAndFechaPagoAndEsPrimerPagoInnerJoinPagoModel(String agencia, String fechaPago,
                                                                                   boolean esPrimerPago);

    @Query("SELECT pag FROM PagoModel pag INNER JOIN LiquidacionModel liq ON liq.pagoId = pag.pagoId " +
            "AND pag.agente = :agencia AND liq.anio = :anio AND liq.semana = :semana")
    ArrayList<PagoModel> findByAgenteAndAnioAndSemanaInnerJoinLiquidacionModel(String agencia, int anio, int semana);

    @Query("SELECT pag FROM PagoModel pag INNER JOIN AgenciaModel agenc ON agenc.id = pag.agente AND pag.tipo = :tipo " +
            "AND pag.anio = :anio AND pag.semana = :semana AND agenc.gerenciaId in :gerencias GROUP BY pag.prestamoId")
    ArrayList<PagoModel> findByGerenciasAndAnioAndSemanaAndTipo(ArrayList<String> gerencias, int anio, int semana, String tipo);
}

package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.PrestamoModel;

import java.util.ArrayList;

@Repository
public interface PrestamoRepository extends CrudRepository<PrestamoModel, String> {
    @Query("SELECT pr " +
            "FROM PrestamoModel pr " +
            "WHERE pr.prestamoId = :prestamoId")
    PrestamoModel getPrestamoModelByPrestamoId(String prestamoId);

    @Query("SELECT pr " +
            "FROM PrestamoModel pr " +
            "INNER JOIN PagoModel pa " +
            "ON pr.prestamoId = pa.prestamoId " +
            "WHERE pa.agente = :agencia " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana - 1 " +
            "AND pa.cierraCon > 0 " +
            "AND pa.cierraCon <> pr.descuento ")
    ArrayList<PrestamoModel> getPrestamoModelsForCobranzaByAgencia(String agencia, int anio, int semana);

    @Query("SELECT pr " +
            "FROM PrestamoModel pr " +
            "INNER JOIN PagoModel pa " +
            "ON pr.prestamoId = pa.prestamoId " +
            "WHERE pr.gerencia = :gerencia " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana - 1 " +
            "AND pa.cierraCon > 0 " +
            "AND pa.cierraCon <> pr.descuento ")
    ArrayList<PrestamoModel> getPrestamoModelsForCobranzaByGerencia(String gerencia, int anio, int semana);
}

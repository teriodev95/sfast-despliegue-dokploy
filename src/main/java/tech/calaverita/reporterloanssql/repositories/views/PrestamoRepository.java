package tech.calaverita.reporterloanssql.repositories.views;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.mariaDB.views.PrestamoModel;

import java.util.ArrayList;

@Repository
public interface PrestamoRepository extends CrudRepository<PrestamoModel, String> {
    @Query("SELECT pr " +
            "FROM PrestamoModel pr " +
            "WHERE pr.prestamoId = :prestamoId")
    PrestamoModel prestEntFindByPrestamoId(
            String prestamoId
    );

    @Query("SELECT pr " +
            "FROM PrestamoModel pr " +
            "INNER JOIN PagoAgrupadoModel pa " +
            "ON pr.prestamoId = pa.prestamoId " +
            "WHERE pa.agente = :agencia " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana " +
            "AND pa.cierraCon > 0")
    ArrayList<PrestamoModel> darrprestEntFindByAgenciaAnioAndSemanaToCobranzaPGS(
            String agencia,
            int anio,
            int semana
    );
}

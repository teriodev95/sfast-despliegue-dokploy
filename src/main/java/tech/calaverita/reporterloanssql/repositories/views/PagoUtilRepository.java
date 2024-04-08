package tech.calaverita.reporterloanssql.repositories.views;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.mariaDB.views.PagoUtilModel;

import java.util.ArrayList;

@Repository
public interface PagoUtilRepository extends CrudRepository<PagoUtilModel, String> {
    ArrayList<PagoUtilModel> findByAgenteAndAnioAndSemanaAndCierraConGreaterThan(String agencia, int anio, int semana,
                                                                                 double cierraConGreaterThan);

    ArrayList<PagoUtilModel> findByAgenteAndAnioAndSemanaAndEsPrimerPago(String agencia, int anio, int semana,
                                                                         boolean esPrimerPago);
}

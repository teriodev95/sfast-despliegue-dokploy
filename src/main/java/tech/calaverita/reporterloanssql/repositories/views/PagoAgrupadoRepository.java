package tech.calaverita.reporterloanssql.repositories.views;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.mariaDB.views.PagoAgrupadoModel;

import java.util.ArrayList;

@Repository
public interface PagoAgrupadoRepository extends CrudRepository<PagoAgrupadoModel, String> {
    ArrayList<PagoAgrupadoModel> findByPrestamoIdAndEsPrimerPagoOrderByAnioDescSemanaDesc(String prestamoId,
                                                                                          boolean esPrimerPago);

    ArrayList<PagoAgrupadoModel> findByPrestamoIdOrderByAnioAscSemanaAsc(String prestamoId);
}

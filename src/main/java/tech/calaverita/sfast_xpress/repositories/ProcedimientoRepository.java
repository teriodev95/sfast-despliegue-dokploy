package tech.calaverita.sfast_xpress.repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tech.calaverita.sfast_xpress.models.mariaDB.ResumenYBalanceModel;

@Repository
public interface ProcedimientoRepository extends CrudRepository<ResumenYBalanceModel, String> {
    @Query(value = "CALL GetDetailedSummaryAndBalance(:usuarioId, :gerencia, :semana, :anio)", nativeQuery = true)
    ArrayList<ResumenYBalanceModel> findResumenYBalanceModel(Integer usuarioId, String gerencia, Integer semana, Integer anio);
}

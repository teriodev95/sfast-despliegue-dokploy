package tech.calaverita.sfast_xpress.repositories;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tech.calaverita.sfast_xpress.models.mariaDB.GastoModel;

@Repository
public interface GastoRepository extends CrudRepository<GastoModel, Integer> {
    ArrayList<GastoModel> findByCreadoPorId(Integer creadoPorId);

    ArrayList<GastoModel> findByCreadoPorIdIn(int[] creadoPorIds);

    ArrayList<GastoModel> findByCreadoPorIdAndAnioAndSemanaAndTipoGasto(Integer creadoPorId, int anio, int semana,
            String tipoGasto);
}

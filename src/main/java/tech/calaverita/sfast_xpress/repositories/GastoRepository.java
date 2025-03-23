package tech.calaverita.sfast_xpress.repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tech.calaverita.sfast_xpress.models.mariaDB.GastoModel;

@Repository
public interface GastoRepository extends CrudRepository<GastoModel, Integer> {
        ArrayList<GastoModel> findByCreadoPorId(Integer creadoPorId);

        ArrayList<GastoModel> findByCreadoPorIdIn(int[] creadoPorIds);

        ArrayList<GastoModel> findByCreadoPorIdAndAnioAndSemana(Integer creadoPorId, int anio, int semana);

        ArrayList<GastoModel> findByCreadoPorIdAndAnioAndSemanaAndTipoGasto(Integer creadoPorId, int anio, int semana,
                        String tipoGasto);

        @Query("SELECT gast FROM GastoModel gast INNER JOIN gast.usuarioModel usuar ON usuar.gerencia = :gerencia "
                        + "AND usuar.tipo = 'Gerente' WHERE gast.anio = :anio AND gast.semana = :semana")
        List<GastoModel> findByGerenciaAndAnioAndSemana(String gerencia, int anio, int semana);
}

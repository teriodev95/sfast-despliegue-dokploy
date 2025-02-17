package tech.calaverita.sfast_xpress.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tech.calaverita.sfast_xpress.models.mariaDB.ComisionModel;

@Repository
public interface ComisionRepository extends CrudRepository<ComisionModel, Integer> {
    ComisionModel findByAgenciaAndAnioAndSemana(String agencia, Integer anio, Integer semana);

    List<ComisionModel> findByGerenciaAndAnioAndSemana(String gerencia, Integer anio, Integer semana);
}

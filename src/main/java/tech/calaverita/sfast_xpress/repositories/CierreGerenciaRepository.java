package tech.calaverita.sfast_xpress.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tech.calaverita.sfast_xpress.models.mariaDB.CierreGerenciaModel;

@Repository
public interface CierreGerenciaRepository extends CrudRepository<CierreGerenciaModel, Integer> {
    boolean existsByGerenciaAndAnioAndSemana(String gerencia, int anio, int semana);

    CierreGerenciaModel findByGerenciaAndAnioAndSemana(String gerencia, int anio, int semana);
}

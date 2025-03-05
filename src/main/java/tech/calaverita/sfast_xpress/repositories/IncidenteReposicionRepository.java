package tech.calaverita.sfast_xpress.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tech.calaverita.sfast_xpress.models.mariaDB.IncidenteReposicionModel;

@Repository
public interface IncidenteReposicionRepository extends CrudRepository<IncidenteReposicionModel, Integer> {
    List<IncidenteReposicionModel> findByGerenciaAndAnioAndSemana(String gerencia, int anio, int semana);

    List<IncidenteReposicionModel> findByCategoriaAndGerenciaAndAnioAndSemana(String categoria, String gerencia,
            int anio, int semana);

    List<IncidenteReposicionModel> findByCategoriaNotAndGerenciaAndAnioAndSemana(String categoria, String gerencia,
            int anio, int semana);
}

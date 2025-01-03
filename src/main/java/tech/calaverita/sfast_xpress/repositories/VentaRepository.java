package tech.calaverita.sfast_xpress.repositories;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tech.calaverita.sfast_xpress.models.VentaModel;

@Repository
public interface VentaRepository extends CrudRepository<VentaModel, Integer> {
    ArrayList<VentaModel> findByGerenciaAndAnioAndSemana(String gerencia, int anio, int semana);

    ArrayList<VentaModel> findByAgenciaAndAnioAndSemana(String gerencia, int anio, int semana);
}
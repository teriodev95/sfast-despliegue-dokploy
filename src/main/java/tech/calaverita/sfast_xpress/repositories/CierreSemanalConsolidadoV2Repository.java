package tech.calaverita.sfast_xpress.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tech.calaverita.sfast_xpress.models.mariaDB.CierreSemanalConsolidadoV2Model;

@Repository
public interface CierreSemanalConsolidadoV2Repository extends CrudRepository<CierreSemanalConsolidadoV2Model, Integer> {
    CierreSemanalConsolidadoV2Model findByAgenciaAndAnioAndSemana(String agencia, Integer anio, Integer semana);

    List<CierreSemanalConsolidadoV2Model> findByGerenciaAndAnioAndSemana(String gerencia, Integer anio, Integer semana);
}

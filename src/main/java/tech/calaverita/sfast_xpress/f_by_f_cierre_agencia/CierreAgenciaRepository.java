package tech.calaverita.sfast_xpress.f_by_f_cierre_agencia;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CierreAgenciaRepository extends JpaRepository<CierreAgenciaModel, Integer> {
    CierreAgenciaModel findByAgenciaAndAnioAndSemana(String agencia, Integer anio, Integer semana);

    List<CierreAgenciaModel> findByGerenciaAndAnioAndSemana(String gerencia, Integer anio, Integer semana);

    boolean existsByAgenciaAndAnioAndSemana(String agencia, int anio, int semana);
}

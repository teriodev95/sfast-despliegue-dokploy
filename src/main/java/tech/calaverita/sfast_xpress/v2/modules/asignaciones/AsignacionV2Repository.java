package tech.calaverita.sfast_xpress.v2.modules.asignaciones;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("asignacionV2Repository")
public interface AsignacionV2Repository extends JpaRepository<AsignacionV2Model, String> {
    List<AsignacionV2Model> findByAgencia(String agencia);
    List<AsignacionV2Model> findByGerenciaEntrega(String gerenciaEntrega);
    List<AsignacionV2Model> findByGerenciaRecibe(String gerenciaRecibe);
    List<AsignacionV2Model> findByQuienEntrego(Integer quienEntrego);
    List<AsignacionV2Model> findByQuienRecibio(Integer quienRecibio);
    List<AsignacionV2Model> findByAnioAndSemana(Integer anio, Integer semana);
    List<AsignacionV2Model> findByAgenciaAndAnioAndSemana(String agencia, Integer anio, Integer semana);
} 
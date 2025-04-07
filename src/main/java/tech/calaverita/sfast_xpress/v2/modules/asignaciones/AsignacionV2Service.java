package tech.calaverita.sfast_xpress.v2.modules.asignaciones;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service("asignacionV2Service")
public class AsignacionV2Service {
    private final AsignacionV2Repository asignacionRepository;

    public AsignacionV2Service(AsignacionV2Repository asignacionRepository) {
        this.asignacionRepository = asignacionRepository;
    }

    public List<AsignacionV2Model> findAll() {
        return asignacionRepository.findAll();
    }

    public Optional<AsignacionV2Model> findById(String id) {
        return asignacionRepository.findById(id);
    }

    public List<AsignacionV2Model> findByAgencia(String agencia) {
        return asignacionRepository.findByAgencia(agencia);
    }

    public List<AsignacionV2Model> findByGerenciaEntrega(String gerenciaEntrega) {
        return asignacionRepository.findByGerenciaEntrega(gerenciaEntrega);
    }

    public List<AsignacionV2Model> findByGerenciaRecibe(String gerenciaRecibe) {
        return asignacionRepository.findByGerenciaRecibe(gerenciaRecibe);
    }

    public List<AsignacionV2Model> findByQuienEntrego(Integer quienEntrego) {
        return asignacionRepository.findByQuienEntrego(quienEntrego);
    }

    public List<AsignacionV2Model> findByQuienRecibio(Integer quienRecibio) {
        return asignacionRepository.findByQuienRecibio(quienRecibio);
    }

    public List<AsignacionV2Model> findByAnioAndSemana(Integer anio, Integer semana) {
        return asignacionRepository.findByAnioAndSemana(anio, semana);
    }

    public List<AsignacionV2Model> findByAgenciaAndAnioAndSemana(String agencia, Integer anio, Integer semana) {
        return asignacionRepository.findByAgenciaAndAnioAndSemana(agencia, anio, semana);
    }

    public AsignacionV2Model save(AsignacionV2Model asignacion) {
        if (asignacion.getId() == null) {
            asignacion.setId(UUID.randomUUID().toString());
        }
        return asignacionRepository.save(asignacion);
    }

    public void deleteById(String id) {
        asignacionRepository.deleteById(id);
    }
} 
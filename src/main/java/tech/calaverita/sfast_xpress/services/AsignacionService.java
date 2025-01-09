package tech.calaverita.sfast_xpress.services;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import tech.calaverita.sfast_xpress.models.mariaDB.AsignacionModel;
import tech.calaverita.sfast_xpress.repositories.AsignacionRepository;

@Service
public class AsignacionService {
    private final AsignacionRepository repo;

    public AsignacionService(AsignacionRepository repo) {
        this.repo = repo;
    }

    public boolean existById(String id) {
        return this.repo.existsById(id);
    }

    public AsignacionModel save(AsignacionModel asignacionModel) {
        return this.repo.save(asignacionModel);
    }

    public AsignacionModel findById(String id) {
        return this.repo.findById(id).orElse(null);
    }

    public ArrayList<AsignacionModel> findByAgenciaAnioAndSemana(String agencia, int anio, int semana) {
        return this.repo.findByAgenciaAndAnioAndSemana(agencia, anio, semana);
    }

    public ArrayList<AsignacionModel> findByQuienRecibioUsuarioIdAnioAndSemana(Integer quienRecibioUsuarioId, int anio,
            int semana) {
        return this.repo.findByQuienRecibioUsuarioIdAndAnioAndSemana(quienRecibioUsuarioId, anio, semana);
    }

    public ArrayList<AsignacionModel> findByQuienEntregoUsuarioIdAnioAndSemana(Integer quienEntregoUsuarioId, int anio,
            int semana) {
        return this.repo.findByQuienEntregoUsuarioIdAndAnioAndSemana(quienEntregoUsuarioId, anio, semana);
    }

    public ArrayList<AsignacionModel> findAll() {
        return (ArrayList<AsignacionModel>) this.repo.findAll();
    }

    public ArrayList<AsignacionModel> findByQuienRecibioUsuarioIdAnioSemanaAndTipoInnerJoinUsuarioModel(
            Integer quienRecibioUsuarioId, int anio,
            int semana, String tipo) {
        return this.repo.findByQuienRecibioUsuarioIdAndAnioAndSemanaAndTipoInnerJoinUsuarioModel(quienRecibioUsuarioId,
                anio,
                semana, tipo);
    }

    public ArrayList<AsignacionModel> findByQuienEntregoUsuarioIdAnioSemanaAndTipoInnerJoinUsuarioModel(
            Integer quienEntregoUsuarioId, int anio,
            int semana, String tipo) {
        return this.repo.findByQuienEntregoUsuarioIdAndAnioAndSemanaAndTipoInnerJoinUsuarioModel(quienEntregoUsuarioId,
                anio,
                semana, tipo);
    }

    @Async("asyncExecutor")
    public CompletableFuture<Double> findSumaAsigancionesByQuienEntregoUsuarioIdAnioAndSemana(Integer quienEntregoUsuarioId, int anio, int semana) {
        return CompletableFuture
                .completedFuture(this.repo.findSumaAsigancionesByQuienEntregoUsuarioIdAnioAndSemana(quienEntregoUsuarioId, anio, semana));
    }
}

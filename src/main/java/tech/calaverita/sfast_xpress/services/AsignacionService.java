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

    public ArrayList<AsignacionModel> findAll() {
        return (ArrayList<AsignacionModel>) this.repo.findAll();
    }

    @Async("asyncExecutor")
    public CompletableFuture<Double> findSumaAsigancionesByAgenciaAnioAndSemana(String agencia, int anio, int semana) {
        return CompletableFuture
                .completedFuture(this.repo.findSumaAsigancionesByAgenciaAnioAndSemana(agencia, anio, semana));
    }
}

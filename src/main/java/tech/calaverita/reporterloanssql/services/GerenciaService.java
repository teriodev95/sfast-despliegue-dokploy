package tech.calaverita.reporterloanssql.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.mariaDB.GerenciaModel;
import tech.calaverita.reporterloanssql.repositories.GerenciaRepository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class GerenciaService {
    private final GerenciaRepository repo;

    public GerenciaService(GerenciaRepository repo) {
        this.repo = repo;
    }

    public GerenciaModel findById(String id) {
        return this.repo.findById(id).orElse(null);
    }

    public ArrayList<GerenciaModel> findAll() {
        return (ArrayList<GerenciaModel>) this.repo.findAll();
    }

    public ArrayList<String> findIdsBySucursalId(int sucursalId) {
        return this.repo.findIdsBySucursalId(sucursalId);
    }

    @Async("asyncExecutor")
    public CompletableFuture<GerenciaModel> findByIdAsync(String id) {
        return CompletableFuture.completedFuture(this.repo.findById(id).orElse(null));
    }
}

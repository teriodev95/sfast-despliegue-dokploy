package tech.calaverita.reporterloanssql.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.mariaDB.AgenciaModel;
import tech.calaverita.reporterloanssql.repositories.AgenciaRepository;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

@Service
public class AgenciaService {
    private final AgenciaRepository repo;

    public AgenciaService(AgenciaRepository repo) {
        this.repo = repo;
    }

    public AgenciaModel findById(String agenciaId) {
        return this.repo.findById(agenciaId).orElseThrow();
    }

    public ArrayList<String> findIdsByGerenciaId(String gerenciaId) {
        return this.repo.findIdsByGerenciaId(gerenciaId);
    }

    public String findStatusById(String id) {
        return this.repo.findStatusById(id);
    }

    @Async("asyncExecutor")
    public CompletableFuture<ArrayList<AgenciaModel>> findByGerenciaIdAsync(String gerenciaId) {
        return CompletableFuture.completedFuture(this.repo.findByGerenciaId(gerenciaId));
    }

    @Async("asyncExecutor")
    public CompletableFuture<ArrayList<String>> findIdsByGerenciaIdAsync(String gerenciaId) {
        return CompletableFuture.completedFuture(this.repo.findIdsByGerenciaId(gerenciaId));
    }
}

package tech.calaverita.sfast_xpress.services;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import tech.calaverita.sfast_xpress.models.mariaDB.AgenciaModel;
import tech.calaverita.sfast_xpress.repositories.AgenciaRepository;

@Service
public class AgenciaService {
    private final AgenciaRepository repo;

    public AgenciaService(AgenciaRepository repo) {
        this.repo = repo;
    }

    public AgenciaModel findById(String agenciaId) {
        return this.repo.findById(agenciaId).orElse(null);
    }

    public ArrayList<String> findIdsByGerenciaId(String gerenciaId) {
        String status = "ACTIVA";
        return this.repo.findIdsByGerenciaIdAndStatus(gerenciaId, status);
    }

    public Boolean existsById(String agenciaId) {
        return this.repo.existsById(agenciaId);
    }

    @Async("asyncExecutor")
    public CompletableFuture<String> findStatusById(String id) {
        return CompletableFuture.completedFuture(this.repo.findStatusById(id));
    }

    @Async("asyncExecutor")
    public CompletableFuture<ArrayList<AgenciaModel>> findByGerenciaIdAsync(String gerenciaId) {
        return CompletableFuture.completedFuture(this.repo.findByGerenciaId(gerenciaId));
    }

    @Async("asyncExecutor")
    public CompletableFuture<ArrayList<AgenciaModel>> findByGerenciaIdAndStatusAsync(String gerenciaId, String status) {
        return CompletableFuture.completedFuture(this.repo.findByGerenciaIdAndStatusOrderById(gerenciaId, status));
    }

    @Async("asyncExecutor")
    public CompletableFuture<ArrayList<String>> findIdsByGerenciaIdAsync(String gerenciaId) {
        String status = "ACTIVA";
        return CompletableFuture.completedFuture(this.repo.findIdsByGerenciaIdAndStatus(gerenciaId, status));
    }
}

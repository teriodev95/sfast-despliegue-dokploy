package tech.calaverita.sfast_xpress.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tech.calaverita.sfast_xpress.models.mariaDB.AgenciaModel;
import tech.calaverita.sfast_xpress.repositories.AgenciaRepository;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

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

    public String findStatusById(String id) {
        return this.repo.findStatusById(id);
    }

    @Async("asyncExecutor")
    public CompletableFuture<ArrayList<AgenciaModel>> findByGerenciaIdAsync(String gerenciaId) {
        return CompletableFuture.completedFuture(this.repo.findByGerenciaId(gerenciaId));
    }

    @Async("asyncExecutor")
    public CompletableFuture<ArrayList<String>> findIdsByGerenciaIdAsync(String gerenciaId) {
        String status = "ACTIVA";
        return CompletableFuture.completedFuture(this.repo.findIdsByGerenciaIdAndStatus(gerenciaId, status));
    }
}

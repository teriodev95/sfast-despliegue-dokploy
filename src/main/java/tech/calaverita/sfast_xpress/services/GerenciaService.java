package tech.calaverita.sfast_xpress.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tech.calaverita.sfast_xpress.models.mariaDB.GerenciaModel;
import tech.calaverita.sfast_xpress.repositories.GerenciaRepository;

import java.util.ArrayList;
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

    public ArrayList<GerenciaModel> findByUsuario(String usuario) {
        return this.repo.findByUsuario(usuario);
    }

    public ArrayList<String> findIdsBySucursalId(int sucursalId) {
        return this.repo.findIdsBySucursalId(sucursalId);
    }

    @Async("asyncExecutor")
    public CompletableFuture<GerenciaModel> findByIdAsync(String id) {
        return CompletableFuture.completedFuture(this.repo.findById(id).orElse(null));
    }
}

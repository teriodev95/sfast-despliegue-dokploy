package tech.calaverita.reporterloanssql.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.mariaDB.SucursalModel;
import tech.calaverita.reporterloanssql.repositories.SucursalRepository;

import java.util.concurrent.CompletableFuture;

@Service
public class SucursalService {
    private final SucursalRepository repo;

    public SucursalService(SucursalRepository repo) {
        this.repo = repo;
    }

    public SucursalModel findBySucursalId(String sucursalId) {
        return this.repo.findById(sucursalId).orElse(null);
    }

    @Async("asyncExecutor")
    public CompletableFuture<SucursalModel> findBySucursalIdAsync(String sucursalId) {
        return CompletableFuture.completedFuture(this.repo.findById(sucursalId).orElse(null));
    }

    public String findNombreSucursalByGerenciaId(String gerenciaId) {
        SucursalModel sucursalModel = this.repo.findById(gerenciaId.substring(0, 4)).orElse(null);

        return sucursalModel != null ? sucursalModel.getNombre().split(" ")[0] : null;
    }
}

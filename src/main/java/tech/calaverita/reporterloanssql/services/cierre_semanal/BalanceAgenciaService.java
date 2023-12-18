package tech.calaverita.reporterloanssql.services.cierre_semanal;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.persistence.entities.cierre_semanal.BalanceAgenciaEntity;
import tech.calaverita.reporterloanssql.persistence.mappers.BalanceAgenciaMapper;
import tech.calaverita.reporterloanssql.persistence.repositories.cierre_semanal.BalanceAgenciaRepository;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class BalanceAgenciaService {
    private final BalanceAgenciaRepository repo;
    public final BalanceAgenciaMapper mapper;

    public BalanceAgenciaService(
            BalanceAgenciaRepository repo,
            BalanceAgenciaMapper mapper
    ) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public void save(
            BalanceAgenciaEntity balAgencEnt
    ) {
        this.repo.save(balAgencEnt);
    }

    @Async("asyncExecutor")
    public CompletableFuture<Optional<BalanceAgenciaEntity>> findById(
            String id
    ) {
        Optional<BalanceAgenciaEntity> entity = this.repo.findById(id);
        return CompletableFuture.completedFuture(entity);
    }
}

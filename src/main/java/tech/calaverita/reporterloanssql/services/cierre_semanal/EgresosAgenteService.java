package tech.calaverita.reporterloanssql.services.cierre_semanal;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.persistence.entities.cierre_semanal.EgresosAgenteEntity;
import tech.calaverita.reporterloanssql.persistence.mappers.EgresosAgenteMapper;
import tech.calaverita.reporterloanssql.persistence.repositories.cierre_semanal.EgresosAgenteRepository;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class EgresosAgenteService {
    private final EgresosAgenteRepository repo;
    public final EgresosAgenteMapper mapper;

    public EgresosAgenteService(
            EgresosAgenteRepository repo_S,
            EgresosAgenteMapper mapper_S
    ) {
        this.repo = repo_S;
        this.mapper = mapper_S;
    }

    public void save(
            EgresosAgenteEntity egrAgentEnt_I
    ) {
        this.repo.save(egrAgentEnt_I);
    }

    @Async("asyncExecutor")

    public CompletableFuture<Optional<EgresosAgenteEntity>> findById(
            String id
    ) {
        Optional<EgresosAgenteEntity> entity = this.repo.findById(id);
        return CompletableFuture.completedFuture(entity);
    }
}

package tech.calaverita.reporterloanssql.services.cierre_semanal;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.persistence.dto.cierre_semanal.IngresosAgenteDTO;
import tech.calaverita.reporterloanssql.persistence.entities.cierre_semanal.IngresosAgenteEntity;
import tech.calaverita.reporterloanssql.persistence.mappers.cierre_semanal.IngresosAgenteMapper;
import tech.calaverita.reporterloanssql.persistence.repositories.cierre_semanal.IngresosAgenteRepository;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class IngresosAgenteService {
    private final IngresosAgenteRepository repo;
    private final IngresosAgenteMapper mapper;

    public IngresosAgenteService(
            IngresosAgenteRepository repo_S,
            IngresosAgenteMapper mapper_S
    ) {
        this.repo = repo_S;
        this.mapper = mapper_S;
    }

    public void save(
            IngresosAgenteEntity ingrAgentEnt_I
    ) {
        this.repo.save(ingrAgentEnt_I);
    }

    @Async("asyncExecutor")

    public CompletableFuture<Optional<IngresosAgenteEntity>> findById(
            String id
    ) {
        Optional<IngresosAgenteEntity> entity = this.repo.findById(id);
        return CompletableFuture.completedFuture(entity);
    }

    public IngresosAgenteDTO getIngresosAgenteDTO(
            IngresosAgenteEntity entity
    ) {
        return this.mapper.mapOut(entity);
    }

    public IngresosAgenteEntity getIngresosAgenteEntity(
            IngresosAgenteDTO DTO
    ) {
        return this.mapper.mapIn(DTO);
    }
}

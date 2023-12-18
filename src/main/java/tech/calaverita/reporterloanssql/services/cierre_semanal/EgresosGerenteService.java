package tech.calaverita.reporterloanssql.services.cierre_semanal;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.persistence.dto.cierre_semanal.EgresosGerenteDTO;
import tech.calaverita.reporterloanssql.persistence.entities.cierre_semanal.EgresosGerenteEntity;
import tech.calaverita.reporterloanssql.persistence.mappers.cierre_semanal.EgresosGerenteMapper;
import tech.calaverita.reporterloanssql.persistence.repositories.cierre_semanal.EgresosGerenteRepository;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class EgresosGerenteService {
    private final EgresosGerenteRepository repo;
    private final EgresosGerenteMapper mapper;

    public EgresosGerenteService(
            EgresosGerenteRepository repo_S,
            EgresosGerenteMapper mapper_S
    ) {
        this.repo = repo_S;
        this.mapper = mapper_S;
    }

    public void save(
            EgresosGerenteEntity egrGerEnt_I
    ) {
        this.repo.save(egrGerEnt_I);
    }

    @Async("asyncExecutor")

    public CompletableFuture<Optional<EgresosGerenteEntity>> findById(
            String id
    ) {
        Optional<EgresosGerenteEntity> entity = this.repo.findById(id);
        return CompletableFuture.completedFuture(entity);
    }

    public EgresosGerenteDTO getEgresosGerenteDTO(
            EgresosGerenteEntity entity
    ) {
        return this.mapper.mapOut(entity);
    }

    public EgresosGerenteEntity getEgresosGerenteEntity(
            EgresosGerenteDTO DTO
    ) {
        return this.mapper.mapIn(DTO);
    }
}

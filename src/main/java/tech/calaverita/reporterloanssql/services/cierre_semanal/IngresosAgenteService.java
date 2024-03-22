package tech.calaverita.reporterloanssql.services.cierre_semanal;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.dto.cierre_semanal.IngresosAgenteDTO;
import tech.calaverita.reporterloanssql.mappers.cierre_semanal.IngresosAgenteMapper;
import tech.calaverita.reporterloanssql.models.mariaDB.cierre_semanal.IngresosAgenteModel;
import tech.calaverita.reporterloanssql.repositories.cierre_semanal.IngresosAgenteRepository;

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
            IngresosAgenteModel ingrAgentEnt_I
    ) {
        this.repo.save(ingrAgentEnt_I);
    }

    @Async("asyncExecutor")

    public CompletableFuture<Optional<IngresosAgenteModel>> findById(
            String id
    ) {
        Optional<IngresosAgenteModel> entity = this.repo.findById(id);
        return CompletableFuture.completedFuture(entity);
    }

    public IngresosAgenteDTO getIngresosAgenteDTO(
            IngresosAgenteModel entity
    ) {
        return this.mapper.mapOut(entity);
    }

    public IngresosAgenteModel getIngresosAgenteEntity(
            IngresosAgenteDTO DTO
    ) {
        return this.mapper.mapIn(DTO);
    }
}

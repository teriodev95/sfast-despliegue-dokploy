package tech.calaverita.reporterloanssql.services.cierre_semanal;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.dto.cierre_semanal.EgresosGerenteDTO;
import tech.calaverita.reporterloanssql.mappers.cierre_semanal.EgresosGerenteMapper;
import tech.calaverita.reporterloanssql.models.mariaDB.cierre_semanal.EgresosGerenteModel;
import tech.calaverita.reporterloanssql.repositories.cierre_semanal.EgresosGerenteRepository;

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
            EgresosGerenteModel egrGerEnt_I
    ) {
        this.repo.save(egrGerEnt_I);
    }

    @Async("asyncExecutor")

    public CompletableFuture<Optional<EgresosGerenteModel>> findById(
            String id
    ) {
        Optional<EgresosGerenteModel> entity = this.repo.findById(id);
        return CompletableFuture.completedFuture(entity);
    }

    public EgresosGerenteDTO getEgresosGerenteDTO(
            EgresosGerenteModel entity
    ) {
        return this.mapper.mapOut(entity);
    }

    public EgresosGerenteModel getEgresosGerenteEntity(
            EgresosGerenteDTO DTO
    ) {
        return this.mapper.mapIn(DTO);
    }
}

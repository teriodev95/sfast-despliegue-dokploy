package tech.calaverita.sfast_xpress.services.cierre_semanal;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tech.calaverita.sfast_xpress.dto.cierre_semanal.EgresosGerenteDTO;
import tech.calaverita.sfast_xpress.mappers.cierre_semanal.EgresosGerenteMapper;
import tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal.EgresosGerenteModel;
import tech.calaverita.sfast_xpress.repositories.cierre_semanal.EgresosGerenteRepository;

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

    public CompletableFuture<EgresosGerenteModel> findById(String id) {
        return CompletableFuture.completedFuture(this.repo.findById(id).orElse(null));
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

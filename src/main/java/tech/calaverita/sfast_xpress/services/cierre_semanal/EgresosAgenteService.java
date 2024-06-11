package tech.calaverita.sfast_xpress.services.cierre_semanal;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tech.calaverita.sfast_xpress.dto.cierre_semanal.EgresosAgenteDTO;
import tech.calaverita.sfast_xpress.mappers.cierre_semanal.EgresosAgenteMapper;
import tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal.EgresosAgenteModel;
import tech.calaverita.sfast_xpress.repositories.cierre_semanal.EgresosAgenteRepository;

import java.util.concurrent.CompletableFuture;

@Service
public class EgresosAgenteService {
    private final EgresosAgenteRepository repo;
    private final EgresosAgenteMapper mapper;

    public EgresosAgenteService(
            EgresosAgenteRepository repo_S,
            EgresosAgenteMapper mapper_S
    ) {
        this.repo = repo_S;
        this.mapper = mapper_S;
    }

    public void save(
            EgresosAgenteModel egrAgentEnt_I
    ) {
        this.repo.save(egrAgentEnt_I);
    }

    @Async("asyncExecutor")

    public CompletableFuture<EgresosAgenteModel> findById(String id) {
        return CompletableFuture.completedFuture(this.repo.findById(id).orElse(null));
    }

    public EgresosAgenteDTO getEgresosGerenteDTO(
            EgresosAgenteModel entity
    ) {
        return this.mapper.mapOut(entity);
    }

    public EgresosAgenteModel getEgresosGerenteEntity(
            EgresosAgenteDTO DTO
    ) {
        return this.mapper.mapIn(DTO);
    }
}

package tech.calaverita.sfast_xpress.services.cierre_semanal;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tech.calaverita.sfast_xpress.dto.cierre_semanal.IngresosAgenteDTO;
import tech.calaverita.sfast_xpress.mappers.cierre_semanal.IngresosAgenteMapper;
import tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal.IngresosAgenteModel;
import tech.calaverita.sfast_xpress.repositories.cierre_semanal.IngresosAgenteRepository;

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

    public CompletableFuture<IngresosAgenteModel> findById(String id) {
        return CompletableFuture.completedFuture(this.repo.findById(id).orElse(null));
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

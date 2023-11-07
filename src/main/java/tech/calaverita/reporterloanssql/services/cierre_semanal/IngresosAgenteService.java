package tech.calaverita.reporterloanssql.services.cierre_semanal;

import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.persistence.entities.cierre_semanal.IngresosAgenteEntity;
import tech.calaverita.reporterloanssql.persistence.mappers.IngresosAgenteMapper;
import tech.calaverita.reporterloanssql.persistence.repositories.cierre_semanal.IngresosAgenteRepository;

@Service
public final class IngresosAgenteService {
    private final IngresosAgenteRepository repo;
    public final IngresosAgenteMapper mapper;

    private IngresosAgenteService(
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
}

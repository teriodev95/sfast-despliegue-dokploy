package tech.calaverita.reporterloanssql.services.cierre_semanal;

import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.persistence.entities.cierre_semanal.EgresosAgenteEntity;
import tech.calaverita.reporterloanssql.persistence.entities.cierre_semanal.EgresosGerenteEntity;
import tech.calaverita.reporterloanssql.persistence.mappers.EgresosAgenteMapper;
import tech.calaverita.reporterloanssql.persistence.mappers.EgresosGerenteMapper;
import tech.calaverita.reporterloanssql.persistence.repositories.cierre_semanal.EgresosAgenteRepository;
import tech.calaverita.reporterloanssql.persistence.repositories.cierre_semanal.EgresosGerenteRepository;

@Service
public final class EgresosGerenteService {
    private final EgresosGerenteRepository repo;
    public final EgresosGerenteMapper mapper;

    private EgresosGerenteService(
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
}

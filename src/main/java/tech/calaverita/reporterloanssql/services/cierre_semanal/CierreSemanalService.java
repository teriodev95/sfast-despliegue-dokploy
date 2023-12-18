package tech.calaverita.reporterloanssql.services.cierre_semanal;

import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.persistence.entities.cierre_semanal.CierreSemanalEntity;
import tech.calaverita.reporterloanssql.persistence.mappers.CierreSemanalMapper;
import tech.calaverita.reporterloanssql.persistence.repositories.cierre_semanal.CierreSemanalRepository;

import java.util.Optional;

@Service
public final class CierreSemanalService {
    private final CierreSemanalRepository repo;
    public final CierreSemanalMapper mapper;

    private CierreSemanalService(
            CierreSemanalRepository repo_S,
            CierreSemanalMapper mapper_S
    ) {
        this.repo = repo_S;
        this.mapper = mapper_S;
    }

    public void save(
            CierreSemanalEntity cierSemEnt_I
    ) {
        this.repo.save(cierSemEnt_I);
    }

    public Optional<CierreSemanalEntity> findById(
            String id_I
    ) {
        return this.repo.findById(id_I);
    }

    public Boolean existsById(
            String id
    ) {
        return this.repo.existsById(id);
    }
}

package tech.calaverita.reporterloanssql.services.cierre_semanal;

import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.persistence.entities.cierre_semanal.CierreSemanalEntity;
import tech.calaverita.reporterloanssql.persistence.mappers.CierreSemanalMapper;
import tech.calaverita.reporterloanssql.persistence.repositories.cierre_semanal.CierreSemanalRepository;

import java.util.Optional;

@Service
public class CierreSemanalService {
    private final CierreSemanalRepository repo;
    public final CierreSemanalMapper mapper;

    public CierreSemanalService(
            CierreSemanalRepository repo,
            CierreSemanalMapper mapper
    ) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public void save(
            CierreSemanalEntity cierSemEnt_I
    ) {
        this.repo.save(cierSemEnt_I);
    }

    public Optional<CierreSemanalEntity> findById(
            String id
    ) {
        return this.repo.findById(id);
    }
}

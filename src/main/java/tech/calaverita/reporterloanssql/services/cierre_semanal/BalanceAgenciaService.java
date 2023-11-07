package tech.calaverita.reporterloanssql.services.cierre_semanal;

import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.persistence.entities.cierre_semanal.BalanceAgenciaEntity;
import tech.calaverita.reporterloanssql.persistence.mappers.BalanceAgenciaMapper;
import tech.calaverita.reporterloanssql.persistence.repositories.cierre_semanal.BalanceAgenciaRepository;

@Service
public final class BalanceAgenciaService {
    private final BalanceAgenciaRepository repo;
    public final BalanceAgenciaMapper mapper;

    private BalanceAgenciaService(
            BalanceAgenciaRepository repo_S,
            BalanceAgenciaMapper mapper_S
    ) {
        this.repo = repo_S;
        this.mapper = mapper_S;
    }

    public void save(
            BalanceAgenciaEntity balAgencEnt_I
    ) {
        this.repo.save(balAgencEnt_I);
    }
}

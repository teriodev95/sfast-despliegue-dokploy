package tech.calaverita.reporterloanssql.services.cierre_semanal;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.dto.cierre_semanal.BalanceAgenciaDTO;
import tech.calaverita.reporterloanssql.mappers.cierre_semanal.BalanceAgenciaMapper;
import tech.calaverita.reporterloanssql.models.mariaDB.cierre_semanal.BalanceAgenciaModel;
import tech.calaverita.reporterloanssql.repositories.cierre_semanal.BalanceAgenciaRepository;

import java.util.concurrent.CompletableFuture;

@Service
public class BalanceAgenciaService {
    private final BalanceAgenciaRepository repo;
    private final BalanceAgenciaMapper mapper;

    public BalanceAgenciaService(
            BalanceAgenciaRepository repo,
            BalanceAgenciaMapper mapper
    ) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public void save(
            BalanceAgenciaModel balAgencEnt
    ) {
        this.repo.save(balAgencEnt);
    }

    @Async("asyncExecutor")
    public CompletableFuture<BalanceAgenciaModel> findById(String id) {
        return CompletableFuture.completedFuture(this.repo.findById(id).orElse(null));
    }

    public BalanceAgenciaDTO getBalanceAgenciaDTO(
            BalanceAgenciaModel entity
    ) {
        return this.mapper.mapOut(entity);
    }

    public BalanceAgenciaModel getBalanceAgenciaEntity(
            BalanceAgenciaDTO DTO
    ) {
        return this.mapper.mapIn(DTO);
    }
}

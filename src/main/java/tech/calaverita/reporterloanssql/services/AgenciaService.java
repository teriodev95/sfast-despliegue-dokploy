package tech.calaverita.reporterloanssql.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.mariaDB.AgenciaModel;
import tech.calaverita.reporterloanssql.repositories.AgenciaRepository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class AgenciaService {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private final AgenciaRepository repo;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    public AgenciaService(
            AgenciaRepository repo
    ) {
        this.repo = repo;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    public AgenciaModel agencModFindByAgenciaId(
            String agenciaId
    ) {
        return this.repo.agencModFindByAgenciaId(agenciaId);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<String> darrstrAgenciaIdFindByGerenciaId(
            String gerencia
    ) {
        return this.repo.darrstrAgenciaIdFindByGerenciaId(gerencia);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Async("asyncExecutor")
    public CompletableFuture<ArrayList<AgenciaModel>> findByGerenciaIdAsync(
            String gerenciaId
    ) {
        ArrayList<AgenciaModel> entities = this.repo.findByGerenciaId(gerenciaId);

        return CompletableFuture.completedFuture(entities);
    }

    public Optional<AgenciaModel> findById(
            String id
    ) {
        return this.repo.findById(id);
    }

    public String getStatusByAgenciaId(
            String agenciaId
    ) {
        Optional<AgenciaModel> entity = repo.findById(agenciaId);

        return entity.get().getStatus();
    }
}

package tech.calaverita.reporterloanssql.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.mariaDB.GerenciaModel;
import tech.calaverita.reporterloanssql.repositories.GerenciaRepository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class GerenciaService {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private final GerenciaRepository repo;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    public GerenciaService(
            GerenciaRepository repo
    ) {
        this.repo = repo;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    public ArrayList<String> darrstrGerenciaIdFindBySeguridad(
            String seguridad
    ) {
        return this.repo.darrstrGerenciaIdFindBySeguridad(seguridad);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<String> darrstrGerenciaIdByRegional(
            String regional
    ) {
        return this.repo.darrstrGerenciaIdByRegional(regional);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<GerenciaModel> darrGerModFindAll() {
        return this.repo.darrGerEntFindAll();
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Async("asyncExecutor")
    public CompletableFuture<ArrayList<GerenciaModel>> findAllAsync() {
        ArrayList<GerenciaModel> entity = this.repo.darrGerEntFindAll();

        return CompletableFuture.completedFuture(entity);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Async("asyncExecutor")
    public CompletableFuture<Optional<GerenciaModel>> findByIdAsync(
            String id
    ) {
        Optional<GerenciaModel> entity = this.repo.findById(id);

        return CompletableFuture.completedFuture(entity);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<String> darrstrGerenciaIdFindAll() {
        return this.repo.darrstrGerenciaIdFindAll();
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public GerenciaModel gerModFindByGerenciaId(
            String gerenciaId
    ) {
        return this.repo.gerEntFindByGerenciaId(gerenciaId);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<String> darrstrGerenciaIdFindBySucursalId(
            int sucursalId
    ) {
        return this.repo.darrstrGerenciaIdFindBySucursalId(sucursalId);
    }
}

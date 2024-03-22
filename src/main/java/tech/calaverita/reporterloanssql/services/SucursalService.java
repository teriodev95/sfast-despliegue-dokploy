package tech.calaverita.reporterloanssql.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.mariaDB.SucursalModel;
import tech.calaverita.reporterloanssql.repositories.SucursalRepository;

import java.util.concurrent.CompletableFuture;

@Service
public class SucursalService {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private final SucursalRepository repo;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    public SucursalService(
            SucursalRepository repo
    ) {
        this.repo = repo;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    public SucursalModel findBySucursalId(
            String sucursalId
    ) {
        return this.repo.findBySucursalId(sucursalId);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Async("asyncExecutor")
    public CompletableFuture<SucursalModel> findBySucursalIdAsync(
            String sucursalId
    ) {
        SucursalModel entity = this.repo.findBySucursalId(sucursalId);

        return CompletableFuture.completedFuture(entity);
    }

    public String getSucursalByGerenciaId(
            String gerenciaId
    ) {
        String sucursalId = gerenciaId.substring(0, 4);
        SucursalModel entity = this.repo.findBySucursalId(sucursalId);

        return entity.getNombre().split(" ")[0];
    }
}

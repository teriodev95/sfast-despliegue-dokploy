package tech.calaverita.reporterloanssql.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.mariaDB.CalendarioModel;
import tech.calaverita.reporterloanssql.repositories.CalendarioRepository;

import java.util.concurrent.CompletableFuture;

@Service
public class CalendarioService {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private final CalendarioRepository repo;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    public CalendarioService(
            CalendarioRepository repo
    ) {
        this.repo = repo;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    public CalendarioModel findByFechaActual(
            String fechaActual
    ) {
        return this.repo.findByFechaActual(fechaActual);
    }

    public Boolean existsByAnioAndSemana(
            int anio,
            int semana
    ) {
        return this.repo.existsByAnioAndSemana(anio, semana);
    }

    @Async("asyncExecutor")
    public CompletableFuture<CalendarioModel> findByFechaActualAsync(
            String fechaActual
    ) {
        CalendarioModel entity = this.repo.findByFechaActual(fechaActual);
        return CompletableFuture.completedFuture(entity);
    }

    @Async("asyncExecutor")
    public CompletableFuture<CalendarioModel> findByAnioAndSemanaAsync(
            int anio,
            int semana
    ) {
        CalendarioModel entity = this.repo.findByAnioAndSemana(anio, semana);
        return CompletableFuture.completedFuture(entity);
    }

    @Async("asyncExecutor")
    public CompletableFuture<Boolean> existsByAnioAndSemanaAsync(
            int anio,
            int semana
    ) {
        Boolean exists = this.repo.existsByAnioAndSemana(anio, semana);

        return CompletableFuture.completedFuture(exists);
    }
}

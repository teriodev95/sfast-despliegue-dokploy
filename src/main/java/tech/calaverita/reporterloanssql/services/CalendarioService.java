package tech.calaverita.reporterloanssql.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.mariaDB.CalendarioModel;
import tech.calaverita.reporterloanssql.repositories.CalendarioRepository;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

@Service
public class CalendarioService {
    private final CalendarioRepository repo;

    public CalendarioService(CalendarioRepository repo) {
        this.repo = repo;
    }

    public boolean existsByAnioAndSemana(int anio, int semana) {
        return this.repo.existsByAnioAndSemana(anio, semana);
    }

    public CalendarioModel findByFechaActual(String fechaActual) {
        return this.repo.findByFechaActual(fechaActual);
    }

    @Async("asyncExecutor")
    public CompletableFuture<CalendarioModel> findByFechaActualAsync(String fechaActual) {
        return CompletableFuture.completedFuture(this.repo.findByFechaActual(fechaActual));
    }

    @Async("asyncExecutor")
    public CompletableFuture<CalendarioModel> findByAnioAndSemanaAsync(int anio, int semana) {
        return CompletableFuture.completedFuture(this.repo.findByAnioAndSemana(anio, semana));
    }

    @Async("asyncExecutor")
    public CompletableFuture<ArrayList<CalendarioModel>> findByAnioAndMesAsync(int anio, String mes) {
        return CompletableFuture.completedFuture(this.repo.findByAnioAndMes(anio, mes));
    }
}

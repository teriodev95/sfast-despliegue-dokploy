package tech.calaverita.sfast_xpress.services;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import tech.calaverita.sfast_xpress.f_by_f_cierre_agencia.pojo.Calendario;
import tech.calaverita.sfast_xpress.models.mariaDB.CalendarioModel;
import tech.calaverita.sfast_xpress.repositories.CalendarioRepository;

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

    public Calendario getSemanaAnteriorByAnioSemana(int anio, int semana) {
        if (semana == 1) {
            anio = anio - 1;
            semana = this.repo.existsByAnioAndSemana(anio, 53) ? 53 : 52;
        } else {
            semana = semana - 1;
        }
        return new Calendario(anio, semana);
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

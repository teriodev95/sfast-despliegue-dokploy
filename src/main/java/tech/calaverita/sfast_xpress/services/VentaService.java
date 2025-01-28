package tech.calaverita.sfast_xpress.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import tech.calaverita.sfast_xpress.models.mariaDB.VentaModel;
import tech.calaverita.sfast_xpress.repositories.VentaRepository;

@Service
public class VentaService {
    private final VentaRepository repo;

    public VentaService(VentaRepository repo) {
        this.repo = repo;
    }

    public VentaModel save(VentaModel ventaModel) {
        return this.repo.save(ventaModel);
    }

    public ArrayList<VentaModel> findByAgenciaAnioAndSemana(String agencia, int anio, int semana) {
        return this.repo.findByAgenciaAndAnioAndSemana(agencia, anio, semana);
    }

    public List<VentaModel> findByGerenciaAnioAndSemana(String gerencia, int anio, int semana) {
        return this.repo.findByGerenciaAndAnioAndSemana(gerencia, anio, semana);
    }

    public ArrayList<VentaModel> findByGerenciaCreatedAtLessThanEqualAnioAndSemana(String gerencia, String createdAt,
            int anio, int semana) {
        return this.repo.findByGerenciaAndCreatedAtLessThanEqualAndAnioAndSemana(gerencia, createdAt, anio, semana);
    }

    public ArrayList<VentaModel> findByAgenciaFechaAnioAndSemana(String agencia, String fecha, int anio, int semana) {
        return this.repo.findByAgenciaAndFechaAndAnioAndSemana(agencia, fecha, anio, semana);
    }

    @Async("asyncExecutor")
    public CompletableFuture<ArrayList<VentaModel>> findByAgenciaAnioAndSemanaAsync(String agencia, int anio,
            int semana) {
        return CompletableFuture.completedFuture(this.repo.findByAgenciaAndAnioAndSemana(agencia, anio, semana));
    }

    @Async("asyncExecutor")
    public CompletableFuture<List<VentaModel>> findByGerenciaAnioSemanaAsync(String gerencia, int anio, int semana) {
        List<VentaModel> ventaModels = this.repo.findByGerenciaAndAnioAndSemana(gerencia, anio, semana);

        return CompletableFuture.completedFuture(ventaModels);
    }
}

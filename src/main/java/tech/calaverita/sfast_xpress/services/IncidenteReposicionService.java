package tech.calaverita.sfast_xpress.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import tech.calaverita.sfast_xpress.models.mariaDB.IncidenteReposicionModel;
import tech.calaverita.sfast_xpress.repositories.IncidenteReposicionRepository;

@Service
public class IncidenteReposicionService {
    private final IncidenteReposicionRepository incidenteReposicionRepository;

    public IncidenteReposicionService(IncidenteReposicionRepository incidenteReposicionRepository) {
        this.incidenteReposicionRepository = incidenteReposicionRepository;
    }

    @Async("asyncExecutor")
    public CompletableFuture<List<IncidenteReposicionModel>> findByGerenciaAnioSemanaAsync(String gerencia, int anio,
            int semana) {
        List<IncidenteReposicionModel> incidenteReposicionModels = this.incidenteReposicionRepository
                .findByGerenciaAndAnioAndSemana(gerencia, anio, semana);

        return CompletableFuture.completedFuture(incidenteReposicionModels);
    }

    @Async("asyncExecutor")
    public CompletableFuture<List<IncidenteReposicionModel>> findByCategoriaGerenciaAnioSemanaAsync(String categoria,
            String gerencia, int anio,
            int semana) {
        List<IncidenteReposicionModel> incidenteReposicionModels = this.incidenteReposicionRepository
                .findByCategoriaAndGerenciaAndAnioAndSemana(categoria, gerencia, anio, semana);

        return CompletableFuture.completedFuture(incidenteReposicionModels);
    }
}

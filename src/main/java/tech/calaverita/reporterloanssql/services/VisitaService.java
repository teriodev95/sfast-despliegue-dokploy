package tech.calaverita.reporterloanssql.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.mariaDB.VisitaModel;
import tech.calaverita.reporterloanssql.repositories.VisitaRepository;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

@Service
public class VisitaService {
    private final VisitaRepository repo;

    public VisitaService(VisitaRepository repo) {
        this.repo = repo;
    }

    public void save(VisitaModel visitaModel) {
        this.repo.save(visitaModel);
    }

    public ArrayList<VisitaModel> findAll() {
        return (ArrayList<VisitaModel>) repo.findAll();
    }

    public VisitaModel findById(String visitaId) {
        return this.repo.findById(visitaId).orElse(null);
    }

    public ArrayList<VisitaModel> findByPrestamoId(String prestamoId) {
        return this.repo.findByPrestamoId(prestamoId);
    }

    public ArrayList<VisitaModel> findByPrestamoIdAnioAndSemana(String prestamoId, int anio, int semana) {
        return this.repo.findByPrestamoIdAndAnioAndSemana(prestamoId, anio, semana);
    }

    @Async("asyncExecutor")
    public CompletableFuture<ArrayList<VisitaModel>> findByGerenciasAnioAndSemanaAsync(ArrayList<String> gerencias, int anio, int semana) {
        return CompletableFuture.completedFuture(this.repo.findByGerenciasAndAnioAndSemanaInnerJoinAgenciaModel(gerencias, anio, semana));
    }
}

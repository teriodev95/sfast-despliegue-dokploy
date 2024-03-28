package tech.calaverita.reporterloanssql.services;

import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.mariaDB.AsignacionModel;
import tech.calaverita.reporterloanssql.repositories.AsignacionRepository;

import java.util.ArrayList;

@Service
public final class AsignacionService {
    private final AsignacionRepository repo;

    public AsignacionService(AsignacionRepository repo) {
        this.repo = repo;
    }

    public AsignacionModel save(AsignacionModel model) {
        return this.repo.save(model);
    }

    public AsignacionModel findById(String id) {
        return this.repo.findById(id).orElseThrow();
    }

    public ArrayList<AsignacionModel> findByAgenciaAnioAndSemana(String agencia, int anio, int semana) {
        return this.repo.findByAgenciaAndAnioAndSemana(agencia, anio, semana);
    }

    public ArrayList<AsignacionModel> findAll() {
        return (ArrayList<AsignacionModel>) this.repo.findAll();
    }

    public double findSumaAsigancionesByAgenciaAnioAndSemana(String agencia, int anio, int semana) {
        return this.repo.findSumaAsigancionesByAgenciaAnioAndSemana(agencia, anio, semana);
    }
}

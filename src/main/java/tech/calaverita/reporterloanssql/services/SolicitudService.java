package tech.calaverita.reporterloanssql.services;

import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.mariaDB.SolicitudModel;
import tech.calaverita.reporterloanssql.repositories.SolicitudRepository;

import java.util.ArrayList;

@Service
public class SolicitudService {
    private final SolicitudRepository repo;

    public SolicitudService(SolicitudRepository repo) {
        this.repo = repo;
    }

    public SolicitudModel save(SolicitudModel model) {
        return this.repo.save(model);
    }

    public Boolean existsById(String id) {
        return this.repo.existsById(id);
    }

    public ArrayList<SolicitudModel> findByGerencia(String gerencia) {
        return this.repo.findByGerencia(gerencia);
    }

    public ArrayList<SolicitudModel> findByAgencia(String agencia) {
        return this.repo.findByAgencia(agencia);
    }

    public SolicitudModel findById(String id) {
        return this.repo.findById(id).orElseThrow();
    }
}

package tech.calaverita.reporterloanssql.services.reportes;

import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.mongoDB.ReporteGeneralGerenciaDocument;
import tech.calaverita.reporterloanssql.repositories.reportes.ReporteGeneralGerenciaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public final class ReporteGeneralGerenciaService {
    private final ReporteGeneralGerenciaRepository repo;

    private ReporteGeneralGerenciaService(
            ReporteGeneralGerenciaRepository repo
    ) {
        this.repo = repo;
    }

    public ReporteGeneralGerenciaDocument insert(
            ReporteGeneralGerenciaDocument document
    ) {
        return this.repo.insert(document);
    }

    public List<ReporteGeneralGerenciaDocument> insert(
            ArrayList<ReporteGeneralGerenciaDocument> documents
    ) {
        return this.repo.insert(documents);
    }

    public boolean existsById(
            String id
    ) {
        return this.repo.existsById(id);
    }

    public void deleteById(
            String id
    ){
        this.repo.deleteById(id);
    }

    public Optional<ReporteGeneralGerenciaDocument> findById(String id) {
        return this.repo.findById(id);
    }
}

package tech.calaverita.reporterloanssql.services.reportes;

import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.persistence.documents.ReporteDiarioAgenciasDocument;
import tech.calaverita.reporterloanssql.persistence.repositories.reportes.ReporteDiarioAgenciasRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public final class ReporteDiarioAgenciasService {
    private final ReporteDiarioAgenciasRepository repo;

    private ReporteDiarioAgenciasService(
            ReporteDiarioAgenciasRepository repo
    ) {
        this.repo = repo;
    }

    public ReporteDiarioAgenciasDocument insert(
            ReporteDiarioAgenciasDocument document
    ) {
        return this.repo.save(document);
    }

    public List<ReporteDiarioAgenciasDocument> insert(
            ArrayList<ReporteDiarioAgenciasDocument> documents
    ) {
        return this.repo.saveAll(documents);
    }
}

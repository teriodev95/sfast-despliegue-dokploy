package tech.calaverita.reporterloanssql.persistence.repositories.reportes;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.persistence.documents.ReporteDiarioAgenciasDocument;

@Repository
public interface ReporteDiarioAgenciasRepository extends MongoRepository<ReporteDiarioAgenciasDocument, Integer> {
}

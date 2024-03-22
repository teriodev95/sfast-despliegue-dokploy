package tech.calaverita.reporterloanssql.repositories.reportes;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.mongoDB.ReporteDiarioAgenciasDocument;

@Repository
public interface ReporteDiarioAgenciasRepository extends MongoRepository<ReporteDiarioAgenciasDocument, Integer> {
}

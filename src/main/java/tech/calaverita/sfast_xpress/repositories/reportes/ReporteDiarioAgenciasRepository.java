package tech.calaverita.sfast_xpress.repositories.reportes;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.sfast_xpress.models.mongoDB.ReporteDiarioAgenciasDocument;

@Repository
public interface ReporteDiarioAgenciasRepository extends MongoRepository<ReporteDiarioAgenciasDocument, Integer> {
}

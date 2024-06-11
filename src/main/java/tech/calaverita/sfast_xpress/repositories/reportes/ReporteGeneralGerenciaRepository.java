package tech.calaverita.sfast_xpress.repositories.reportes;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.sfast_xpress.models.mongoDB.ReporteGeneralGerenciaDocument;

@Repository
public interface ReporteGeneralGerenciaRepository extends MongoRepository<ReporteGeneralGerenciaDocument, String> {
}

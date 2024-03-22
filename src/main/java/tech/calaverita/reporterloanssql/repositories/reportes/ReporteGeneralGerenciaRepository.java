package tech.calaverita.reporterloanssql.repositories.reportes;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.mongoDB.ReporteGeneralGerenciaDocument;

@Repository
public interface ReporteGeneralGerenciaRepository extends MongoRepository<ReporteGeneralGerenciaDocument, String> {
}

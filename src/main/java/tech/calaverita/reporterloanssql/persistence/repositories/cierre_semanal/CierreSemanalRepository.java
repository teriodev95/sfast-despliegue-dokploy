package tech.calaverita.reporterloanssql.persistence.repositories.cierre_semanal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.persistence.entities.cierre_semanal.CierreSemanalEntity;

import java.util.Optional;

@Repository
public interface CierreSemanalRepository extends CrudRepository<CierreSemanalEntity, String> {
    @Query(value = "SELECT cs.* FROM cierres_semanales cs " +
            "WHERE cs.id LIKE %:agenciaAnioAndSemana", nativeQuery = true)
    Optional<CierreSemanalEntity> findByAgenciaAnioAndSemana(String agenciaAnioAndSemana);
}

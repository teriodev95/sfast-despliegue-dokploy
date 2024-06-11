package tech.calaverita.sfast_xpress.repositories.cierre_semanal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal.CierreSemanalModel;

import java.util.Optional;

@Repository
public interface CierreSemanalRepository extends CrudRepository<CierreSemanalModel, String> {
    @Query(value = "SELECT cs.* FROM cierres_semanales cs " +
            "WHERE cs.id LIKE %:agenciaAnioAndSemana", nativeQuery = true)
    Optional<CierreSemanalModel> findByAgenciaAnioAndSemana(String agenciaAnioAndSemana);
}

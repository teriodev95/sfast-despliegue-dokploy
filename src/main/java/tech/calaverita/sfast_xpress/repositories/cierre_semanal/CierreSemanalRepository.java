package tech.calaverita.sfast_xpress.repositories.cierre_semanal;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal.CierreSemanalModel;

@Repository
public interface CierreSemanalRepository extends CrudRepository<CierreSemanalModel, String> {
    Optional<CierreSemanalModel> findByAgenciaAndAnioAndSemana(String agencia, Integer anio, Integer semana);

    @Query("SELECT cierr.pagoComisionCobranza FROM CierreSemanalModel cierr WHERE cierr.agencia = :agencia AND cierr.anio = :anio "
            + "AND cierr.semana = :semana")
    Double findComisionCobranzaAgenciaByAgenciaAndAnioAndSemana(String agencia, Integer anio, Integer semana);
}

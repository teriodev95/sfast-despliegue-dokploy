package tech.calaverita.sfast_xpress.repositories.cierre_semanal;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal.EgresosGerenteModel;

@Repository
public interface EgresosGerenteRepository extends CrudRepository<EgresosGerenteModel, String> {
    @Query(value = "select comision_cobranza_agencia(:agencia, :anio, :semana)", nativeQuery = true)
    Double findComisionCobranzaAgenciaByAgenciaAndAnioAndSemana(String agencia, int anio, int semana);

    @Query("SELECT egr_ger FROM EgresosGerenteModel egr_ger WHERE id like concat(:gerencia, '%', '-', :anio, '-', :semana)")
    ArrayList<EgresosGerenteModel> findByIdLike(String gerencia, int anio, int semana);
}

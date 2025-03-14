package tech.calaverita.sfast_xpress.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.sfast_xpress.models.mariaDB.SolicitudModel;

import java.util.ArrayList;

@Repository
public interface SolicitudRepository extends CrudRepository<SolicitudModel, String> {
    ArrayList<SolicitudModel> findByGerencia(String gerencia);

    ArrayList<SolicitudModel> findByAgencia(String agencia);

    @Query("SELECT s FROM SolicitudModel s WHERE s.agencia = :agencia AND s.anio = :anio AND s.semana BETWEEN :semanaAnterior AND :semanaActual AND s.status LIKE %:status%")
    ArrayList<SolicitudModel> findByAgenciaAnioStatusYEntre3Semanas(String agencia, Integer anio, Integer semanaActual, Integer semanaAnterior, String status);

    @Query("SELECT s FROM SolicitudModel s WHERE s.agencia = :agencia AND s.anio = :anio AND s.semana = :semana AND s.status LIKE %:status%")
    ArrayList<SolicitudModel> findByAgenciaAnioSemanasYStatus(String agencia, Integer anio, Integer semana, String status);
}

package tech.calaverita.reporterloanssql.persistence.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.persistence.entities.PagoEntity;
import tech.calaverita.reporterloanssql.persistence.entities.VisitaEntity;

import java.util.ArrayList;

@Repository
public interface VisitaRepository extends CrudRepository<VisitaEntity, String> {
    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @Query("SELECT vi " +
            "FROM VisitaEntity vi")
    ArrayList<VisitaEntity> darrvisEntFindAll();

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT vi " +
            "FROM VisitaEntity vi " +
            "WHERE vi.visitaId = :visitaId")
    VisitaEntity visEntFindByVisitaId(
            String visitaId
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT vi " +
            "FROM VisitaEntity vi " +
            "WHERE vi.prestamoId = :prestamoId")
    ArrayList<VisitaEntity> darrvisEntFindByPrestamoId(
            String prestamoId
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT vis " +
            "FROM VisitaEntity vis " +
            "WHERE vis.agente = :agencia " +
            "AND vis.anio = :anio " +
            "AND vis.semana = :semana")
    ArrayList<VisitaEntity> darrvisEntFindByAgenciaAnioAndSemana(
            String agencia,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT vis " +
            "FROM VisitaEntity vis " +
            "WHERE vis.prestamoId = :prestamoId " +
            "AND vis.anio = :anio " +
            "AND vis.semana = :semana")
    ArrayList<VisitaEntity> darrVisEntFindByPrestamoIdAnioAndSemana(
            String prestamoId,
            int anio,
            int semana
    );

    @Query(value = "select * from visitas vis " +
            "inner join agencias agenc " +
            "on agenc.AgenciaID = vis.Agente " +
            "and vis.anio = :anio " +
            "and vis.semana = :semana " +
            "and agenc.GerenciaID in :gerencias", nativeQuery = true)
    ArrayList<VisitaEntity> findByGerenciasAndAnioAndSemana(ArrayList<String> gerencias, int anio, int semana);
}

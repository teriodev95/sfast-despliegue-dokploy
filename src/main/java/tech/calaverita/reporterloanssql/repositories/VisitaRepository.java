package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.mariaDB.VisitaModel;

import java.util.ArrayList;

@Repository
public interface VisitaRepository extends CrudRepository<VisitaModel, String> {
    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @Query("SELECT vi " +
            "FROM VisitaModel vi")
    ArrayList<VisitaModel> darrvisEntFindAll();

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT vi " +
            "FROM VisitaModel vi " +
            "WHERE vi.visitaId = :visitaId")
    VisitaModel visEntFindByVisitaId(
            String visitaId
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT vi " +
            "FROM VisitaModel vi " +
            "WHERE vi.prestamoId = :prestamoId")
    ArrayList<VisitaModel> darrvisEntFindByPrestamoId(
            String prestamoId
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT vis " +
            "FROM VisitaModel vis " +
            "WHERE vis.agente = :agencia " +
            "AND vis.anio = :anio " +
            "AND vis.semana = :semana")
    ArrayList<VisitaModel> darrvisEntFindByAgenciaAnioAndSemana(
            String agencia,
            int anio,
            int semana
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT vis " +
            "FROM VisitaModel vis " +
            "WHERE vis.prestamoId = :prestamoId " +
            "AND vis.anio = :anio " +
            "AND vis.semana = :semana")
    ArrayList<VisitaModel> darrVisEntFindByPrestamoIdAnioAndSemana(
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
    ArrayList<VisitaModel> findByGerenciasAndAnioAndSemana(ArrayList<String> gerencias, int anio, int semana);
}

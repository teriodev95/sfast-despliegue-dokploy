package tech.calaverita.sfast_xpress.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.sfast_xpress.models.mariaDB.VisitaModel;

import java.util.ArrayList;

@Repository
public interface VisitaRepository extends CrudRepository<VisitaModel, String> {
    ArrayList<VisitaModel> findByPrestamoId(String prestamoId);

    ArrayList<VisitaModel> findByPrestamoIdAndAnioAndSemana(String prestamoId, int anio, int semana);

    @Query("SELECT vis FROM VisitaModel vis INNER JOIN AgenciaModel agenc ON agenc.id = vis.agente " +
            "AND vis.anio = :anio AND vis.semana = :semana AND agenc.gerenciaId IN :gerencias")
    ArrayList<VisitaModel> findByGerenciasAndAnioAndSemanaInnerJoinAgenciaModel(ArrayList<String> gerencias, int anio, int semana);
}

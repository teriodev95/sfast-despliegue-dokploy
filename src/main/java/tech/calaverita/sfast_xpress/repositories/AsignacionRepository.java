package tech.calaverita.sfast_xpress.repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tech.calaverita.sfast_xpress.models.mariaDB.AsignacionModel;

@Repository
public interface AsignacionRepository extends CrudRepository<AsignacionModel, String> {
        ArrayList<AsignacionModel> findByAgenciaAndAnioAndSemana(String agencia, int anio, int semana);

        ArrayList<AsignacionModel> findByQuienRecibioUsuarioIdAndAnioAndSemana(Integer quienRecibioUsuarioId,
                        int anio, int semana);

        ArrayList<AsignacionModel> findByQuienEntregoUsuarioIdAndAnioAndSemana(Integer quienEntregoUsuarioId,
                        int anio, int semana);

        @Query("SELECT IF((SUM(asign.monto) IS NULL), 0, SUM(asign.monto)) FROM AsignacionModel asign " +
                        "WHERE asign.quienEntregoUsuarioId = :quienEntregoUsuarioId AND asign.anio = :anio AND asign.semana = :semana")
        double findSumaAsigancionesByQuienEntregoUsuarioIdAnioAndSemana(Integer quienEntregoUsuarioId, int anio, int semana);

        @Query("SELECT asign FROM AsignacionModel asign INNER JOIN UsuarioModel usuar ON asign.quienEntregoUsuarioId = usuar.usuarioId "
                        + "AND usuar.tipo = :tipo WHERE asign.quienRecibioUsuarioId = :quienRecibioUsuarioId AND asign.anio = :anio "
                        + "AND asign.semana = :semana")
        ArrayList<AsignacionModel> findByQuienRecibioUsuarioIdAndAnioAndSemanaAndTipoInnerJoinUsuarioModel(
                        Integer quienRecibioUsuarioId, int anio,
                        int semana, String tipo);

        @Query("SELECT asign FROM AsignacionModel asign INNER JOIN UsuarioModel usuar ON asign.quienRecibioUsuarioId = usuar.usuarioId "
                        + "AND usuar.tipo = :tipo WHERE asign.quienEntregoUsuarioId = :quienEntregoUsuarioId AND asign.anio = :anio "
                        + "AND asign.semana = :semana")
        ArrayList<AsignacionModel> findByQuienEntregoUsuarioIdAndAnioAndSemanaAndTipoInnerJoinUsuarioModel(
                        Integer quienEntregoUsuarioId, int anio,
                        int semana, String tipo);
}

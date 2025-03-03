package tech.calaverita.sfast_xpress.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tech.calaverita.sfast_xpress.models.mariaDB.TabulacionDineroModel;

@Repository
public interface TabulacionDineroRepository extends CrudRepository<TabulacionDineroModel, Integer> {
    Boolean existsByUsuarioIdAndAnioAndSemana(Integer usuarioId, Integer anio, Integer semana);

    TabulacionDineroModel findByUsuarioIdAndAnioAndSemana(Integer usuarioId, Integer anio, Integer semana);

    @Query("SELECT tab.id FROM TabulacionDineroModel tab WHERE tab.usuarioId = :usuarioId AND tab.anio = :anio "
            + "AND tab.semana = :semana")
    int findIdByUsuarioIdAndAnioAndSemana(Integer usuarioId, Integer anio, Integer semana);
}

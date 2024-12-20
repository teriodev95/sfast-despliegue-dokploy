package tech.calaverita.sfast_xpress.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tech.calaverita.sfast_xpress.models.TabulacionDineroModel;

@Repository
public interface TabulacionDineroRepository extends CrudRepository<TabulacionDineroModel, Integer> {
    Boolean existsByUsuarioIdAndAnioAndSemana(Integer usuarioId, Integer anio, Integer semana);

    TabulacionDineroModel findByUsuarioIdAndAnioAndSemana(Integer usuarioId, Integer anio, Integer semana);
}

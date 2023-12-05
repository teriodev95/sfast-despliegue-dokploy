package tech.calaverita.reporterloanssql.persistence.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.persistence.entities.CalendarioEntity;
import tech.calaverita.reporterloanssql.services.CalendarioService;

@Repository
public interface CalendarioRepository extends CrudRepository<CalendarioEntity, Integer> {
    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @Query("SELECT ca " +
            "FROM CalendarioEntity ca " +
            "WHERE ca.desde <= :fechaActual " +
            "AND ca.hasta >= :fechaActual")
    CalendarioEntity findByFechaActual(
            String fechaActual
    );

    Boolean existsByAnioAndSemana(
            int anio,
            int semana
    );

    CalendarioEntity findByAnioAndSemana(int anio, int semana);
}

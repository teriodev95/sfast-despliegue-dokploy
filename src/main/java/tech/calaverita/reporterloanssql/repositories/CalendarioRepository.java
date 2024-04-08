package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.mariaDB.CalendarioModel;

@Repository
public interface CalendarioRepository extends CrudRepository<CalendarioModel, Integer> {
    boolean existsByAnioAndSemana(int anio, int semana);

    CalendarioModel findByAnioAndSemana(int anio, int semana);

    @Query("SELECT cal FROM CalendarioModel cal WHERE cal.desde <= :fechaActual AND cal.hasta >= :fechaActual")
    CalendarioModel findByFechaActual(String fechaActual);
}

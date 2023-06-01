package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.CalendarioModel;

@Repository
public interface CalendarioRepository extends CrudRepository<CalendarioModel, Integer> {
    @Query("SELECT ca FROM CalendarioModel ca WHERE ca.desde <= :fechaActual AND ca.hasta >= :fechaActual")
    CalendarioModel getSemanaActualXpressByFechaActual(String fechaActual);
}

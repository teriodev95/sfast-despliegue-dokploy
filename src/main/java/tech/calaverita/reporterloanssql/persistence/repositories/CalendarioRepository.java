package tech.calaverita.reporterloanssql.persistence.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.persistence.entities.CalendarioEntity;

@Repository
public interface CalendarioRepository extends CrudRepository<CalendarioEntity, Integer> {
    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @Query("SELECT ca " +
            "FROM CalendarioEntity ca " +
            "WHERE ca.desde <= :fechaActual " +
            "AND ca.hasta >= :fechaActual")
    CalendarioEntity calModFindBySemanaActualXpressByFechaActual(
            String fechaActual
    );
}

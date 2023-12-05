package tech.calaverita.reporterloanssql.persistence.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.persistence.entities.AgenciaEntity;

import java.util.ArrayList;

@Repository
public interface AgenciaRepository extends CrudRepository<AgenciaEntity, String> {
    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @Query("SELECT ag FROM AgenciaEntity ag WHERE ag.agenciaId = :agenciaId")
    AgenciaEntity agencModFindByAgenciaId(
            String agenciaId
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT ag.agenciaId " +
            "FROM AgenciaEntity ag " +
            "WHERE ag.gerenciaId = :gerenciaId " +
            "ORDER BY ag.agenciaId")
    ArrayList<String> darrstrAgenciaIdFindByGerenciaId(
            String gerenciaId
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    ArrayList<AgenciaEntity> findByGerenciaId(
            String gerenciaId
    );
}

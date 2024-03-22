package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.mariaDB.AgenciaModel;

import java.util.ArrayList;

@Repository
public interface AgenciaRepository extends CrudRepository<AgenciaModel, String> {
    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @Query("SELECT ag FROM AgenciaModel ag WHERE ag.id = :agenciaId")
    AgenciaModel agencModFindByAgenciaId(
            String agenciaId
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT ag.id " +
            "FROM AgenciaModel ag " +
            "WHERE ag.gerenciaId = :gerenciaId " +
            "ORDER BY ag.id")
    ArrayList<String> darrstrAgenciaIdFindByGerenciaId(
            String gerenciaId
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    ArrayList<AgenciaModel> findByGerenciaId(
            String gerenciaId
    );
}

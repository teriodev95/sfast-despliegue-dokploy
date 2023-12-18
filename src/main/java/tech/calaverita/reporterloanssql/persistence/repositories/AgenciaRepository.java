package tech.calaverita.reporterloanssql.persistence.repositories;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.persistence.entities.AgenciaEntity;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface AgenciaRepository extends CrudRepository<AgenciaEntity, String> {
    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @Query("SELECT ag FROM AgenciaEntity ag WHERE ag.id = :agenciaId")
    AgenciaEntity agencModFindByAgenciaId(
            String agenciaId
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Query("SELECT ag.id " +
            "FROM AgenciaEntity ag " +
            "WHERE ag.gerenciaId = :gerenciaId " +
            "ORDER BY ag.id")
    ArrayList<String> darrstrAgenciaIdFindByGerenciaId(
            String gerenciaId
    );

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    ArrayList<AgenciaEntity> findByGerenciaId(
            String gerenciaId
    );
}

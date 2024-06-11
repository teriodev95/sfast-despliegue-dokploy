package tech.calaverita.sfast_xpress.repositories.relation;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.sfast_xpress.models.mariaDB.relation.UsuarioGerenciaModel;

import java.util.ArrayList;

@Repository
public interface UsuarioGerenciaRepository extends CrudRepository<UsuarioGerenciaModel, Integer> {
    @Query("SELECT ugm.gerenciaId FROM UsuarioGerenciaModel ugm WHERE ugm.usuarioId = :usuarioId")
    ArrayList<String> darrstrGerenciaIdFindByUsuarioId(int usuarioId);
}

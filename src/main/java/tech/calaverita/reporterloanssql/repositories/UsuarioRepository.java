package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.UsuarioModel;

@Repository
public interface UsuarioRepository extends CrudRepository<UsuarioModel, Integer> {
    @Query("SELECT us FROM UsuarioModel us WHERE us.usuario = :usuario")
    UsuarioModel getUsuarioByUsuario(String usuario);
}

package tech.calaverita.sfast_xpress.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.sfast_xpress.models.mariaDB.SolicitudModel;

import java.util.ArrayList;

@Repository
public interface SolicitudRepository extends CrudRepository<SolicitudModel, String> {
    ArrayList<SolicitudModel> findByGerencia(String gerencia);

    ArrayList<SolicitudModel> findByAgencia(String agencia);
}

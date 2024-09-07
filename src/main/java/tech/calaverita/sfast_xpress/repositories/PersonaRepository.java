package tech.calaverita.sfast_xpress.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tech.calaverita.sfast_xpress.models.mariaDB.PersonaModel;

@Repository
public interface PersonaRepository extends CrudRepository<PersonaModel, String> {

}

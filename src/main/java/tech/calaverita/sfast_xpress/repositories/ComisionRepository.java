package tech.calaverita.sfast_xpress.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tech.calaverita.sfast_xpress.models.mariaDB.ComisionModel;

@Repository
public interface ComisionRepository extends CrudRepository<ComisionModel, Integer> { 

}

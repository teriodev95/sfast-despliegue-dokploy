package tech.calaverita.sfast_xpress.repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tech.calaverita.sfast_xpress.models.mariaDB.PersonaModel;

@Repository
public interface PersonaRepository extends CrudRepository<PersonaModel, String> {
    @Query(value = "SELECT pers.*, " +
            "IF(trim(nombres) like :inicioNombres%, 2, 0) + " +
            "IF(trim(nombres) like %:finalNombres%, 1, 0) + " +
            "IF(trim(apellido_paterno) like :inicioApellidoPaterno%, 2, 0) + " +
            "IF(trim(apellido_paterno) like %:finalApellidoPaterno, 1, 0) + " +
            "IF(trim(apellido_materno) like :inicioApellidoMaterno%, 2, 0) +" +
            "IF(trim(apellido_materno) like %:finalApellidoMaterno, 1, 0) " +
            "coincidencias FROM personas pers " +
            "WHERE (TRIM(pers.nombres) like :inicioNombres% or TRIM(pers.nombres) like %:finalNombres%) "
            +
            "OR (TRIM(pers.apellido_paterno) like :inicioApellidoPaterno% or TRIM(pers.apellido_paterno) like %:finalApellidoPaterno) "
            +
            "OR (TRIM(pers.apellido_materno) like :inicioApellidoMaterno% or TRIM(pers.apellido_materno) like %:finalApellidoMaterno) "
            +
            "HAVING coincidencias >= 4 order by coincidencias desc", nativeQuery = true)
    ArrayList<PersonaModel> findByNombresOrApellidoPaternoOrApellidoMaterno(String inicioNombres,
            String finalNombres,
            String inicioApellidoPaterno, String finalApellidoPaterno, String inicioApellidoMaterno,
            String finalApellidoMaterno);
}

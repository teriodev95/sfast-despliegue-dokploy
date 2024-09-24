package tech.calaverita.sfast_xpress.services;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import tech.calaverita.sfast_xpress.models.mariaDB.PersonaModel;
import tech.calaverita.sfast_xpress.repositories.PersonaRepository;

@Service
public class PersonaService {
    private final PersonaRepository personaRepository;

    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    public PersonaModel findById(String id) {
        return this.personaRepository.findById(id).orElse(null);
    }

    public ArrayList<PersonaModel> findByNombresOrApellidoPaternoOrApellidoMaterno(String inicioNombres,
            String finalNombres, String inicioApellidoPaterno, String finalApellidoPaterno,
            String inicioApellidoMaterno, String finalApellidoMaterno) {
        return this.personaRepository.findByNombresOrApellidoPaternoOrApellidoMaterno(inicioNombres, finalNombres,
                inicioApellidoPaterno, finalApellidoPaterno, inicioApellidoMaterno, finalApellidoMaterno);
    }
}

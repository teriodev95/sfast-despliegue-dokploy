package tech.calaverita.sfast_xpress.services;

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
}

package tech.calaverita.sfast_xpress.services;

import org.springframework.stereotype.Service;

import tech.calaverita.sfast_xpress.models.mariaDB.ComisionModel;
import tech.calaverita.sfast_xpress.repositories.ComisionRepository;

@Service
public class ComisionService {
    private ComisionRepository comisionRepository;

    public ComisionService(ComisionRepository comisionRepository) {
        this.comisionRepository = comisionRepository;
    }

    public ComisionModel save(ComisionModel comisionModel) {
        return this.comisionRepository.save(comisionModel);
    }

    public ComisionModel findByAgenciaAnioAndSemana(String agencia, Integer anio, Integer semana) {
        return this.comisionRepository.findByAgenciaAndAnioAndSemana(agencia, anio, semana);
    }
}

package tech.calaverita.sfast_xpress.services;

import org.springframework.stereotype.Service;

import tech.calaverita.sfast_xpress.models.TabulacionDineroModel;
import tech.calaverita.sfast_xpress.repositories.TabulacionDineroRepository;

@Service
public class TabulacionDineroService {
    private TabulacionDineroRepository tabulacionDineroRepository;

    public TabulacionDineroService(TabulacionDineroRepository tabulacionDineroRepository) {
        this.tabulacionDineroRepository = tabulacionDineroRepository;
    }

    public Boolean existsByUsuarioIdAnioAndSemana(Integer usuarioId, Integer anio, Integer semana) {
        return this.tabulacionDineroRepository.existsByUsuarioIdAndAnioAndSemana(usuarioId, anio, semana);
    }

    public TabulacionDineroModel findByUsuarioIdAnioAndSemana(Integer usuarioId, Integer anio, Integer semana) {
        return this.tabulacionDineroRepository.findByUsuarioIdAndAnioAndSemana(usuarioId, anio, semana);
    }

    public TabulacionDineroModel save(TabulacionDineroModel tabulacionDineroModel) {
        return this.tabulacionDineroRepository.save(tabulacionDineroModel);
    }
}

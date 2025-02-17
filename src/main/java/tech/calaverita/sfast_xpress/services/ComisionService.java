package tech.calaverita.sfast_xpress.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
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

    public List<ComisionModel> findByGerenciaAnioSemana(String gerencia, Integer anio, Integer semana) {
        return this.comisionRepository.findByGerenciaAndAnioAndSemana(gerencia, anio, semana);
    }

    @Async("asyncExecutor")
    public CompletableFuture<List<ComisionModel>> findByGerenciaAnioSemanaAsync(String gerencia, Integer anio,
            Integer semana) {
        return CompletableFuture.completedFuture(
                this.comisionRepository.findByGerenciaAndAnioAndSemana(gerencia, anio, semana));
    }
}

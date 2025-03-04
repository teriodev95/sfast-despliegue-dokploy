package tech.calaverita.sfast_xpress.services;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

import org.springframework.cglib.core.Local;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import tech.calaverita.sfast_xpress.models.mariaDB.CalendarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.TabulacionDineroModel;
import tech.calaverita.sfast_xpress.repositories.TabulacionDineroRepository;

@Service
public class TabulacionDineroService {
    private TabulacionDineroRepository tabulacionDineroRepository;
    private CalendarioService calendarioService;

    public TabulacionDineroService(TabulacionDineroRepository tabulacionDineroRepository,
            CalendarioService calendarioService) {
        this.tabulacionDineroRepository = tabulacionDineroRepository;
        this.calendarioService = calendarioService;
    }

    public Boolean existsByGerenciaAnioAndSemana(String gerencia, Integer anio, Integer semana) {
        return this.tabulacionDineroRepository.existsByGerenciaAndAnioAndSemana(gerencia, anio, semana);
    }

    public TabulacionDineroModel findByGerenciaAnioAndSemana(String gerencia, Integer anio, Integer semana) {
        return this.tabulacionDineroRepository.findByGerenciaAndAnioAndSemana(gerencia, anio, semana);
    }

    public TabulacionDineroModel save(TabulacionDineroModel tabulacionDineroModel) {
        return this.tabulacionDineroRepository.save(tabulacionDineroModel);
    }

    public Integer findIdByGerencia(String gerencia) {
        CalendarioModel calendarioModel = this.calendarioService.findByFechaActual(LocalDate.now().toString());

        // To easy code
        int anio = calendarioModel.getAnio();
        int semana = calendarioModel.getSemana();

        return this.tabulacionDineroRepository.findIdByGerenciaAndAnioAndSemana(gerencia, anio, semana);
    }

    public Integer findIdByGerenciaAnioSemana(String gerencia, Integer anio, Integer semana) {
        return this.tabulacionDineroRepository.findIdByGerenciaAndAnioAndSemana(gerencia, anio, semana);
    }

    @Async("asyncExecutor")
    public CompletableFuture<Integer> findIdByGerenciaAnioAndSemanaAsync(
            String gerencia, Integer anio, Integer semana) {
        return CompletableFuture.completedFuture(findIdByGerenciaAnioSemana(gerencia, anio, semana));
    }

    @Async("asyncExecutor")
    public CompletableFuture<Integer> findIdByGerenciaAsync(String gerencia) {
        return CompletableFuture.completedFuture(findIdByGerencia(gerencia));
    }
}

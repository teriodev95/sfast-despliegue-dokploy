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

    public Boolean existsByUsuarioIdAnioAndSemana(Integer usuarioId, Integer anio, Integer semana) {
        return this.tabulacionDineroRepository.existsByUsuarioIdAndAnioAndSemana(usuarioId, anio, semana);
    }

    public TabulacionDineroModel findByUsuarioIdAnioAndSemana(Integer usuarioId, Integer anio, Integer semana) {
        return this.tabulacionDineroRepository.findByUsuarioIdAndAnioAndSemana(usuarioId, anio, semana);
    }

    public TabulacionDineroModel save(TabulacionDineroModel tabulacionDineroModel) {
        return this.tabulacionDineroRepository.save(tabulacionDineroModel);
    }

    public int findIdByUsuarioId(Integer usuarioId) {
        CalendarioModel calendarioModel = this.calendarioService.findByFechaActual(LocalDate.now().toString());

        // To easy code
        int anio = calendarioModel.getAnio();
        int semana = calendarioModel.getSemana();

        return this.tabulacionDineroRepository.findIdByUsuarioIdAndAnioAndSemana(usuarioId, anio, semana);
    }

    public int findIdByUsuarioIdAnioSemana(Integer usuarioId, Integer anio, Integer semana) {
        return this.tabulacionDineroRepository.findIdByUsuarioIdAndAnioAndSemana(usuarioId, anio, semana);
    }

    @Async("asyncExecutor")
    public CompletableFuture<Integer> findIdByUsuarioIdAnioAndSemanaAsync(Integer usuarioId, Integer anio,
            Integer semana) {
        return CompletableFuture.completedFuture(findIdByUsuarioIdAnioSemana(usuarioId, anio, semana));
    }

    @Async("asyncExecutor")
    public CompletableFuture<Integer> findIdByUsuarioIdAsync(Integer usuarioId) {
        return CompletableFuture.completedFuture(findIdByUsuarioId(usuarioId));
    }
}

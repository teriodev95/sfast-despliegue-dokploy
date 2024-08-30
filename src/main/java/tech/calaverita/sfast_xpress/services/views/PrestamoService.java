package tech.calaverita.sfast_xpress.services.views;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoModel;
import tech.calaverita.sfast_xpress.repositories.views.PrestamoRepository;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

@Service
public class PrestamoService {
    private final PrestamoRepository repo;

    public PrestamoService(PrestamoRepository repo) {
        this.repo = repo;
    }

    @Async("asyncExecutor")
    public CompletableFuture<ArrayList<PrestamoModel>> findByAgenciaAndSaldoAlIniciarSemanaGreaterThan(String agencia,
            Double saldoAlIniciarSemana) {
        return CompletableFuture.completedFuture(
                this.repo.findByAgenteAndSaldoAlIniciarSemanaGreaterThan(agencia, saldoAlIniciarSemana));
    }

    @Cacheable("PrestamosPorFinalizarByAgenciaAnioAndSemana")
    public ArrayList<PrestamoModel> findPorFinalizarByAgenciaAnioAndSemana(String agencia, int anio, int semana) {
        return this.repo.findPorFinalizarByAgenteAndAnioAndSemana(agencia, anio, semana);
    }

    @Cacheable("PrestamosPorFinalizarByGerenciaAnioAndSemana")
    public ArrayList<PrestamoModel> findPorFinalizarByGerenciaAnioAndSemana(String sucursal, String gerencia, int anio,
            int semana) {
        return this.repo.findPorFinalizarByGerenciaAndAnioAndSemana(sucursal, gerencia, anio, semana);
    }

    public PrestamoModel prestModFindByPrestamoId(String strPrestamoId_I) {
        return this.repo.prestEntFindByPrestamoId(strPrestamoId_I);
    }

    public PrestamoModel findById(String strPrestamoId_I) {
        return this.repo.findById(strPrestamoId_I).orElse(null);
    }
}

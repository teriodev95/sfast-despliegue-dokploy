package tech.calaverita.sfast_xpress.services.views;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import tech.calaverita.sfast_xpress.models.mariaDB.PrestamoHistorialMigradoModel;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoViewModel;
import tech.calaverita.sfast_xpress.repositories.views.PrestamoViewRepository;

@Service
public class PrestamoViewService {
    private final PrestamoViewRepository repo;

    public PrestamoViewService(PrestamoViewRepository repo) {
        this.repo = repo;
    }

    public PrestamoViewModel findById(String id) {
        return this.repo.findById(id).orElse(null);
    }

    @Async("asyncExecutor")
    public CompletableFuture<ArrayList<PrestamoViewModel>> findByAgenciaSaldoAlIniciarSemanaGreaterThanAndNotAnioAndSemana(
            String agencia, Double saldoAlIniciarSemana, Integer anio, Integer semana) {
        return CompletableFuture.completedFuture(
                this.repo.findByAgenciaAndSaldoAlIniciarSemanaGreaterThanAndNotAnioAndSemana(agencia,
                        saldoAlIniciarSemana, anio, semana));
    }

    @Async("asyncExecutor")
    public CompletableFuture<Double> findDebitoTotalByAgenciaSaldoAlIniciarSemanaGreaterThanAndNotAnioAndSemana(
            String agencia, Double saldoAlIniciarSemana, Integer anio, Integer semana) {
        return CompletableFuture.completedFuture(
                this.repo.findDebitoTotalByAgenciaAndSaldoAlIniciarSemanaGreaterThanAndNotAnioAndSemana(agencia,
                        saldoAlIniciarSemana, anio, semana));
    }

    @Async("asyncExecutor")
    public CompletableFuture<ArrayList<PrestamoViewModel>> findByGerenciaSucursalAndSaldoAlIniciarSemanaGreaterThan(
            String gerencia, String sucursal, Double saldoAlIniciarSemana) {
        return CompletableFuture.completedFuture(
                this.repo.findByGerenciaAndSucursalAndSaldoAlIniciarSemanaGreaterThan(gerencia, sucursal,
                        saldoAlIniciarSemana));
    }

    @Cacheable("PrestamosPorFinalizarByAgenciaAnioAndSemana")
    public ArrayList<PrestamoViewModel> findPorFinalizarByAgenciaAnioAndSemana(String agencia, int anio,
            int semana) {
        return this.repo.findPorFinalizarByAgenciaAndAnioAndSemana(agencia, anio, semana);
    }

    @Cacheable("PrestamosPorFinalizarByGerenciaAnioAndSemana")
    public ArrayList<PrestamoViewModel> findPorFinalizarByGerenciaAnioAndSemana(String sucursal, String gerencia,
            int anio,
            int semana) {
        return this.repo.findPorFinalizarByGerenciaAndAnioAndSemana(sucursal, gerencia, anio, semana);
    }

    public ArrayList<PrestamoViewModel> darrprestUtilModFindByAgenciaAnioAndSemanaToDashboard(String strAgencia_I,
            int intAnio_I, int intSemana_I) {
        return this.repo.darrprestUtilEntFindByAgenciaAnioAndSemanaToDashboard(strAgencia_I, intAnio_I, intSemana_I);
    }

    public ArrayList<PrestamoViewModel> darrPrestUtilModFindByAgenciaAnioAndSemanaToCobranza(String strAgencia_I,
            int intAnio_I, int intSemana_I) {
        return this.repo.darrprestUtilEntFindByAgenciaAnioAndSemanaToCobranza(strAgencia_I, intAnio_I, intSemana_I);
    }

    public ArrayList<PrestamoViewModel> darrprestUtilModByAgenciaFechaPagoAnioAndSemanaToDashboard(String strAgencia_I,
            String strFechaPago_I, Integer anio, Integer semana) {
        return this.repo.darrprestUtilEntByAgenciaAndFechaPagoAndAnioAndSemanaToDashboard(strAgencia_I, strFechaPago_I,
                anio, semana);
    }

    public ArrayList<PrestamoViewModel> darrprestUtilEntByAgenciaAndFechaPagoLessThanEqualToDashboard(
            String strAgencia_I,
            String strFechaPago_I, int intAnio_I, int intSemana_I) {
        return this.repo.darrprestUtilEntByAgenciaAndFechaPagoLessThanEqualToDashboard(strAgencia_I, strFechaPago_I,
                intAnio_I,
                intSemana_I);
    }

    public ArrayList<PrestamoViewModel> darrprestUtilEntByGerenciaSucursalAndFechaPagoLessThanEqualToDashboard(
            String gerencia, String sucursal,
            String strFechaPago_I, int intAnio_I, int intSemana_I) {
        return this.repo.darrprestUtilEntByGerenciaAndSucursalAndFechaPagoLessThanEqualToDashboard(gerencia, sucursal,
                strFechaPago_I,
                intAnio_I,
                intSemana_I);
    }

    public PrestamoViewModel findByClienteId(String clienteId) {
        return this.repo.findByClienteId(clienteId);
    }

    public ArrayList<PrestamoViewModel> findByAgencia(String agencia) {
        return this.repo.findByAgencia(agencia);
    }

    public ArrayList<PrestamoHistorialMigradoModel> findHistorialByAgencia(String agencia) {
        return this.repo.findHistorialByAgencia(agencia);
    }

    public List<PrestamoViewModel> findPrestamosSinPagoByAgenciaSaldoAlIniciarSemanaGreaterThanAnioSemana(
            String agencia, Double saldoAlIniciarSemana, Integer anio, Integer semana) {
        return this.repo.findPrestamosSinPagoByAgenciaAndSaldoAlIniciarSemanaGreaterThanAndAnioAndSemana(agencia,
                saldoAlIniciarSemana, anio, semana);
    }
}

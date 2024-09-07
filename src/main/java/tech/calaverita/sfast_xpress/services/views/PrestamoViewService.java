package tech.calaverita.sfast_xpress.services.views;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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
    public CompletableFuture<ArrayList<PrestamoViewModel>> findByAgenciaAndSaldoAlIniciarSemanaGreaterThan(
            String agencia,
            Double saldoAlIniciarSemana) {
        return CompletableFuture.completedFuture(
                this.repo.findByAgenciaAndSaldoAlIniciarSemanaGreaterThan(agencia, saldoAlIniciarSemana));
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

    public ArrayList<PrestamoViewModel> darrprestUtilModByAgenciaAndFechaPagoToDashboard(String strAgencia_I,
            String strFechaPago_I) {
        return this.repo.darrprestUtilEntByAgenciaAndFechaPagoToDashboard(strAgencia_I, strFechaPago_I);
    }

    public ArrayList<PrestamoViewModel> findByNombresOrApellidoPaternoOrApellidoMaterno(String inicioNombres,
            String finalNombres, String inicioApellidoPaterno, String finalApellidoPaterno,
            String inicioApellidoMaterno, String finalApellidoMaterno) {
        return this.repo.findByNombresOrApellidoPaternoOrApellidoMaterno(inicioNombres, finalNombres,
        inicioApellidoPaterno, finalApellidoPaterno, inicioApellidoMaterno, finalApellidoMaterno);
    }
}

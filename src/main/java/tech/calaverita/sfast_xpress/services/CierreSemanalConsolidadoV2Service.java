package tech.calaverita.sfast_xpress.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import tech.calaverita.sfast_xpress.models.mariaDB.CierreSemanalConsolidadoV2Model;
import tech.calaverita.sfast_xpress.repositories.CierreSemanalConsolidadoV2Repository;

@Service
public class CierreSemanalConsolidadoV2Service {
    private CierreSemanalConsolidadoV2Repository cierreSemanalConsolidadoV2Repository;

    public CierreSemanalConsolidadoV2Service(
            CierreSemanalConsolidadoV2Repository cierreSemanalConsolidadoV2Repository) {
        this.cierreSemanalConsolidadoV2Repository = cierreSemanalConsolidadoV2Repository;
    }

    public CierreSemanalConsolidadoV2Model findByAgenciaAnioAndSemana(String agencia,
            Integer anio, Integer semana) {
        return this.cierreSemanalConsolidadoV2Repository.findByAgenciaAndAnioAndSemana(agencia, anio, semana);
    }

    public CierreSemanalConsolidadoV2Model save(CierreSemanalConsolidadoV2Model cierreSemanalConsolidadoV2Model) {
        return this.cierreSemanalConsolidadoV2Repository.save(cierreSemanalConsolidadoV2Model);
    }

    @Async("asyncExecutor")
    public CompletableFuture<List<CierreSemanalConsolidadoV2Model>> findByGerenciaAnioSemanaAsync(String gerencia,
            Integer anio,
            Integer semana) {
        return CompletableFuture.completedFuture(
                this.cierreSemanalConsolidadoV2Repository.findByGerenciaAndAnioAndSemana(gerencia, anio, semana));
    }
}

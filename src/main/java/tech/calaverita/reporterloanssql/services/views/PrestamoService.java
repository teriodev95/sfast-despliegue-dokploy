package tech.calaverita.reporterloanssql.services.views;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.mariaDB.views.PrestamoModel;
import tech.calaverita.reporterloanssql.repositories.views.PrestamoRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class PrestamoService {
    private final PrestamoRepository repo;

    public PrestamoService(PrestamoRepository repo) {
        this.repo = repo;
    }

    public ArrayList<PrestamoModel> darrprestModFindByAgenciaAnioAndSemanaToCobranzaPGS(String strAgencia_I, int intAnio_I, int intSemana_I) {
        return this.repo.darrprestEntFindByAgenciaAnioAndSemanaToCobranzaPGS(strAgencia_I, intAnio_I, intSemana_I);
    }

    @Cacheable("PrestamosPorFinalizarByAgenciaAnioAndSemana")
    public ArrayList<PrestamoModel> findPorFinalizarByAgenciaAnioAndSemana(String agencia, int anio, int semana) {
        return this.repo.findPorFinalizarByAgenteAndAnioAndSemana(agencia, anio, semana);
    }

    @Cacheable("PrestamosPorFinalizarByGerenciaAnioAndSemana")
    public ArrayList<PrestamoModel> findPorFinalizarByGerenciaAnioAndSemana(String sucursal, String gerencia, int anio, int semana) {
        return this.repo.findPorFinalizarByGerenciaAndAnioAndSemana(sucursal, gerencia, anio, semana);
    }

    public PrestamoModel prestModFindByPrestamoId(String strPrestamoId_I) {
        return this.repo.prestEntFindByPrestamoId(strPrestamoId_I);
    }

    public Optional<PrestamoModel> findById(String strPrestamoId_I) {
        return this.repo.findById(strPrestamoId_I);
    }

}

package tech.calaverita.reporterloanssql.services.views;

import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.mariaDB.views.PrestamoModel;
import tech.calaverita.reporterloanssql.repositories.views.PrestamoRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
public final class PrestamoService {
    private final PrestamoRepository repo;

    private PrestamoService(
            PrestamoRepository repo
    ) {
        this.repo = repo;
    }

    public ArrayList<PrestamoModel> darrprestModFindByAgenciaAnioAndSemanaToCobranzaPGS(String strAgencia_I, int intAnio_I, int intSemana_I) {
        return this.repo.darrprestEntFindByAgenciaAnioAndSemanaToCobranzaPGS(strAgencia_I, intAnio_I, intSemana_I);
    }

    public PrestamoModel prestModFindByPrestamoId(String strPrestamoId_I) {
        return this.repo.prestEntFindByPrestamoId(strPrestamoId_I);
    }

    public Optional<PrestamoModel> findById(String strPrestamoId_I) {
        return this.repo.findById(strPrestamoId_I);
    }
}

package tech.calaverita.reporterloanssql.services.views;

import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.mariaDB.views.PrestamoUtilModel;
import tech.calaverita.reporterloanssql.repositories.views.PrestamoUtilRepository;

import java.util.ArrayList;

@Service
public class PrestamoUtilService {
    private final PrestamoUtilRepository repo;

    public PrestamoUtilService(PrestamoUtilRepository repo) {
        this.repo = repo;
    }

    public ArrayList<PrestamoUtilModel> darrprestUtilModFindByAgenciaAnioAndSemanaToDashboard(String strAgencia_I, int intAnio_I, int intSemana_I) {
        return this.repo.darrprestUtilEntFindByAgenciaAnioAndSemanaToDashboard(strAgencia_I, intAnio_I, intSemana_I);
    }

    public ArrayList<PrestamoUtilModel> darrPrestUtilModFindByAgenciaAnioAndSemanaToCobranza(String strAgencia_I, int intAnio_I, int intSemana_I) {
        return this.repo.darrprestUtilEntFindByAgenciaAnioAndSemanaToCobranza(strAgencia_I, intAnio_I, intSemana_I);
    }

    public ArrayList<PrestamoUtilModel> darrprestUtilModByAgenciaAndFechaPagoToDashboard(String strAgencia_I, String strFechaPago_I) {
        return this.repo.darrprestUtilEntByAgenciaAndFechaPagoToDashboard(strAgencia_I, strFechaPago_I);
    }
}

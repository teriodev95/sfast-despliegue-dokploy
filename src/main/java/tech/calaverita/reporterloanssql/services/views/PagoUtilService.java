package tech.calaverita.reporterloanssql.services.views;

import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.mariaDB.views.PagoUtilModel;
import tech.calaverita.reporterloanssql.repositories.views.PagoUtilRepository;

import java.util.ArrayList;

@Service
public class PagoUtilService {
    private final PagoUtilRepository repo;

    public PagoUtilService(PagoUtilRepository repo) {
        this.repo = repo;
    }

    public ArrayList<PagoUtilModel> findByAgenciaAnioSemanaAndCierraConGreaterThan(String agencia, int anio, int semana,
                                                                                   double cierraConGreaterThan) {
        return this.repo.findByAgenteAndAnioAndSemanaAndCierraConGreaterThan(agencia, anio, semana,
                cierraConGreaterThan);
    }

    public ArrayList<PagoUtilModel> findByAgenciaAnioSemanaAndEsPrimerPago(String agencia, int anio, int semana,
                                                                           boolean esPrimerPago) {
        return this.repo.findByAgenteAndAnioAndSemanaAndEsPrimerPago(agencia, anio, semana, esPrimerPago);
    }
}

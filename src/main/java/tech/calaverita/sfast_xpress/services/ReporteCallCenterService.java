package tech.calaverita.sfast_xpress.services;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tech.calaverita.sfast_xpress.models.mariaDB.ReporteCallCenterLiteModel;
import tech.calaverita.sfast_xpress.models.mariaDB.ReporteCallCenterModel;
import tech.calaverita.sfast_xpress.repositories.ReporteCallCenterRepository;

import java.util.ArrayList;

@Service
public class ReporteCallCenterService {
    private final ReporteCallCenterRepository repo;

    public ReporteCallCenterService(ReporteCallCenterRepository repo) {
        this.repo = repo;
    }

    @Cacheable("ReporteCallCenterById")
    public ReporteCallCenterModel findById(String id) {
        return this.repo.findById(id).orElse(null);
    }

    //    @Cacheable("ReporteCallCenterById")
    public ReporteCallCenterLiteModel findLiteModelById(String id) {
        return this.repo.findLiteModelById(id);
    }

    public ArrayList<ReporteCallCenterLiteModel> findLiteModelByGerenciaAndSucursalId(String gerencia) {
        return this.repo.findLiteModelByGerenciaAndSucursalId(gerencia);
    }
}

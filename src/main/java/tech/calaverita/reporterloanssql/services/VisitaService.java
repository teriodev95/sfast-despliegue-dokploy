package tech.calaverita.reporterloanssql.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.VisitaModel;
import tech.calaverita.reporterloanssql.repositories.VisitaRepository;

import java.util.ArrayList;

@Service
public class VisitaService {
    private static VisitaRepository visitaRepository;

    @Autowired
    public VisitaService(VisitaRepository visitaRepository) {
        VisitaService.visitaRepository = visitaRepository;
    }

    public static ArrayList<VisitaModel> getVisitaModels() {

        return visitaRepository.getVisitaModels();
    }

    public static VisitaModel getVisitaModelByVisitaId(String visitaId) {

        return visitaRepository.getVisitaModelByVisitaId(visitaId);
    }

    public static ArrayList<VisitaModel> getVisitaModelsByPrestamoId(String prestamoId) {

        return visitaRepository.getVisitaModelsByPrestamoId(prestamoId);
    }

    public static void createVisitaModel(VisitaModel visitaModel) {
        visitaRepository.save(visitaModel);
    }
}

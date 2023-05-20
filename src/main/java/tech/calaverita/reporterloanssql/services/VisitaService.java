package tech.calaverita.reporterloanssql.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.VisitaModel;
import tech.calaverita.reporterloanssql.repositories.VisitaRepository;

import java.util.ArrayList;

@Service
public class VisitaService {
    private final VisitaRepository visitaRepository;

    @Autowired
    public VisitaService(VisitaRepository visitaRepository){
        this.visitaRepository = visitaRepository;
    }

    public ArrayList<VisitaModel> getVisitaModels(){

        return visitaRepository.getVisitaModels();
    }

    public VisitaModel getVisitaModelByVisitaId(String visitaId){

        return visitaRepository.getVisitaModelByVisitaId(visitaId);
    }

    public ArrayList<VisitaModel> getVisitaModelsByPrestamoId(String prestamoId){

        return visitaRepository.getVisitaModelsByPrestamoId(prestamoId);
    }

    public void createVisitaModel(VisitaModel visitaModel){
        visitaRepository.save(visitaModel);
    }
}

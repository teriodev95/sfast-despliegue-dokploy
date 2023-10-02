package tech.calaverita.reporterloanssql.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.AgenciaModel;
import tech.calaverita.reporterloanssql.repositories.AgenciaRepository;

import java.util.ArrayList;

@Service
public class AgenciaService {
    private static AgenciaRepository agenciaRepository;

    @Autowired
    public AgenciaService(AgenciaRepository agenciaRepository) {
        AgenciaService.agenciaRepository = agenciaRepository;
    }

    public static ArrayList<String> getAgenciaModelsByGerencia(String gerencia) {
        return agenciaRepository.getAgenciasByGerencia(gerencia);
    }

    public static AgenciaModel getAgenciaModelByAgenciaId(String agenciaId){
        return agenciaRepository.getAgenciaModelByAgenciaId(agenciaId);
    }
}

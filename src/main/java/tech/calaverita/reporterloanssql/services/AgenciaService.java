package tech.calaverita.reporterloanssql.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.repositories.AgenciaRepository;

import java.util.ArrayList;

@Service
public class AgenciaService {
    private static AgenciaRepository agenciaRepository;

    @Autowired
    public AgenciaService(AgenciaRepository agenciaRepository) {
        AgenciaService.agenciaRepository = agenciaRepository;
    }

    public static ArrayList<String> getAgenciasByGerencia(String gerencia) {
        return agenciaRepository.getAgenciasByGerencia(gerencia);
    }
}

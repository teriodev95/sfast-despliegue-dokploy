package tech.calaverita.reporterloanssql.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.repositories.AgenciaRepository;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class AgenciaService {
    private static AgenciaRepository agenciaRepository;

    public static ArrayList<String> getAgenciasByGerencia(String gerencia) {
        return agenciaRepository.getAgenciasByGerencia(gerencia);
    }
}

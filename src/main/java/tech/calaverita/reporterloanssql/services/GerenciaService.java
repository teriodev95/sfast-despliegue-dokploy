package tech.calaverita.reporterloanssql.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.repositories.GerenciaRepository;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class GerenciaService {
    private static GerenciaRepository gerenciaRepository;

    public static ArrayList<String> getGerenciasBySeguridad(String seguridad) {
        return gerenciaRepository.getGerenciasBySeguridad(seguridad);
    }

    public static ArrayList<String> getGerencias() {
        return gerenciaRepository.getGerencias();
    }
}

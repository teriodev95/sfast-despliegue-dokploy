package tech.calaverita.reporterloanssql.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.GerenciaModel;
import tech.calaverita.reporterloanssql.repositories.GerenciaRepository;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class GerenciaService {
    private static GerenciaRepository gerenciaRepository;

    @Autowired
    public GerenciaService(GerenciaRepository gerenciaRepository) {
        GerenciaService.gerenciaRepository = gerenciaRepository;
    }

    public static ArrayList<String> getGerenciasBySeguridad(String seguridad) {
        return gerenciaRepository.getGerenciasBySeguridad(seguridad);
    }

    public static ArrayList<String> getGerenciasByRegional(String regional) {
        return gerenciaRepository.getGerenciasByRegional(regional);
    }

    public static ArrayList<GerenciaModel> getGerenciaModels(){
        return gerenciaRepository.getGerenciaModels();
    }

    public static ArrayList<String> getGerencias() {
        return gerenciaRepository.getGerencias();
    }
}

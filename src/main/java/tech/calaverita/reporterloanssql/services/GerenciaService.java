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

    public static ArrayList<String> findManyBySeguridad(String seguridad) {
        return gerenciaRepository.findManyBySeguridad(seguridad);
    }

    public static ArrayList<String> findManyByRegional(String regional) {
        return gerenciaRepository.findManyByRegional(regional);
    }

    public static ArrayList<GerenciaModel> getGerenciaModels(){
        return gerenciaRepository.getGerenciaModels();
    }

    public static ArrayList<String> getGerencias() {
        return gerenciaRepository.getGerencias();
    }

    public static GerenciaModel findOneByGerenciaId(String gerenciaId){
        return gerenciaRepository.findOneByGerenciaId(gerenciaId);
    }

    public static ArrayList<String> getGerenciaIdsBySucursalId(String sucursalId){
        return gerenciaRepository.getGerenciaIdsBySucursalId(sucursalId);
    }
}

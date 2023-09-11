package tech.calaverita.reporterloanssql.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.AsignacionModel;
import tech.calaverita.reporterloanssql.repositories.AsignacionRepository;

import java.util.ArrayList;

@Service
public class AsignacionService {
    private static AsignacionRepository asignacionRepository;

    @Autowired
    public AsignacionService(AsignacionRepository asignacionRepository) {
        AsignacionService.asignacionRepository = asignacionRepository;
    }

    public static ArrayList<AsignacionModel> findManyByAgenciaAnioAndSemana(String agencia, int anio, int semana) {
        return asignacionRepository.findManyByAgenciaAnioAndSemana(agencia, anio, semana);
    }

    public static ArrayList<AsignacionModel> getAsignacionesByAgenciaAnioAndSemanaToDashboard(String agencia, int anio, int semana) {
        return asignacionRepository.getAsignacionesByAgenciaAnioAndSemanaToDashboard(agencia, anio, semana);
    }
}

package tech.calaverita.reporterloanssql.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.PrestamoModel;
import tech.calaverita.reporterloanssql.repositories.PrestamoRepository;

import java.util.ArrayList;

@Service
public class PrestamoService {
    private static PrestamoRepository prestamoRepository;

    @Autowired
    public PrestamoService(PrestamoRepository prestamoRepository) {
        PrestamoService.prestamoRepository = prestamoRepository;
    }

    public PrestamoModel getPrestamoModelByPrestamoId(String prestamoId) {
        return prestamoRepository.getPrestamoByPrestamoId(prestamoId);
    }

    public static ArrayList<PrestamoModel> getPrestamoModelsByAgenciaAnioAndSemanaToDashboard(String agencia, int anio, int semana) {
        return prestamoRepository.getPrestamosToDashboardByAgenciaAnioAndSemana(agencia, anio, semana);
    }

    public static ArrayList<PrestamoModel> getPrestamoModelsByAgenciaAnioAndSemanaToCobranza(String agencia, int anio, int semana) {
        return prestamoRepository.getPrestamosToCobranzaByAgenciaAnioAndSemana(agencia, anio, semana);
    }
}

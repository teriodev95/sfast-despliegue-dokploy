package tech.calaverita.reporterloanssql.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.PrestamoModel;
import tech.calaverita.reporterloanssql.models.PrestamoUtilModel;
import tech.calaverita.reporterloanssql.repositories.PrestamoRepository;

import java.util.ArrayList;

@Service
public class PrestamoService {
    private static PrestamoRepository prestamoRepository;

    @Autowired
    public PrestamoService(PrestamoRepository prestamoRepository) {
        PrestamoService.prestamoRepository = prestamoRepository;
    }

    public static ArrayList<PrestamoUtilModel> getPrestamoModelsByAgenciaAnioAndSemanaToDashboard(String agencia, int anio, int semana) {
        return prestamoRepository.getPrestamoUtilModelByAgenciaAnioAndSemanaToDashboard(agencia, anio, semana);
    }

    public static ArrayList<PrestamoUtilModel> getPrestamoUtilModelsByAgenciaAnioAndSemanaToCobranza(String agencia, int anio, int semana) {
        return prestamoRepository.getPrestamoUtilModelByAgenciaAnioAndSemanaToCobranza(agencia, anio, semana);
    }

    public static ArrayList<PrestamoModel> getPrestamoModelsByAgenciaAnioAndSemanaToCobranzaPGS(String agencia, int anio, int semana) {
        return prestamoRepository.getPrestamoModelsByAgenciaAnioAndSemanaToCobranzaPGS(agencia, anio, semana);
    }

    public static ArrayList<PrestamoUtilModel> getPrestamoModelsByAgenciaAndFechaPagoToDashboard(String agencia, String fechaPago) {
        return prestamoRepository.getPrestamoUtilModelByAgenciaAndFechaPagoToDashboard(agencia, fechaPago);
    }

    public PrestamoModel getPrestamoModelByPrestamoId(String prestamoId) {
        return prestamoRepository.getPrestamoModelByPrestamoId(prestamoId);
    }
}

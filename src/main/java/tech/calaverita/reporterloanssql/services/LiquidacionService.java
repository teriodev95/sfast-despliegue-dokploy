package tech.calaverita.reporterloanssql.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.LiquidacionModel;
import tech.calaverita.reporterloanssql.repositories.LiquidacionRepository;

import java.util.ArrayList;

@Service
public class LiquidacionService {
    private static LiquidacionRepository liquidacionRepository;

    @Autowired
    public LiquidacionService(LiquidacionRepository liquidacionRepository) {
        LiquidacionService.liquidacionRepository = liquidacionRepository;
    }

    public static ArrayList<LiquidacionModel> getLiquidacionModelsByAgenciaAnioAndSemanaToDashboard(String agencia, int anio, int semana) {
        return liquidacionRepository.getLiquidacionModelsByAgenciaAnioAndSemanaToDashboard(agencia, anio, semana);
    }

    public static ArrayList<LiquidacionModel> getLiquidacionModelsByAgenciaAndFechaPagoToDashboard(String agencia, String fechaPago) {
        return liquidacionRepository.getLiquidacionModelsByAgenciaAndFechaPagoToDashboard(agencia, fechaPago);
    }
}

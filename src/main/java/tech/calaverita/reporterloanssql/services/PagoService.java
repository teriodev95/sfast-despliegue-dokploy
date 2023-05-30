package tech.calaverita.reporterloanssql.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.PagoModel;
import tech.calaverita.reporterloanssql.repositories.PagoRepository;

import java.util.ArrayList;

@Service
public class PagoService {
    private static PagoRepository pagoRepository;

    @Autowired
    public PagoService(PagoRepository pagoRepository) {
        PagoService.pagoRepository = pagoRepository;
    }

    public static PagoModel getPagoMigracionByAgenteAnioAndSemana(String prestamoId, int anio, int semana) {
        return pagoRepository.getPagoMigracionByAgenteAnioAndSemana(prestamoId, anio, semana);
    }

    public static PagoModel getPagoByPrestamoIdAnioAndSemana(String prestamoId, int anio, int semana) {
        return pagoRepository.getPagoModelByPrestamoIdAnioAndSemana(prestamoId, anio, semana);
    }

    public static ArrayList<PagoModel> getPagoModelByAgenciaAnioAndSemana(String agencia, int anio, int semana) {
        return pagoRepository.getPagoModelsByAgenciaAnioAndSemana(agencia, anio, semana);
    }
}

package tech.calaverita.reporterloanssql.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.PagoModel;
import tech.calaverita.reporterloanssql.models.PagoVistaModel;
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

    public static ArrayList<PagoModel> findPagoModelsByAgenciaAnioAndSemana(String agencia, int anio, int semana) {
        return pagoRepository.findPagoModelsByAgenciaAnioAndSemana(agencia, anio, semana);
    }

    public static ArrayList<PagoModel> findPagoModelsByPrestamoId(String prestamoId) {
        return pagoRepository.findPagoModelsByPrestamoId(prestamoId);
    }

    public static ArrayList<PagoVistaModel> findPagoVistaModelsByAgenciaAnioAndSemana(String agencia, int anio, int semana) {
        return pagoRepository.findPagoVistaModelsByAgenciaAnioAndSemana(agencia, anio, semana);
    }

    public static ArrayList<PagoVistaModel> findPagoVistaModelsByPrestamoId(String prestamoId) {
        return pagoRepository.findPagoVistaModelsByPrestamoId(prestamoId);
    }

    public static ArrayList<PagoModel> findPagoModelsByPrestamoIdAnioAndSemana(String prestamoId, int anio, int semana) {
        return pagoRepository.findPagoModelsByPrestamoIdAnioAndSemana(prestamoId, anio, semana);
    }

    public static ArrayList<PagoVistaModel> getHistorialDePagosToPGS(String prestamoId) {
        return pagoRepository.getHistorialDePagosToPGS(prestamoId);
    }
}

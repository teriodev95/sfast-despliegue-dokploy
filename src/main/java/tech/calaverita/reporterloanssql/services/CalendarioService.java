package tech.calaverita.reporterloanssql.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.CalendarioModel;
import tech.calaverita.reporterloanssql.repositories.CalendarioRepository;

@Service
public class CalendarioService {
    private static CalendarioRepository calendarioRepository;

    @Autowired
    public CalendarioService(CalendarioRepository calendarioRepository) {
        CalendarioService.calendarioRepository = calendarioRepository;
    }

    public static CalendarioModel getSemanaActualXpressByFechaActual(String fechaActual) {
        return calendarioRepository.getSemanaActualXpressByFechaActual(fechaActual);
    }
}

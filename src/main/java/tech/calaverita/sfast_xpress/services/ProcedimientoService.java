package tech.calaverita.sfast_xpress.services;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import tech.calaverita.sfast_xpress.models.mariaDB.ResumenYBalanceModel;
import tech.calaverita.sfast_xpress.repositories.ProcedimientoRepository;

@Service
public class ProcedimientoService {
    private ProcedimientoRepository procedimientoRepository;

    public ProcedimientoService(ProcedimientoRepository procedimientoRepository) {
        this.procedimientoRepository = procedimientoRepository;
    }

    public ArrayList<ResumenYBalanceModel> findResumenYBalanceModel(Integer usuarioId, String gerencia, Integer semana,
            Integer anio) {
        return this.procedimientoRepository.findResumenYBalanceModel(usuarioId, gerencia, semana, anio);
    }
}

package tech.calaverita.sfast_xpress.services;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import tech.calaverita.sfast_xpress.models.mariaDB.GastoModel;
import tech.calaverita.sfast_xpress.repositories.GastoRepository;

@Service
public class GastoService {
    private GastoRepository gastoRepository;

    public GastoService(GastoRepository gastoRepository) {
        this.gastoRepository = gastoRepository;
    }

    public ArrayList<GastoModel> findByCreadoPorId(Integer creadoPorId) {
        return this.gastoRepository.findByCreadoPorId(creadoPorId);
    }

    public ArrayList<GastoModel> findByCreadoPorIdIn(int[] creadoPorIds) {
        return this.gastoRepository.findByCreadoPorIdIn(creadoPorIds);
    }

    public GastoModel save(GastoModel gastoModel) {
        return this.gastoRepository.save(gastoModel);
    }

    public ArrayList<GastoModel> saveAll(ArrayList<GastoModel> gastoModels) {
        ArrayList<GastoModel> responseGastoModels = new ArrayList<>();

        for (GastoModel gastoModel : this.gastoRepository.saveAll(gastoModels)) {
            responseGastoModels.add(gastoModel);
        }

        return responseGastoModels;
    }

    public ArrayList<GastoModel> findByCreadoPorIdAnioSemanaAndTipoGasto(Integer creadoPorId, int anio, int semana,
            String tipo) {
        return this.gastoRepository.findByCreadoPorIdAndAnioAndSemanaAndTipoGasto(creadoPorId, anio, semana, tipo);
    }
}

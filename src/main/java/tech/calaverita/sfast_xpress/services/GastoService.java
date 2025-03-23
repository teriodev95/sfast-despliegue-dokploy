package tech.calaverita.sfast_xpress.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
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

    @Async("asyncExecutor")
    public CompletableFuture<List<GastoModel>> findByGerenciaAnioSemanaAsync(String gerencia, int anio, int semana) {
        List<GastoModel> gastoModels = this.gastoRepository.findByGerenciaAndAnioAndSemana(gerencia, anio, semana);

        return CompletableFuture.completedFuture(gastoModels);
    }

    @Async("asyncExecutor")
    public CompletableFuture<List<GastoModel>> findByUsuarioIdAnioSemanaAsync(int usuarioId, int anio, int semana) {
        List<GastoModel> gastoModels = this.gastoRepository.findByCreadoPorIdAndAnioAndSemana(usuarioId, anio, semana);

        return CompletableFuture.completedFuture(gastoModels);
    }
}

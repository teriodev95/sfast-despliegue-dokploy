package tech.calaverita.sfast_xpress.services;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import tech.calaverita.sfast_xpress.DTOs.LiquidacionDTO;
import tech.calaverita.sfast_xpress.mappers.LiquidacionMapper;
import tech.calaverita.sfast_xpress.models.mariaDB.LiquidacionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoViewModel;
import tech.calaverita.sfast_xpress.repositories.LiquidacionRepository;

@Service
public class LiquidacionService {
    private final LiquidacionRepository repo;
    private final LiquidacionMapper mapper;

    public LiquidacionService(LiquidacionRepository repo, LiquidacionMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public LiquidacionModel save(LiquidacionModel liquidacionModel) {
        return this.repo.save(liquidacionModel);
    }

    @Async("asyncExecutor")
    public CompletableFuture<ArrayList<LiquidacionModel>> findByAgenciaAnioAndSemana(String strAgenciaI, int anio,
            int semana) {
        return CompletableFuture.completedFuture(this.repo.findByAgenciaAndAnioAndSemana(strAgenciaI, anio, semana));
    }

    public ArrayList<LiquidacionModel> findByAgenciaAndFechaPago(String agencia, String fechaPago) {
        return this.repo.findByAgenciaAndFechaPago(agencia, fechaPago);
    }

    public LiquidacionModel getLiquidacionEntity(LiquidacionDTO DTO) {
        return this.mapper.mapIn(DTO);
    }

    public LiquidacionDTO getLiquidacionDTO(PrestamoViewModel prestamoViewModel) {
        return this.mapper.mapOut(prestamoViewModel);
    }
}

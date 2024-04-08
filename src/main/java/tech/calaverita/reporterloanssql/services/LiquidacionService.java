package tech.calaverita.reporterloanssql.services;

import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.dto.LiquidacionDTO;
import tech.calaverita.reporterloanssql.mappers.LiquidacionMapper;
import tech.calaverita.reporterloanssql.models.mariaDB.LiquidacionModel;
import tech.calaverita.reporterloanssql.models.mariaDB.views.PrestamoModel;
import tech.calaverita.reporterloanssql.repositories.LiquidacionRepository;

import java.util.ArrayList;

@Service
public final class LiquidacionService {
    private final LiquidacionRepository repo;
    private final LiquidacionMapper mapper;

    private LiquidacionService(LiquidacionRepository repo, LiquidacionMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public LiquidacionModel save(LiquidacionModel liquidacionModel) {
        return this.repo.save(liquidacionModel);
    }

    public ArrayList<LiquidacionModel> findByAgenciaAnioAndSemana(String strAgenciaI, int anio, int semana) {
        return this.repo.findByAgenciaAndAnioAndSemana(strAgenciaI, anio, semana);
    }

    public ArrayList<LiquidacionModel> findByAgenciaAndFechaPago(String agencia, String fechaPago) {
        return this.repo.findByAgenciaAndFechaPago(agencia, fechaPago);
    }

    public LiquidacionModel getLiquidacionEntity(LiquidacionDTO DTO) {
        return this.mapper.mapIn(DTO);
    }

    public LiquidacionDTO getLiquidacionDTO(PrestamoModel prestamoModel) {
        return this.mapper.mapOut(prestamoModel);
    }
}

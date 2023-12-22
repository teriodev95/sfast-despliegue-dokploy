package tech.calaverita.reporterloanssql.services;

import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.persistence.entities.PorcentajesDescuentoLiquidacionesEntity;
import tech.calaverita.reporterloanssql.persistence.repositories.PorcentajesDescuentoLiquidacionesRepository;

import java.util.Optional;

@Service
public class PorcentajesDescuentoLiquidacionesService {
    private final PorcentajesDescuentoLiquidacionesRepository repo;

    public PorcentajesDescuentoLiquidacionesService(
            PorcentajesDescuentoLiquidacionesRepository repo
    ) {
        this.repo = repo;
    }

    public Optional<PorcentajesDescuentoLiquidacionesEntity> findById(
            String id
    ) {
        return this.repo.findById(id);
    }
}

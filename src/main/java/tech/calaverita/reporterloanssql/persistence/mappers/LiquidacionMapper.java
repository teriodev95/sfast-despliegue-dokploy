package tech.calaverita.reporterloanssql.persistence.mappers;

import tech.calaverita.reporterloanssql.persistence.dto.LiquidacionDTO;
import tech.calaverita.reporterloanssql.persistence.entities.LiquidacionEntity;

public class LiquidacionMapper implements IMapper<LiquidacionEntity, LiquidacionDTO> {

    @Override
    public LiquidacionDTO mapOut(LiquidacionEntity out) {
        return null;
    }

    @Override
    public LiquidacionEntity mapIn(LiquidacionDTO in) {
        LiquidacionEntity entity = new LiquidacionEntity();
        {
            entity.setPrestamoId(in.getPrestamoId());
            entity.setDescuentoEnDinero(in.getDescuentoDinero());
            entity.setDescuentoEnPorcentaje(in.getDescuentoPorcentaje());
            entity.setLiquidoCon(in.getLiquidaCon());
            entity.setSemTranscurridas(in.getSemanasTranscurridas());
        }
        return entity;
    }
}

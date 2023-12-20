package tech.calaverita.reporterloanssql.persistence.mappers.cierre_semanal;

import org.springframework.stereotype.Component;
import tech.calaverita.reporterloanssql.persistence.dto.cierre_semanal.IngresosAgenteDTO;
import tech.calaverita.reporterloanssql.persistence.entities.cierre_semanal.IngresosAgenteEntity;
import tech.calaverita.reporterloanssql.persistence.mappers.IMapper;

@Component
public final class IngresosAgenteMapper implements IMapper<IngresosAgenteEntity, IngresosAgenteDTO> {
    @Override
    public IngresosAgenteDTO mapOut(IngresosAgenteEntity out) {
        IngresosAgenteDTO DTO = new IngresosAgenteDTO();
        {
            DTO.setCobranzaPura(out.getCobranzaPura());
            DTO.setMontoExcedente(out.getMontoExcedente());
            DTO.setLiquidaciones(out.getLiquidaciones());
            DTO.setMultas(out.getMultas());
            DTO.setOtros(out.getOtros());
            DTO.setTotal(out.getTotal());
        }
        return DTO;
    }

    @Override
    public IngresosAgenteEntity mapIn(IngresosAgenteDTO in) {
        IngresosAgenteEntity entity = new IngresosAgenteEntity();
        {
            entity.setCobranzaPura(in.getCobranzaPura());
            entity.setMontoExcedente(in.getMontoExcedente());
            entity.setLiquidaciones(in.getLiquidaciones());
            entity.setMultas(in.getMultas());
            entity.setOtros(in.getOtros());
            entity.setTotal(in.getTotal());
            entity.setMotivoOtros(in.getMotivoOtros());
        }
        return entity;
    }
}

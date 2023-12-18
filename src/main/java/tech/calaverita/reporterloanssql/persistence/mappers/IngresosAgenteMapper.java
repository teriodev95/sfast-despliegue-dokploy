package tech.calaverita.reporterloanssql.persistence.mappers;

import org.springframework.stereotype.Component;
import tech.calaverita.reporterloanssql.persistence.dto.cierre_semanal.IngresosAgenteDTO;
import tech.calaverita.reporterloanssql.persistence.entities.cierre_semanal.IngresosAgenteEntity;

@Component
public final class IngresosAgenteMapper implements IMapper<IngresosAgenteEntity, IngresosAgenteDTO> {
    @Override
    public IngresosAgenteDTO mapOut(IngresosAgenteEntity out) {
        IngresosAgenteDTO ingresosAgenteDTO = new IngresosAgenteDTO();
        {
            ingresosAgenteDTO.setCobranzaPura(out.getCobranzaPura());
            ingresosAgenteDTO.setMontoExcedente(out.getMontoExcedente());
            ingresosAgenteDTO.setLiquidaciones(out.getLiquidaciones());
            ingresosAgenteDTO.setMultas(out.getMultas());
            ingresosAgenteDTO.setOtros(out.getOtros());
            ingresosAgenteDTO.setTotal(out.getTotal());
        }
        return ingresosAgenteDTO;
    }

    @Override
    public IngresosAgenteEntity mapIn(IngresosAgenteDTO in) {
        IngresosAgenteEntity ingresosAgenteEntity = new IngresosAgenteEntity();
        {
            ingresosAgenteEntity.setCobranzaPura(in.getCobranzaPura());
            ingresosAgenteEntity.setMontoExcedente(in.getMontoExcedente());
            ingresosAgenteEntity.setLiquidaciones(in.getLiquidaciones());
            ingresosAgenteEntity.setMultas(in.getMultas());
            ingresosAgenteEntity.setOtros(in.getOtros());
            ingresosAgenteEntity.setTotal(in.getTotal());
        }
        return ingresosAgenteEntity;
    }
}

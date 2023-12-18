package tech.calaverita.reporterloanssql.persistence.mappers.cierre_semanal;

import org.springframework.stereotype.Component;
import tech.calaverita.reporterloanssql.persistence.dto.cierre_semanal.EgresosAgenteDTO;
import tech.calaverita.reporterloanssql.persistence.entities.cierre_semanal.EgresosAgenteEntity;
import tech.calaverita.reporterloanssql.persistence.mappers.IMapper;

@Component
public final class EgresosAgenteMapper implements IMapper<EgresosAgenteEntity, EgresosAgenteDTO> {
    @Override
    public EgresosAgenteDTO mapOut(EgresosAgenteEntity out) {
        EgresosAgenteDTO egresosAgenteDTO = new EgresosAgenteDTO();
        {
            egresosAgenteDTO.setAsignaciones(out.getAsignaciones());
            egresosAgenteDTO.setOtros(out.getOtros());
            egresosAgenteDTO.setEfectivoEntregadoCierre(out.getEfectivoEntregadoCierre());
            egresosAgenteDTO.setTotal(out.getTotal());
        }
        return egresosAgenteDTO;
    }

    @Override
    public EgresosAgenteEntity mapIn(EgresosAgenteDTO in) {
        EgresosAgenteEntity egresosAgenteEntity = new EgresosAgenteEntity();
        {
            egresosAgenteEntity.setAsignaciones(egresosAgenteEntity.getAsignaciones());
            egresosAgenteEntity.setOtros(egresosAgenteEntity.getOtros());
            egresosAgenteEntity.setEfectivoEntregadoCierre(egresosAgenteEntity.getEfectivoEntregadoCierre());
            egresosAgenteEntity.setTotal(egresosAgenteEntity.getTotal());
        }
        return egresosAgenteEntity;
    }
}

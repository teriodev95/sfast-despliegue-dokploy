package tech.calaverita.reporterloanssql.persistence.mappers.cierre_semanal;

import org.springframework.stereotype.Component;
import tech.calaverita.reporterloanssql.persistence.dto.cierre_semanal.EgresosAgenteDTO;
import tech.calaverita.reporterloanssql.persistence.entities.cierre_semanal.EgresosAgenteEntity;
import tech.calaverita.reporterloanssql.persistence.mappers.IMapper;

@Component
public final class EgresosAgenteMapper implements IMapper<EgresosAgenteEntity, EgresosAgenteDTO> {
    @Override
    public EgresosAgenteDTO mapOut(EgresosAgenteEntity out) {
        EgresosAgenteDTO DTO = new EgresosAgenteDTO();
        {
            DTO.setAsignaciones(out.getAsignaciones());
            DTO.setOtros(out.getOtros());
            DTO.setEfectivoEntregadoCierre(out.getEfectivoEntregadoCierre());
            DTO.setTotal(out.getTotal());
        }
        return DTO;
    }

    @Override
    public EgresosAgenteEntity mapIn(EgresosAgenteDTO in) {
        EgresosAgenteEntity entity = new EgresosAgenteEntity();
        {
            entity.setAsignaciones(in.getAsignaciones());
            entity.setOtros(in.getOtros());
            entity.setEfectivoEntregadoCierre(in.getEfectivoEntregadoCierre());
            entity.setTotal(in.getTotal());
            entity.setMotivoOtros(in.getMotivoOtros());
        }
        return entity;
    }
}

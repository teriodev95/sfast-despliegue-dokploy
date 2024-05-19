package tech.calaverita.reporterloanssql.mappers.cierre_semanal;

import org.springframework.stereotype.Component;
import tech.calaverita.reporterloanssql.dto.cierre_semanal.EgresosAgenteDTO;
import tech.calaverita.reporterloanssql.mappers.IMapper;
import tech.calaverita.reporterloanssql.models.mariaDB.cierre_semanal.EgresosAgenteModel;

import java.util.ArrayList;

@Component
public final class EgresosAgenteMapper implements IMapper<EgresosAgenteModel, EgresosAgenteDTO> {
    @Override
    public EgresosAgenteDTO mapOut(EgresosAgenteModel out) {
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
    public EgresosAgenteModel mapIn(EgresosAgenteDTO in) {
        EgresosAgenteModel entity = new EgresosAgenteModel();
        {
            entity.setAsignaciones(in.getAsignaciones());
            entity.setOtros(in.getOtros());
            entity.setEfectivoEntregadoCierre(in.getEfectivoEntregadoCierre());
            entity.setTotal(in.getTotal());
            entity.setMotivoOtros(in.getMotivoOtros());
        }
        return entity;
    }

    @Override
    public ArrayList<EgresosAgenteDTO> mapOuts(ArrayList<EgresosAgenteModel> outs) {
        return null;
    }

    @Override
    public ArrayList<EgresosAgenteModel> mapIns(ArrayList<EgresosAgenteDTO> ins) {
        return null;
    }
}

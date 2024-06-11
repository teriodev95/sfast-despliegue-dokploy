package tech.calaverita.sfast_xpress.mappers.cierre_semanal;

import org.springframework.stereotype.Component;
import tech.calaverita.sfast_xpress.dto.cierre_semanal.IngresosAgenteDTO;
import tech.calaverita.sfast_xpress.mappers.IMapper;
import tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal.IngresosAgenteModel;

import java.util.ArrayList;

@Component
public final class IngresosAgenteMapper implements IMapper<IngresosAgenteModel, IngresosAgenteDTO> {
    @Override
    public IngresosAgenteDTO mapOut(IngresosAgenteModel out) {
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
    public IngresosAgenteModel mapIn(IngresosAgenteDTO in) {
        IngresosAgenteModel entity = new IngresosAgenteModel();
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

    @Override
    public ArrayList<IngresosAgenteDTO> mapOuts(ArrayList<IngresosAgenteModel> outs) {
        return null;
    }

    @Override
    public ArrayList<IngresosAgenteModel> mapIns(ArrayList<IngresosAgenteDTO> ins) {
        return null;
    }
}

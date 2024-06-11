package tech.calaverita.sfast_xpress.mappers.cierre_semanal;

import org.springframework.stereotype.Component;
import tech.calaverita.sfast_xpress.dto.cierre_semanal.EgresosGerenteDTO;
import tech.calaverita.sfast_xpress.mappers.IMapper;
import tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal.EgresosGerenteModel;

import java.util.ArrayList;

@Component
public final class EgresosGerenteMapper implements IMapper<EgresosGerenteModel, EgresosGerenteDTO> {
    @Override
    public EgresosGerenteDTO mapOut(EgresosGerenteModel out) {
        EgresosGerenteDTO egresosGerenteDTO = new EgresosGerenteDTO();
        {
            egresosGerenteDTO.setPorcentajeComisionCobranza(out.getPorcentajeComisionCobranza());
            egresosGerenteDTO.setPorcentajeBonoMensual(out.getPorcentajeBonoMensual());
            egresosGerenteDTO.setPagoComisionCobranza(out.getPagoComisionCobranza());
            egresosGerenteDTO.setPagoComisionVentas(out.getPagoComisionVentas());
            egresosGerenteDTO.setBonos(out.getBonos());
            egresosGerenteDTO.setEfectivoRestanteCierre(out.getEfectivoRestanteCierre());
        }
        return egresosGerenteDTO;
    }

    @Override
    public EgresosGerenteModel mapIn(EgresosGerenteDTO in) {
        EgresosGerenteModel egresosGerenteModel = new EgresosGerenteModel();
        {
            egresosGerenteModel.setPorcentajeComisionCobranza(in.getPorcentajeComisionCobranza());
            egresosGerenteModel.setPorcentajeBonoMensual(in.getPorcentajeBonoMensual());
            egresosGerenteModel.setPagoComisionCobranza(in.getPagoComisionCobranza());
            egresosGerenteModel.setPagoComisionVentas(in.getPagoComisionVentas());
            egresosGerenteModel.setBonos(in.getBonos());
            egresosGerenteModel.setEfectivoRestanteCierre(in.getEfectivoRestanteCierre());
        }
        return egresosGerenteModel;
    }

    @Override
    public ArrayList<EgresosGerenteDTO> mapOuts(ArrayList<EgresosGerenteModel> outs) {
        return null;
    }

    @Override
    public ArrayList<EgresosGerenteModel> mapIns(ArrayList<EgresosGerenteDTO> ins) {
        return null;
    }
}

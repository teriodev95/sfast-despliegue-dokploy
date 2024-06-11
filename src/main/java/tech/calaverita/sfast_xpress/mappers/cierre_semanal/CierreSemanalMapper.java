package tech.calaverita.sfast_xpress.mappers.cierre_semanal;

import org.springframework.stereotype.Component;
import tech.calaverita.sfast_xpress.dto.cierre_semanal.CierreSemanalDTO;
import tech.calaverita.sfast_xpress.mappers.IMapper;
import tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal.CierreSemanalModel;

import java.util.ArrayList;

@Component
public final class CierreSemanalMapper implements IMapper<CierreSemanalModel, CierreSemanalDTO> {
    @Override
    public CierreSemanalDTO mapOut(CierreSemanalModel out) {
        CierreSemanalDTO cierreSemanalDTO = new CierreSemanalDTO();
        {
            cierreSemanalDTO.setSemana(out.getSemana());
            cierreSemanalDTO.setAnio(out.getAnio());
            cierreSemanalDTO.setDia(out.getDia());
            cierreSemanalDTO.setMes(out.getMes());
            cierreSemanalDTO.setPDF(out.getPDF());
            cierreSemanalDTO.setSelfieAgente(out.getSelfieAgente());
            cierreSemanalDTO.setSelfieGerente(out.getSelfieGerente());
        }
        return cierreSemanalDTO;
    }

    @Override
    public CierreSemanalModel mapIn(CierreSemanalDTO in) {
        CierreSemanalModel cierreSemanalModel = new CierreSemanalModel();
        {
            String strGerencia;
            String strAgencia;
            int intAnio;
            int intSemana;
            {
                strGerencia = in.getBalanceAgencia().getZona();
                strAgencia = in.getBalanceAgencia().getAgencia();
                intAnio = in.getAnio();
                intSemana = in.getSemana();
            }
            cierreSemanalModel.setId(strGerencia + '-' + strAgencia + '-' + intAnio + '-' + intSemana);
            cierreSemanalModel.setBalanceAgenciaId(cierreSemanalModel.getId());
            cierreSemanalModel.setEgresosAgenteId(cierreSemanalModel.getId());
            cierreSemanalModel.setEgresosGerenteId(cierreSemanalModel.getId());
            cierreSemanalModel.setIngresosAgenteId(cierreSemanalModel.getId());

            cierreSemanalModel.setSemana(in.getSemana());
            cierreSemanalModel.setAnio(in.getAnio());
            cierreSemanalModel.setDia(in.getDia());
            cierreSemanalModel.setMes(in.getMes());
            cierreSemanalModel.setSelfieAgente(in.getSelfieAgente());
            cierreSemanalModel.setSelfieGerente(in.getSelfieGerente());
            cierreSemanalModel.setStatusAgencia(in.getStatusAgencia());
        }
        return cierreSemanalModel;
    }

    @Override
    public ArrayList<CierreSemanalDTO> mapOuts(ArrayList<CierreSemanalModel> outs) {
        return null;
    }

    @Override
    public ArrayList<CierreSemanalModel> mapIns(ArrayList<CierreSemanalDTO> ins) {
        return null;
    }
}

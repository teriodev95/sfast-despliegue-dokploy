package tech.calaverita.reporterloanssql.persistence.mappers.cierre_semanal;

import org.springframework.stereotype.Component;
import tech.calaverita.reporterloanssql.persistence.dto.cierre_semanal.CierreSemanalDTO;
import tech.calaverita.reporterloanssql.persistence.entities.cierre_semanal.CierreSemanalEntity;
import tech.calaverita.reporterloanssql.persistence.mappers.IMapper;

@Component
public final class CierreSemanalMapper implements IMapper<CierreSemanalEntity, CierreSemanalDTO> {
    @Override
    public CierreSemanalDTO mapOut(CierreSemanalEntity out) {
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
    public CierreSemanalEntity mapIn(CierreSemanalDTO in) {
        CierreSemanalEntity cierreSemanalEntity = new CierreSemanalEntity();
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
            cierreSemanalEntity.setId(strGerencia + '-' + strAgencia + '-' + intAnio + '-' + intSemana);
            cierreSemanalEntity.setBalanceAgenciaId(cierreSemanalEntity.getId());
            cierreSemanalEntity.setEgresosAgenteId(cierreSemanalEntity.getId());
            cierreSemanalEntity.setEgresosGerenteId(cierreSemanalEntity.getId());
            cierreSemanalEntity.setIngresosAgenteId(cierreSemanalEntity.getId());

            cierreSemanalEntity.setSemana(in.getSemana());
            cierreSemanalEntity.setAnio(in.getAnio());
            cierreSemanalEntity.setDia(in.getDia());
            cierreSemanalEntity.setMes(in.getMes());
            cierreSemanalEntity.setSelfieAgente(in.getSelfieAgente());
            cierreSemanalEntity.setSelfieGerente(in.getSelfieGerente());
        }
        return cierreSemanalEntity;
    }
}

package tech.calaverita.reporterloanssql.persistence.mappers;

import org.springframework.stereotype.Component;
import tech.calaverita.reporterloanssql.persistence.dto.cierre_semanal.EgresosGerenteDTO;
import tech.calaverita.reporterloanssql.persistence.entities.cierre_semanal.EgresosGerenteEntity;

@Component
public final class EgresosGerenteMapper implements IMapper<EgresosGerenteEntity, EgresosGerenteDTO> {
    @Override
    public EgresosGerenteDTO mapOut(EgresosGerenteEntity out) {
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
    public EgresosGerenteEntity mapIn(EgresosGerenteDTO in) {
        EgresosGerenteEntity egresosGerenteEntity = new EgresosGerenteEntity();
        {
            egresosGerenteEntity.setPorcentajeComisionCobranza(in.getPorcentajeComisionCobranza());
            egresosGerenteEntity.setPorcentajeBonoMensual(in.getPorcentajeBonoMensual());
            egresosGerenteEntity.setPagoComisionCobranza(in.getPagoComisionCobranza());
            egresosGerenteEntity.setPagoComisionVentas(in.getPagoComisionVentas());
            egresosGerenteEntity.setBonos(in.getBonos());
            egresosGerenteEntity.setEfectivoRestanteCierre(in.getEfectivoRestanteCierre());
        }

        return egresosGerenteEntity;
    }
}

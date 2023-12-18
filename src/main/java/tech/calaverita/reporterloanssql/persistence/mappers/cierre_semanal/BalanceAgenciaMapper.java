package tech.calaverita.reporterloanssql.persistence.mappers.cierre_semanal;

import org.springframework.stereotype.Component;
import tech.calaverita.reporterloanssql.persistence.dto.cierre_semanal.BalanceAgenciaDTO;
import tech.calaverita.reporterloanssql.persistence.entities.cierre_semanal.BalanceAgenciaEntity;
import tech.calaverita.reporterloanssql.persistence.mappers.IMapper;

@Component
public final class BalanceAgenciaMapper implements IMapper<BalanceAgenciaEntity, BalanceAgenciaDTO> {
    @Override
    public BalanceAgenciaDTO mapOut(BalanceAgenciaEntity out) {
        BalanceAgenciaDTO balanceAgenciaDTO = new BalanceAgenciaDTO();
        {
            balanceAgenciaDTO.setZona(out.getZona());
            balanceAgenciaDTO.setGerente(out.getGerente());
            balanceAgenciaDTO.setAgencia(out.getAgencia());
            balanceAgenciaDTO.setAgente(out.getAgente());
            balanceAgenciaDTO.setRendimiento(out.getRendimiento());
            balanceAgenciaDTO.setNivel(out.getNivel());
            balanceAgenciaDTO.setClientes(out.getClientes());
            balanceAgenciaDTO.setPagosReducidos(out.getPagosReducidos());
            balanceAgenciaDTO.setNoPagos(out.getNoPagos());
            balanceAgenciaDTO.setLiquidaciones(out.getLiquidaciones());
        }
        return balanceAgenciaDTO;
    }

    @Override
    public BalanceAgenciaEntity mapIn(BalanceAgenciaDTO in) {
        BalanceAgenciaEntity balanceAgenciaEntity = new BalanceAgenciaEntity();
        {
            balanceAgenciaEntity.setZona(in.getZona());
            balanceAgenciaEntity.setGerente(in.getGerente());
            balanceAgenciaEntity.setAgencia(in.getAgencia());
            balanceAgenciaEntity.setAgente(in.getAgente());
            balanceAgenciaEntity.setRendimiento(in.getRendimiento());
            balanceAgenciaEntity.setNivel(in.getNivel());
            balanceAgenciaEntity.setClientes(in.getClientes());
            balanceAgenciaEntity.setPagosReducidos(in.getPagosReducidos());
            balanceAgenciaEntity.setNoPagos(in.getNoPagos());
            balanceAgenciaEntity.setLiquidaciones(in.getLiquidaciones());
        }
        return balanceAgenciaEntity;
    }
}

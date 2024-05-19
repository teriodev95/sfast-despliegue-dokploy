package tech.calaverita.reporterloanssql.mappers.cierre_semanal;

import org.springframework.stereotype.Component;
import tech.calaverita.reporterloanssql.dto.cierre_semanal.BalanceAgenciaDTO;
import tech.calaverita.reporterloanssql.mappers.IMapper;
import tech.calaverita.reporterloanssql.models.mariaDB.cierre_semanal.BalanceAgenciaModel;

import java.util.ArrayList;

@Component
public final class BalanceAgenciaMapper implements IMapper<BalanceAgenciaModel, BalanceAgenciaDTO> {
    @Override
    public BalanceAgenciaDTO mapOut(BalanceAgenciaModel out) {
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
    public BalanceAgenciaModel mapIn(BalanceAgenciaDTO in) {
        BalanceAgenciaModel balanceAgenciaModel = new BalanceAgenciaModel();
        {
            balanceAgenciaModel.setZona(in.getZona());
            balanceAgenciaModel.setGerente(in.getGerente());
            balanceAgenciaModel.setAgencia(in.getAgencia());
            balanceAgenciaModel.setAgente(in.getAgente());
            balanceAgenciaModel.setRendimiento(in.getRendimiento());
            balanceAgenciaModel.setNivel(in.getNivel());
            balanceAgenciaModel.setClientes(in.getClientes());
            balanceAgenciaModel.setPagosReducidos(in.getPagosReducidos());
            balanceAgenciaModel.setNoPagos(in.getNoPagos());
            balanceAgenciaModel.setLiquidaciones(in.getLiquidaciones());
        }
        return balanceAgenciaModel;
    }

    @Override
    public ArrayList<BalanceAgenciaDTO> mapOuts(ArrayList<BalanceAgenciaModel> outs) {
        return null;
    }

    @Override
    public ArrayList<BalanceAgenciaModel> mapIns(ArrayList<BalanceAgenciaDTO> ins) {
        return null;
    }
}

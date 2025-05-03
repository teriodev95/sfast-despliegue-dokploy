package tech.calaverita.sfast_xpress.mappers.cierre_semanal;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import tech.calaverita.sfast_xpress.DTOs.CierreSemanalConsolidadoV2DTO;
import tech.calaverita.sfast_xpress.DTOs.cierre_semanal.BalanceAgenciaDTO;
import tech.calaverita.sfast_xpress.DTOs.cierre_semanal.CierreSemanalDTO;
import tech.calaverita.sfast_xpress.DTOs.cierre_semanal.ComisionesAPagarEnSemanaDTO;
import tech.calaverita.sfast_xpress.DTOs.cierre_semanal.EgresosAgenteDTO;
import tech.calaverita.sfast_xpress.DTOs.cierre_semanal.EgresosGerenteDTO;
import tech.calaverita.sfast_xpress.DTOs.cierre_semanal.IngresosAgenteDTO;
import tech.calaverita.sfast_xpress.mappers.IMapper;
import tech.calaverita.sfast_xpress.models.mariaDB.CierreSemanalConsolidadoV2Model;
import tech.calaverita.sfast_xpress.models.mariaDB.ComisionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal.CierreSemanalModel;

@Component
public final class CierreSemanalMapper implements IMapper<CierreSemanalModel, CierreSemanalDTO> {
    @Override
    public CierreSemanalDTO mapOut(CierreSemanalModel out) {
        CierreSemanalDTO cierreSemanalDTO = new CierreSemanalDTO();
        {
            cierreSemanalDTO.setSemana(out.getSemana());
            cierreSemanalDTO.setAnio(out.getAnio());
            cierreSemanalDTO.setBalanceAgencia(new BalanceAgenciaDTO(out));
            cierreSemanalDTO.setIngresosAgente(new IngresosAgenteDTO(out));
            cierreSemanalDTO.setEgresosAgente(new EgresosAgenteDTO(out));
            cierreSemanalDTO.setEgresosGerente(new EgresosGerenteDTO(out));
            cierreSemanalDTO.setUidVerificacionAgente(out.getUidVerificacionAgente());
            cierreSemanalDTO.setUidVerificacionGerente(out.getUidVerificacionGerente());
        }
        return cierreSemanalDTO;
    }

    public CierreSemanalConsolidadoV2DTO mapCierreSemanalConsolidadoDTOOut(CierreSemanalConsolidadoV2Model out,
            ComisionModel comisionModel) {
        CierreSemanalConsolidadoV2DTO cierreSemanalConsolidadoV2DTO = new CierreSemanalConsolidadoV2DTO();
        {
            cierreSemanalConsolidadoV2DTO.setId(out.getId());
            cierreSemanalConsolidadoV2DTO.setSemana(out.getSemana());
            cierreSemanalConsolidadoV2DTO.setAnio(out.getAnio());
            cierreSemanalConsolidadoV2DTO.setBalanceAgenciaDTO(new BalanceAgenciaDTO(out, comisionModel));
            cierreSemanalConsolidadoV2DTO.setIngresosAgenteDTO(new IngresosAgenteDTO(out, comisionModel));
            cierreSemanalConsolidadoV2DTO.setEgresosAgenteDTO(new EgresosAgenteDTO(out));
            cierreSemanalConsolidadoV2DTO
                    .setComisionesAPagarEnSemanaDTO(new ComisionesAPagarEnSemanaDTO(out));
            cierreSemanalConsolidadoV2DTO.setUidVerificacionAgente(out.getUidVerificacionAgente());
            cierreSemanalConsolidadoV2DTO.setUidVerificacionGerente(out.getUidVerificacionGerente());
        }
        return cierreSemanalConsolidadoV2DTO;
    }

    @Override
    public CierreSemanalModel mapIn(CierreSemanalDTO in) {
        CierreSemanalModel cierreSemanalModel = new CierreSemanalModel();
        {
            cierreSemanalModel.setSemana(in.getSemana());
            cierreSemanalModel.setAnio(in.getAnio());
            cierreSemanalModel.setGerencia(in.getBalanceAgencia().getZona());
            cierreSemanalModel.setAgencia(in.getBalanceAgencia().getAgencia());
            cierreSemanalModel.setRendimiento(in.getBalanceAgencia().getRendimiento());
            cierreSemanalModel.setNivel(in.getBalanceAgencia().getNivel());
            cierreSemanalModel.setClientes(in.getBalanceAgencia().getClientes());
            cierreSemanalModel.setPagosReducidos(in.getBalanceAgencia().getPagosReducidos());
            cierreSemanalModel.setNoPagos(in.getBalanceAgencia().getNoPagos());
            cierreSemanalModel.setClientesLiquidados(in.getBalanceAgencia().getLiquidaciones());
            cierreSemanalModel.setCobranzaPura(in.getIngresosAgente().getCobranzaPura());
            cierreSemanalModel.setMontoExcedente(in.getIngresosAgente().getMontoExcedente());
            cierreSemanalModel.setLiquidaciones(in.getIngresosAgente().getLiquidaciones());
            cierreSemanalModel.setMultas(in.getIngresosAgente().getMultas());
            cierreSemanalModel.setOtrosIngresos(in.getIngresosAgente().getOtros());
            cierreSemanalModel.setMotivoOtrosIngresos(in.getIngresosAgente().getMotivoOtros());
            cierreSemanalModel.setAsignaciones(in.getEgresosAgente().getAsignaciones());
            cierreSemanalModel.setOtrosEgresos(in.getEgresosAgente().getOtros());
            cierreSemanalModel.setMotivoOtrosEgresos(in.getEgresosAgente().getMotivoOtros());
            cierreSemanalModel.setEfectivoEntregadoCierre(in.getEgresosAgente().getEfectivoEntregadoCierre());
            cierreSemanalModel.setTotalEgresosAgente(in.getEgresosAgente().getTotal());
            cierreSemanalModel.setPorcentajeComisionCobranza(in.getEgresosGerente().getPorcentajeComisionCobranza());
            cierreSemanalModel.setPorcentajeBonoMensual(in.getEgresosGerente().getPorcentajeBonoMensual());
            cierreSemanalModel.setPagoComisionCobranza(in.getEgresosGerente().getPagoComisionCobranza());
            cierreSemanalModel.setPagoComisionVentas(in.getEgresosGerente().getPagoComisionVentas());
            cierreSemanalModel.setBonos(in.getEgresosGerente().getBonos());
            cierreSemanalModel.setEfectivoRestanteCierre(in.getEgresosGerente().getEfectivoRestanteCierre());
            cierreSemanalModel.setUidVerificacionAgente(in.getUidVerificacionAgente());
            cierreSemanalModel.setUidVerificacionGerente(in.getUidVerificacionGerente());
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

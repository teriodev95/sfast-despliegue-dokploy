package tech.calaverita.sfast_xpress.mappers;

import org.springframework.stereotype.Component;
import tech.calaverita.sfast_xpress.DTOs.LiquidacionDTO;
import tech.calaverita.sfast_xpress.models.mariaDB.LiquidacionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoViewModel;

import java.util.ArrayList;

@Component
public class LiquidacionMapper implements IMapper<LiquidacionModel, LiquidacionDTO> {

    @Override
    public LiquidacionDTO mapOut(LiquidacionModel out) {
        return null;
    }

    @Override
    public LiquidacionModel mapIn(LiquidacionDTO in) {
        LiquidacionModel liquidacionModel = new LiquidacionModel();
        liquidacionModel.setPrestamoId(in.getPrestamoId());
        liquidacionModel.setDescuentoEnDinero(in.getDescuentoDinero());
        liquidacionModel.setDescuentoEnPorcentaje(in.getDescuentoPorcentaje());
        liquidacionModel.setLiquidoCon(in.getLiquidaCon());
        liquidacionModel.setSemTranscurridas(in.getSemanasTranscurridas());
        return liquidacionModel;
    }

    @Override
    public ArrayList<LiquidacionDTO> mapOuts(ArrayList<LiquidacionModel> outs) {
        return null;
    }

    @Override
    public ArrayList<LiquidacionModel> mapIns(ArrayList<LiquidacionDTO> ins) {
        return null;
    }

    public LiquidacionDTO mapOut(PrestamoViewModel prestamoViewModel) {
        LiquidacionDTO DTO = new LiquidacionDTO();
        // To easy code
        String cliente = prestamoViewModel.getNombres() + " " + prestamoViewModel.getApellidoPaterno() + " "
                + prestamoViewModel.getApellidoMaterno();
        String semanaEntrega = "#" + prestamoViewModel.getSemana() + "-" + prestamoViewModel.getAnio();

        DTO.setCliente(cliente);
        DTO.setIdentificador(prestamoViewModel.getIdentificadorCredito());
        DTO.setSemEntrega(semanaEntrega);
        DTO.setEntregado(prestamoViewModel.getMontoOtorgado());
        DTO.setCargo(prestamoViewModel.getCargo());
        DTO.setMontoTotal(prestamoViewModel.getTotalAPagar());
        DTO.setCobrado(prestamoViewModel.getCobrado());
        DTO.setSaldo(prestamoViewModel.getSaldo());
        DTO.setPrestamoId(prestamoViewModel.getPrestamoId());

        return DTO;
    }
}

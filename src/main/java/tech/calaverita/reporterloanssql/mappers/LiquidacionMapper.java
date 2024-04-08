package tech.calaverita.reporterloanssql.mappers;

import org.springframework.stereotype.Component;
import tech.calaverita.reporterloanssql.dto.LiquidacionDTO;
import tech.calaverita.reporterloanssql.models.mariaDB.LiquidacionModel;
import tech.calaverita.reporterloanssql.models.mariaDB.views.PrestamoModel;

@Component
public class LiquidacionMapper implements IMapper<LiquidacionModel, LiquidacionDTO> {

    @Override
    public LiquidacionDTO mapOut(
            LiquidacionModel out
    ) {
        return null;
    }

    @Override
    public LiquidacionModel mapIn(
            LiquidacionDTO in
    ) {
        LiquidacionModel entity = new LiquidacionModel();
        {
            entity.setPrestamoId(in.getPrestamoId());
            entity.setDescuentoEnDinero(in.getDescuentoDinero());
            entity.setDescuentoEnPorcentaje(in.getDescuentoPorcentaje());
            entity.setLiquidoCon(in.getLiquidaCon());
            entity.setSemTranscurridas(in.getSemanasTranscurridas());
        }
        return entity;
    }

    public LiquidacionDTO mapOut(
            PrestamoModel entity
    ) {
        LiquidacionDTO DTO = new LiquidacionDTO();
        {
            // To easy code
            String cliente = entity.getNombres() + " " + entity.getApellidoPaterno() + " "
                    + entity.getApellidoMaterno();
            String semanaEntrega = "#" + entity.getSemana() + "-" + entity.getAnio();

            DTO.setCliente(cliente);
            DTO.setIdentificador(entity.getIdentificadorCredito());
            DTO.setSemEntrega(semanaEntrega);
            DTO.setEntregado(entity.getMontoOtorgado());
            DTO.setCargo(entity.getCargo());
            DTO.setMontoTotal(entity.getTotalAPagar());
            DTO.setCobrado(entity.getCobrado());
            DTO.setSaldo(entity.getSaldo());
            DTO.setPrestamoId(entity.getPrestamoId());
        }
        return DTO;
    }
}

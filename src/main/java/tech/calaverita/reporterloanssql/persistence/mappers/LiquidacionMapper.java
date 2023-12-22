package tech.calaverita.reporterloanssql.persistence.mappers;

import org.springframework.stereotype.Component;
import tech.calaverita.reporterloanssql.persistence.dto.LiquidacionDTO;
import tech.calaverita.reporterloanssql.persistence.entities.LiquidacionEntity;
import tech.calaverita.reporterloanssql.persistence.entities.view.PrestamoEntity;

@Component
public class LiquidacionMapper implements IMapper<LiquidacionEntity, LiquidacionDTO> {

    @Override
    public LiquidacionDTO mapOut(
            LiquidacionEntity out
    ) {
        return null;
    }

    @Override
    public LiquidacionEntity mapIn(
            LiquidacionDTO in
    ) {
        LiquidacionEntity entity = new LiquidacionEntity();
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
            PrestamoEntity entity
    ){
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

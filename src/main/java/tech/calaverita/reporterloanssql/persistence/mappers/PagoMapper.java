package tech.calaverita.reporterloanssql.persistence.mappers;

import tech.calaverita.reporterloanssql.persistence.dto.LiquidacionDTO;
import tech.calaverita.reporterloanssql.persistence.entities.PagoEntity;

public class PagoMapper {
    public PagoEntity mapIn(LiquidacionDTO in) {
        PagoEntity entity = new PagoEntity();
        {
            entity.setPrestamoId(in.getPrestamoId());
            entity.setPrestamo(in.getPrestamoId());
            entity.setMonto(in.getLiquidaCon());
            entity.setAbreCon(in.getSaldo());
            entity.setCliente(in.getCliente());
            entity.setIdentificador(in.getIdentificador());
        }
        return entity;
    }
}

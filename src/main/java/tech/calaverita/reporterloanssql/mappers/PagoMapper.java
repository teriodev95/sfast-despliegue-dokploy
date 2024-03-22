package tech.calaverita.reporterloanssql.mappers;

import org.springframework.stereotype.Component;
import tech.calaverita.reporterloanssql.dto.LiquidacionDTO;
import tech.calaverita.reporterloanssql.models.mariaDB.PagoModel;

@Component
public class PagoMapper {
    public PagoModel mapIn(
            LiquidacionDTO in
    ) {
        PagoModel entity = new PagoModel();
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

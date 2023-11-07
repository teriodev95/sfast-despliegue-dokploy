package tech.calaverita.reporterloanssql.pojos;

import lombok.Data;
import tech.calaverita.reporterloanssql.persistence.entities.PagoEntity;
import tech.calaverita.reporterloanssql.persistence.entities.view.PrestamoEntity;

@Data
public class PrestamoPago {
    PrestamoEntity prestamo;
    PagoEntity pago;
}

package tech.calaverita.reporterloanssql.pojos;

import lombok.Data;
import tech.calaverita.reporterloanssql.models.PagoModel;
import tech.calaverita.reporterloanssql.models.PrestamoModel;

@Data
public class PrestamoPago {
    PrestamoModel prestamo;
    PagoModel pago;
}

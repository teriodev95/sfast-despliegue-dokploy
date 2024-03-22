package tech.calaverita.reporterloanssql.pojos;

import lombok.Data;
import tech.calaverita.reporterloanssql.models.mariaDB.PagoModel;
import tech.calaverita.reporterloanssql.models.view.PrestamoModel;

@Data
public class PrestamoPago {
    PrestamoModel prestamo;
    PagoModel pago;
}

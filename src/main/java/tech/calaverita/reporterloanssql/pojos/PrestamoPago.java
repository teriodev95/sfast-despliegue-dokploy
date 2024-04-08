package tech.calaverita.reporterloanssql.pojos;

import lombok.Data;
import tech.calaverita.reporterloanssql.models.mariaDB.PagoModel;
import tech.calaverita.reporterloanssql.models.mariaDB.views.PrestamoModel;

@Data
public class PrestamoPago {
    PrestamoModel prestamo;
    PagoModel pago;
}

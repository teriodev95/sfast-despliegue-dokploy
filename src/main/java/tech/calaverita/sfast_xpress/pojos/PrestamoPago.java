package tech.calaverita.sfast_xpress.pojos;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.PagoModel;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoModel;

@Data
public class PrestamoPago {
    PrestamoModel prestamo;
    PagoModel pago;
}

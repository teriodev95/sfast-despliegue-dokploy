package tech.calaverita.sfast_xpress.pojos;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.PagoModel;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoViewModel;

@Data
public class PrestamoPago {
    PrestamoViewModel prestamo;
    PagoModel pago;
}

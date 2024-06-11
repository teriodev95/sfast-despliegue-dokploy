package tech.calaverita.sfast_xpress.retrofit.pojos;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.PagoModel;

import java.util.ArrayList;

@Data
public class PagoList {
    private ArrayList<PagoModel> pagos = new ArrayList<>();

    public PagoList() {

    }

    public PagoList(PagoModel pago) {
        this.pagos.add(pago);
    }
}

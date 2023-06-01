package tech.calaverita.reporterloanssql.retrofit.pojos;

import lombok.Data;
import tech.calaverita.reporterloanssql.models.PagoModel;

import java.util.ArrayList;

@Data
public class PagoList {
    public PagoList() {

    }

    public PagoList(PagoModel pago) {
        this.pagos.add(pago);
    }

    private ArrayList<PagoModel> pagos = new ArrayList<>();
}

package tech.calaverita.reporterloanssql.retrofit.pojos;

import lombok.Data;
import tech.calaverita.reporterloanssql.persistence.entities.PagoEntity;

import java.util.ArrayList;

@Data
public class PagoList {
    private ArrayList<PagoEntity> pagos = new ArrayList<>();

    public PagoList() {

    }

    public PagoList(PagoEntity pago) {
        this.pagos.add(pago);
    }
}

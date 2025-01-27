package tech.calaverita.sfast_xpress.pojos.PWA;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoViewModel;

@Data
public class CobranzaPWA {
    ArrayList<PrestamoCobranzaPWA> cobranza;

    public CobranzaPWA() {

    }

    public CobranzaPWA(List<PrestamoViewModel> prestamosViewModels) {
        this.cobranza = new ArrayList<>();
        for (PrestamoViewModel prestamoViewModel : prestamosViewModels) {
            this.cobranza.add(new PrestamoCobranzaPWA(prestamoViewModel));
        }
    }
}

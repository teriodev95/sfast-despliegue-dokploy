package tech.calaverita.sfast_xpress.retrofit.pojos;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.AsignacionModel;

import java.util.ArrayList;

@Data
public class AsignacionList {
    private ArrayList<AsignacionModel> asignaciones = new ArrayList<>();

    public AsignacionList() {

    }

    public AsignacionList(AsignacionModel asignacion) {
        this.asignaciones.add(asignacion);
    }
}

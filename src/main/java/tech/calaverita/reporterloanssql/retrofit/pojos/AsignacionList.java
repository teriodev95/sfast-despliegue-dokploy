package tech.calaverita.reporterloanssql.retrofit.pojos;

import lombok.Data;
import tech.calaverita.reporterloanssql.models.AsignacionModel;

import java.util.ArrayList;

@Data
public class AsignacionList {
    public AsignacionList() {

    }

    public AsignacionList(AsignacionModel asignacion) {
        this.asignaciones.add(asignacion);
    }

    private ArrayList<AsignacionModel> asignaciones = new ArrayList<>();
}

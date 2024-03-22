package tech.calaverita.reporterloanssql.retrofit.pojos;

import lombok.Data;
import tech.calaverita.reporterloanssql.models.mariaDB.AsignacionModel;

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

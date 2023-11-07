package tech.calaverita.reporterloanssql.retrofit.pojos;

import lombok.Data;
import tech.calaverita.reporterloanssql.persistence.entities.AsignacionEntity;

import java.util.ArrayList;

@Data
public class AsignacionList {
    private ArrayList<AsignacionEntity> asignaciones = new ArrayList<>();

    public AsignacionList() {

    }

    public AsignacionList(AsignacionEntity asignacion) {
        this.asignaciones.add(asignacion);
    }
}

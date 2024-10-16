package tech.calaverita.sfast_xpress.DTOs.solicitud;

import lombok.Data;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class ActivoDTO {
    private String activo;
    private String marca;
    private String antiguedad;
    private String noSerie;
    private Object valorAprox;

    public ActivoDTO(String activo, String marca, String antiguedad, String noSerie, Object valorAprox) {
        this.activo = activo;
        this.marca = marca;
        this.antiguedad = antiguedad;
        this.noSerie = noSerie;
        this.valorAprox = valorAprox;

        monetaryToDouble();
    }

    public void monetaryToDouble() {
        this.valorAprox = MyUtil.monetaryToDouble(this.valorAprox);
    }
}

package tech.calaverita.reporterloanssql.pojos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@Data
@JsonSerialize
public class PreguntaCallCenter {
    private String pregunta;
    private String respuesta;
    private Integer puntuacion;
}

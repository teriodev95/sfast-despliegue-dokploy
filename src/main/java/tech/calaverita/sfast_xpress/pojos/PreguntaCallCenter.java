package tech.calaverita.sfast_xpress.pojos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@Data
@JsonSerialize
public class PreguntaCallCenter {
    private String pregunta;
    private String respuesta;
    private Integer puntuacion;

    public PreguntaCallCenter() {
        this.pregunta = "";
        this.respuesta = "";
        this.puntuacion = 0;
    }
}

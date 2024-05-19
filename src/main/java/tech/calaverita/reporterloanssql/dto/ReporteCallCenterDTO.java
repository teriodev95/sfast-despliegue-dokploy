package tech.calaverita.reporterloanssql.dto;

import lombok.Data;
import tech.calaverita.reporterloanssql.pojos.PreguntaCallCenter;

import java.util.ArrayList;

@Data
public class ReporteCallCenterDTO {
    private String nombre_atiende_aval;
    private String nombre_atiende_cliente;
    private String nombres_aval;
    private String nombres_cliente;
    private Integer num_llamadas_aval;
    private Integer num_llamadas_cliente;
    private String observaciones_aval;
    private String observaciones_cliente;
    private String prestamoId;
    private String status_llamada_aval;
    private String status_llamada_cliente;
    private String url_llamada_aval;
    private String url_llamada_cliente;
    private ArrayList<PreguntaCallCenter> preguntas_cliente;
    private ArrayList<PreguntaCallCenter> preguntas_aval;
}

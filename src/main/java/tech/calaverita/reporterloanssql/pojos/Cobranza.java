package tech.calaverita.reporterloanssql.pojos;

import lombok.Data;
import tech.calaverita.reporterloanssql.persistence.entities.view.PrestamoEntity;

import java.util.ArrayList;

@Data
public class Cobranza {
    private String gerencia;
    private String agencia;
    private Integer anio;
    private Integer semana;
    private Integer clientes;
    private Double debitoMiercoles;
    private Double debitoJueves;
    private Double debitoViernes;
    private Double debitoTotal;
    private ArrayList<PrestamoEntity> prestamos;
}

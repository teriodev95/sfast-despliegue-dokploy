package tech.calaverita.sfast_xpress.pojos;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoModel;

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
    private ArrayList<PrestamoModel> prestamos;
}

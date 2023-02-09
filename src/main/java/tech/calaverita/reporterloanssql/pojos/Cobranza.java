package tech.calaverita.reporterloanssql.pojos;

import lombok.Data;
import tech.calaverita.reporterloanssql.models.PrestamoModel;

import java.util.ArrayList;

@Data
public class Cobranza {
    private String gerencia;
    private String agencia;
    private int anio;
    private int semana;
    private int clientes;
    private double debitoMiercoles;
    private double debitoJueves;
    private double debitoViernes;
    private double debitoTotal;
    private ArrayList<PrestamoModel> prestamos;
}

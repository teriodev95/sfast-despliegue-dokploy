package tech.calaverita.reporterloanssql.pojos.pwa;

import lombok.Data;
import tech.calaverita.reporterloanssql.models.VisitaModel;

import java.util.ArrayList;

@Data
public class PagoHistoricoPWA {
    private Double monto;
    private String tipo;
    private String prestamoId;
    private String prestamo;
    private int semana;
    private int anio;
    private Double tarifa;
    private String cliente;
    private String agente;
    private String identificador;
    private ArrayList<PagoPWA> pagos;
    private ArrayList<VisitaModel> visitas;
}

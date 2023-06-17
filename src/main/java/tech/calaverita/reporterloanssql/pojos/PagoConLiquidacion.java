package tech.calaverita.reporterloanssql.pojos;

import lombok.Data;
import tech.calaverita.reporterloanssql.models.LiquidacionModel;

@Data
public class PagoConLiquidacion {
    private String pagoId;
    private String prestamoId;
    private String prestamo;
    private Double monto;
    private Integer semana;
    private Integer anio;
    private Boolean esPrimerPago;
    private Double abreCon;
    private Double cierraCon;
    private Double tarifa;
    private String cliente;
    private String agente;
    private String tipo;
    private String creadoDesde;
    private String identificador;
    private String fechaPago;
    private Double lat;
    private Double lng;
    private String comentario;
    private String datosMigracion;
    private String createdAt;
    private String updatedAt;
    private String log;
    private String quienPago;
    private LiquidacionModel infoLiquidacion;
}

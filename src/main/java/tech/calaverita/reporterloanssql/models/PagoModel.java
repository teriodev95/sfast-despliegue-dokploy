package tech.calaverita.reporterloanssql.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pagos_v3")
public class PagoModel {
    @Id
    @Column(name = "pagoid")
    private String pagoId;
    @Column(name = "prestamoid")
    private String prestamoId;
    private String prestamo;
    private double monto;
    private int semana;
    private int anio;
    @Column(name = "esprimerpago")
    private boolean esPrimerPago;
    @Column(name = "abrecon")
    private double abreCon;
    @Column(name = "cierracon")
    private double cierraCon;
    private double tarifa;
    private String cliente;
    private String agente;
    private String tipo;
    private String creadoDesde;
    private String identificador;
    private String fechaPago;
    private double lat;
    private double lng;
    private String comentario;
    private String datosMigracion;
    private String createdAt;
    private String updatedAt;
}


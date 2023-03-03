package tech.calaverita.reporterloanssql.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Double monto;
    private Integer semana;
    private Integer anio;
    @Column(name = "esprimerpago")
    private Boolean esPrimerPago;
    @Column(name = "abrecon")
    private Double abreCon;
    @Column(name = "cierracon")
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
}


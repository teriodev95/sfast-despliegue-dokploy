package tech.calaverita.sfast_xpress.models.mariaDB.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "pagos_agrupados")
public class PagoAgrupadoModel {
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
    private String quienPago;
}


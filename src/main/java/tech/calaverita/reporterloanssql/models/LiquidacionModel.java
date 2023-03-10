package tech.calaverita.reporterloanssql.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "liquidaciones")
public class LiquidacionModel {
    @Id
    @Column(name = "liquidacionID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer liquidacionId;
    @Column(name = "prestamoID")
    private String prestamoId;
    @Column(name = "pagoID")
    private String pagoId;
    private Integer anio;
    private Integer semana;
    private Double descuentoEnDinero;
    private Integer descuentoEnPorcentaje;
    private Double liquidoCon;
    private Integer semTranscurridas;
}

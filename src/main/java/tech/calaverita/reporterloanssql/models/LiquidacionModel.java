package tech.calaverita.reporterloanssql.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "liquidaciones")
public class LiquidacionModel {
    @Id
    @Column(name = "liquidacionid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer liquidacionId;
    @Column(name = "prestamoid")
    private String prestamoId;
    @Column(name = "pagoid")
    private String pagoId;
    private Integer anio;
    private Integer semana;
    private Double descuentoEnDinero;
    private Integer descuentoEnPorcentaje;
    private Double liquidoCon;
    private Integer semTranscurridas;
}

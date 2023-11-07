package tech.calaverita.reporterloanssql.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "liquidaciones")
public class LiquidacionEntity {
    @Id
    @Column(name = "liquidacionid")
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

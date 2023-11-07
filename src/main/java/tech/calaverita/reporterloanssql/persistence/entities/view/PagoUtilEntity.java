package tech.calaverita.reporterloanssql.persistence.entities.view;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "pagos_util")
public class PagoUtilEntity {
    @Id
    private String prestamoId;
    private Double monto;
    private Integer semana;
    private Integer anio;
    private Boolean esPrimerPago;
    private Double abreCon;
    private Double cierraCon;
    private Double tarifa;
    private String agente;
    private String tipo;
    private String fechaPago;
}


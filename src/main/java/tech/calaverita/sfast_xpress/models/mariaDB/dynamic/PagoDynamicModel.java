package tech.calaverita.sfast_xpress.models.mariaDB.dynamic;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "pagos_dynamic")
public class PagoDynamicModel {
    @Id
    private String pagoId;
    private String prestamoId;
    private String prestamo;
    private String cliente;
    private Double monto;
    private Integer semana;
    private Integer anio;
    private Boolean esPrimerPago;
    private Double abreCon;
    private Double cierraCon;
    private Double tarifa;
    private String agencia;
    private String tipo;
    private String fechaPago;
    private String identificador;
    private String quienPago;
    private String comentario;
    private Double lat;
    private Double lng;
}

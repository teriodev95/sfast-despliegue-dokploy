package tech.calaverita.sfast_xpress.models.mariaDB;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "ventas")
public class VentaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String fecha;
    private Integer anio;
    private Integer semana;
    private String agencia;
    private String gerencia;
    private String nombreCliente;
    private String tipo;
    private String nivel;
    private Integer plazo;
    private Double monto;
    private Double primerPago;
    private String createdAt;
}

package tech.calaverita.sfast_xpress.models.mariaDB;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "gastos")
public class GastoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gasto_id")
    private Integer id;
    private Integer creadoPorId;
    private String gerencia;
    private String tipoGasto;
    private String fecha;
    private Integer semana;
    private Integer anio;
    private Double monto;
    private Double litros;
    private String concepto;
    private String urlRecibo;
    private Boolean reembolsado;
    private String createdAt;
    private String updatedAt;
    @ManyToOne
    @JoinColumn(name = "creadoPorId", insertable = false, updatable = false)
    @JsonIgnore
    private UsuarioModel usuarioModel;
}

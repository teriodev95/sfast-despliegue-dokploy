package tech.calaverita.sfast_xpress.models.mariaDB;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "comisiones")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ComisionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer semana;
    private String mes;
    private Integer anio;
    private String agencia;
    private String gerencia;
    private Double debito;
    private String nivel;
    private Double cobranzaPura;
    private Double rendimiento;
    private Double montoExcedente;
    private Double cobranzaTotal;
    private Double primerosPagos;
    private Integer porcentajeComisionCobranza;
    private Integer porcentajeBonoMensual;
    private Double comisionCobranza;
    private Double comisionVentas;
    private Double bonos;
}

package tech.calaverita.sfast_xpress.models.mariaDB;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tabulaciones_dinero")
public class TabulacionDineroModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer anio;
    private Integer semana;
    private String gerencia;
    @Column(name = "cantidad_50_centavos")
    private Integer cantidad50Centavos;
    @Column(name = "cantidad_1_peso")
    private Integer cantidad1Peso;
    @Column(name = "cantidad_2_pesos")
    private Integer cantidad2Pesos;
    @Column(name = "cantidad_5_pesos")
    private Integer cantidad5Pesos;
    @Column(name = "cantidad_10_pesos")
    private Integer cantidad10Pesos;
    @Column(name = "cantidad_20_pesos")
    private Integer cantidad20Pesos;
    @Column(name = "cantidad_20_billetes")
    private Integer cantidad20Billetes;
    @Column(name = "cantidad_50_billetes")
    private Integer cantidad50Billetes;
    @Column(name = "cantidad_100_billetes")
    private Integer cantidad100Billetes;
    @Column(name = "cantidad_200_billetes")
    private Integer cantidad200Billetes;
    @Column(name = "cantidad_500_billetes")
    private Integer cantidad500Billetes;
    @Column(name = "cantidad_1000_billetes")
    private Integer cantidad1000Billetes;
    private String createdAt;
    private String updatedAt;

    public TabulacionDineroModel() {
        this.cantidad50Centavos = 0;
        this.cantidad1Peso = 0;
        this.cantidad2Pesos = 0;
        this.cantidad5Pesos = 0;
        this.cantidad10Pesos = 0;
        this.cantidad20Pesos = 0;
        this.cantidad20Billetes = 0;
        this.cantidad50Billetes = 0;
        this.cantidad100Billetes = 0;
        this.cantidad200Billetes = 0;
        this.cantidad500Billetes = 0;
        this.cantidad1000Billetes = 0;
    }
}
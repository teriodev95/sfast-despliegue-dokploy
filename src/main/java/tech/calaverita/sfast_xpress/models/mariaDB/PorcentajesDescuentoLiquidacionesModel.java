package tech.calaverita.sfast_xpress.models.mariaDB;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "porcentajes_descuento_liquidaciones")
public class PorcentajesDescuentoLiquidacionesModel {
    @Id
    private String id;
    private int semana1;
    private int semana2;
    private int semana3;
    private int semana4;
    private int semana5;
    private int semana6;
    private int semana7;
    private int semana8;
    private int semana9;
    private int semana10;
    private int semana11;
    private int semana12;
    private int semana13;
    private int semana14;
    private int semana15;
    private int semana16;
    private int semana17;
    private int semana18;
    private int semana19;
    private int semana20;
    private int semana21;
    private int semana22;
    private int semana23;
    private int semana24;
    private int semana25;
}

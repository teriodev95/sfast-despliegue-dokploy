package tech.calaverita.reporterloanssql.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "sucursales")
public class SucursalModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sucursalid")
    private Integer sucursalId;
    private String nombre;
    @Column(name = "regionalid")
    private Integer regionalId;
}

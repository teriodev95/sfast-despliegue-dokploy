package tech.calaverita.reporterloanssql.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "sucursales")
public class SucursalModel {
    @Id
    @Column(name = "sucursalid")
    private String sucursalId;
    private String nombre;
    @Column(name = "regionalid")
    private Integer regionalId;
}

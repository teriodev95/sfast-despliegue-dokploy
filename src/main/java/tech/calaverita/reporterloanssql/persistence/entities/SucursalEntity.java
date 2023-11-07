package tech.calaverita.reporterloanssql.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "sucursales")
public class SucursalEntity {
    @Id
    @Column(name = "sucursalid")
    private String sucursalId;
    private String nombre;
    @Column(name = "regionalid")
    private Integer regionalId;
}

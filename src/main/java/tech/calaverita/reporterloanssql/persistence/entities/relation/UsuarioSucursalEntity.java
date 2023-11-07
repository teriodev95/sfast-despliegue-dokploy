package tech.calaverita.reporterloanssql.persistence.entities.relation;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "usuarios_sucursales")
public class UsuarioSucursalEntity {
    @Id
    private Integer usuarioId;
    private String sucursalId;
}

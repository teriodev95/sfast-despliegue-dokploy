package tech.calaverita.reporterloanssql.models.mariaDB.relation;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "usuarios_gerencias")
public class UsuarioGerenciaModel {
    @Id
    private Integer usuarioId;
    private String gerenciaId;
}

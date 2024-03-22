package tech.calaverita.reporterloanssql.pojos;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import tech.calaverita.reporterloanssql.models.mariaDB.UsuarioModel;

@Data
public class BalanceAgencia {
    private ResponseEntity<Dashboard> dashboard;
    private UsuarioModel agente;
    private UsuarioModel gerente;
    private Double asignaciones;
}

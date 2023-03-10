package tech.calaverita.reporterloanssql.pojos;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import tech.calaverita.reporterloanssql.controllers.UsuarioController;
import tech.calaverita.reporterloanssql.models.UsuarioModel;

@Data
public class BalanceAgencia {
    private ResponseEntity<Dashboard> dashboard;
    private UsuarioModel agente;
    private UsuarioModel gerente;
    private Double asignaciones;
}

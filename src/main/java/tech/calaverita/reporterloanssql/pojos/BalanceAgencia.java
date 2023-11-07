package tech.calaverita.reporterloanssql.pojos;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import tech.calaverita.reporterloanssql.persistence.entities.UsuarioEntity;

@Data
public class BalanceAgencia {
    private ResponseEntity<Dashboard> dashboard;
    private UsuarioEntity agente;
    private UsuarioEntity gerente;
    private Double asignaciones;
}

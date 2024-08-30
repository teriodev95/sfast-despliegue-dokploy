package tech.calaverita.sfast_xpress.pojos;

import lombok.Data;
import org.springframework.http.ResponseEntity;

import tech.calaverita.sfast_xpress.DTOs.dashboard.DashboardDTO;
import tech.calaverita.sfast_xpress.models.mariaDB.UsuarioModel;

@Data
public class BalanceAgencia {
    private ResponseEntity<DashboardDTO> dashboard;
    private UsuarioModel agente;
    private UsuarioModel gerente;
    private Double asignaciones;
}

package tech.calaverita.reporterloanssql.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.repositories.*;

@Data
@Service
@AllArgsConstructor
public class RepositoriesContainer {
    private AsignacionRepository asignacionRepository;
    private LiquidacionRepository liquidacionRepository;
    private PagoRepository pagoRepository;
    private PrestamoRepository prestamoRepository;
    private UsuarioRepository usuarioRepository;
    private XpressRepository xpressRepository;
    private GerenciaRepository gerenciaRepository;
    private AgenciaRepository agenciaRepository;
    private CalendarioRepository calendarioRepository;
}

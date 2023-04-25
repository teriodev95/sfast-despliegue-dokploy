package tech.calaverita.reporterloanssql.services;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.repositories.*;

@Data
@Service
public class RepositoriesContainer {
    @Autowired
    private AsignacionRepository asignacionRepository;
    @Autowired
    private LiquidacionRepository liquidacionRepository;
    @Autowired
    private PagoRepository pagoRepository;
    @Autowired
    private PrestamoRepository prestamoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private XpressRepository xpressRepository;
}

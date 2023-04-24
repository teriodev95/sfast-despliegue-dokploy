package tech.calaverita.reporterloanssql.pojos;

import lombok.Data;
import tech.calaverita.reporterloanssql.repositories.*;

@Data
public class Repositories {
    private AsignacionRepository asignacionRepository;
    private LiquidacionRepository liquidacionRepository;
    private PagoRepository pagoRepository;
    private PrestamoRepository prestamoRepository;
    private UsuarioRepository usuarioRepository;
    private XpressRepository xpressRepository;
}

package tech.calaverita.reporterloanssql.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.repositories.UsuarioSucursalRepository;

@Service
public class UsuarioSucursalService {
    private static UsuarioSucursalRepository usuarioSucursalRepository;

    @Autowired
    public UsuarioSucursalService(UsuarioSucursalRepository usuarioSucursalRepository) {
        UsuarioSucursalService.usuarioSucursalRepository = usuarioSucursalRepository;
    }

    public static String getSucursalIdByUsuarioId(int usuarioId) {
        return usuarioSucursalRepository.getSucursalIdByUsuarioId(usuarioId);
    }
}

package tech.calaverita.reporterloanssql.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.repositories.UsuarioSucursalRepository;

import java.util.ArrayList;

@Service
public class UsuarioSucursalService {
    private static UsuarioSucursalRepository usuarioSucursalRepository;

    @Autowired
    public UsuarioSucursalService(UsuarioSucursalRepository usuarioSucursalRepository) {
        UsuarioSucursalService.usuarioSucursalRepository = usuarioSucursalRepository;
    }

    public static ArrayList<String> getSucursalIdsByUsuarioId(int usuarioId) {
        return usuarioSucursalRepository.getSucursalIdsByUsuarioId(usuarioId);
    }
}

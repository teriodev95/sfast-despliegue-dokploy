package tech.calaverita.reporterloanssql.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.repositories.UsuarioGerenciaRepository;

import java.util.ArrayList;

@Service
public class UsuarioGerenciaService {
    private static UsuarioGerenciaRepository usuarioGerenciaRepository;

    @Autowired
    public UsuarioGerenciaService(UsuarioGerenciaRepository usuarioGerenciaRepository) {
        UsuarioGerenciaService.usuarioGerenciaRepository = usuarioGerenciaRepository;
    }

    public static ArrayList<String> getGerenciaIdsByUsuarioId(int usuarioId) {
        return usuarioGerenciaRepository.getGerenciaIdsByUsuarioId(usuarioId);
    }
}

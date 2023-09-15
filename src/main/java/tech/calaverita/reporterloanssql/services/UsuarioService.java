package tech.calaverita.reporterloanssql.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.UsuarioModel;
import tech.calaverita.reporterloanssql.repositories.UsuarioRepository;

import java.util.ArrayList;

@Service
public class UsuarioService {
    private static UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        UsuarioService.usuarioRepository = usuarioRepository;
    }

    public static UsuarioModel findOneByUsuarioAndPin(String usuario, String pin) {
        return usuarioRepository.findOneByUsuarioAndPin(usuario, pin);
    }

    public static UsuarioModel findOneByUsuario(String usuario) {
        return usuarioRepository.findOneByUsuario(usuario).orElseThrow();
    }

    public static ArrayList<UsuarioModel> findManyByTipo(String tipo) {
        return usuarioRepository.findManyByTipo(tipo);
    }

    public static UsuarioModel findOneByUsuarioId(int usuarioId) {
        return usuarioRepository.findOneByUsuarioId(usuarioId);
    }

    public static UsuarioModel findOneByUsuarioIdFromGerenciaIdOfGerenciaModel(String gerenciaId) {
        return usuarioRepository.findOneByUsuarioIdFromGerenciaIdOfGerenciaModel(gerenciaId);
    }

    public static ArrayList<UsuarioModel> findManyByGerencia(String gerencia){
        return usuarioRepository.findManyByGerencia(gerencia);
    }
}
